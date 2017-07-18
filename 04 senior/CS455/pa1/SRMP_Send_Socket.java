import java.util.*;
import java.io.*;
import java.net.*;

public class SRMP_Send_Socket {
	
	final int PACKET_TIMEOUT_VALUE = 2000; // milliseconds
	final int SOCKET_RECEIVE_TIMEOUT = 1; // milliseconds
	final int SYN_PACKET_ATTEMPTS = 4;
	final int FINACK_TIMEOUTS = 3;
	final int SEQUENCE_NUMBER_MAX = 65536;
	final int MAX_PACKET_SIZE = 1000;
	DatagramSocket udp_socket;
	InetAddress destAddress;
	int ISN, destPort, seqno, window;
	int packetsInFlight = 0, bytesInFlight = 0, packetsAcked = 0;
	int lastByteAcked = 0, lastByteSent = 0, bytesSent = 0, bytesTotal = 0;
	String status = "";
	ArrayList<SRMP_Packet> bufferedPackets;
	
	public SRMP_Send_Socket ()
	{
	}
	
	public int srmp_open (String destHostname, int destPort, int srcPort) throws SocketException, UnknownHostException, IOException
	{
		// open the socket pointed to destination
		System.out.println("UDP socket being created...");
		udp_socket = new DatagramSocket(srcPort);
		System.out.println("UDP socket locally bound to <" + udp_socket.getLocalAddress() + ", " + udp_socket.getLocalPort() + ">");
		udp_socket.setSoTimeout(PACKET_TIMEOUT_VALUE);
		System.out.println("UDP socket's receive timeout set to " + PACKET_TIMEOUT_VALUE + " ms");
		udp_socket.setReceiveBufferSize(MAX_PACKET_SIZE);
		System.out.println("UDP socket's receive buffer size set to " + MAX_PACKET_SIZE + " bytes");
		udp_socket.setSendBufferSize(MAX_PACKET_SIZE);
		System.out.println("UDP socket's send buffer size set to " + MAX_PACKET_SIZE + " bytes");
		destAddress = InetAddress.getByName(destHostname);
		this.destPort = destPort;
		System.out.println("UDP socket pointed to <" + this.destAddress + ", " + this.destPort + ">");
		System.out.println("UDP socket created.\n");
		
		// create new SRMP packet to send
		SRMP_Packet SRMP_synPacket = new SRMP_Packet();
		// set packet type to SYN
		SRMP_synPacket.header.setType(2);
		// set a random initial sequence number (ISN)
		int ISN = 65535; //(new Random()).nextInt(SEQUENCE_NUMBER_MAX);
		SRMP_synPacket.header.setSeqno(ISN);
		
		// create SRMP packet to receive
		SRMP_Packet SRMP_synackPacket = null;
		
		// try to transmit SYN packet SYN_PACKET_ATTEMPTS times
		int timeouts = 0;
		while (timeouts < SYN_PACKET_ATTEMPTS && SRMP_synackPacket == null)
		{
			try {
				// send SYN to receiver
				srmp_send_through_socket(SRMP_synPacket, timeouts == 0 ? false : true);
				// wait for SYNACK from receiver
				SRMP_synackPacket = srmp_receive_through_socket();
			} catch (IOException e) {
				System.out.println("SYNACK timeout detected");
				timeouts++;
			}
		}
		
		if (timeouts == SYN_PACKET_ATTEMPTS)
		{
			System.err.println("receiver didn't ACK after " + SYN_PACKET_ATTEMPTS + " SYN attempts");
			// send a RESET packet
			sendResetPacket();
			return 0;
		} else {
			// set this socket's variables to received constraints
			this.ISN = SRMP_synackPacket.header.getSeqno();
			this.seqno = SRMP_synackPacket.header.getSeqno();
			this.window = SRMP_synackPacket.header.getWindow();
			
			// initialize buffer
			this.bufferedPackets = new ArrayList<SRMP_Packet>();
		}
		
		udp_socket.setSoTimeout(SOCKET_RECEIVE_TIMEOUT);
		System.out.println("UDP socket's receive timeout set to " + SOCKET_RECEIVE_TIMEOUT + " ms");
		
		return 1;
	}
	
	/**
	 * Reliably transfers the entire file using the SRMP protocol.
	 * 
	 * Algorithm:
	 * 1. upon message receipt, assign it a seqno and put it in the buffered queue
	 * 2. process all ACKs (mark it in the queue)
	 * 3. go through the inFlight packets and mark it as not inFlight if it's timed out
	 * 4. while there are more eligible (non-ACKed) packets and (the window isn't full || window < 1000),
	 * 		transmit the next eligible packet
	 * @return number of packets left to send
	 */
	public int srmp_send (byte[] data, int len)
	{
		if (len != 0)
		{
			// create new SRMP packet to send
			SRMP_Packet SRMP_buffPacket = new SRMP_Packet();
			// set packet type to DATA
			SRMP_buffPacket.header.setType(0);
			// set packet data
			SRMP_buffPacket.payload.setData(data);
			// set packet seqno
			SRMP_buffPacket.header.setSeqno(this.seqno);
			// increase current seqno
			this.seqno = (this.seqno+SRMP_buffPacket.payload.getLength())%SEQUENCE_NUMBER_MAX;
			// increase total size
			bytesTotal += SRMP_buffPacket.payload.getLength();
			// buffer the packet
			this.bufferedPackets.add(SRMP_buffPacket);
		}
		
		// process any awaiting ACKS
		SRMP_Packet SRMP_ackPacket = null;
		try {
			while ((SRMP_ackPacket = srmp_receive_through_socket()) != null)
			{
				// determine the seqno range which this ACK packet acknowledges
				int ackSeqno = SRMP_ackPacket.header.getSeqno() - 1;
				int ackWindow = SRMP_ackPacket.header.getWindow();
				int lowCap, highCap;
				boolean wraparound = false;
				if (ackSeqno - ackWindow > 0)
				{ // no wraparound
					lowCap = ackSeqno - ackWindow;
					highCap = ackSeqno;
				} else { // wraparound
					lowCap = SEQUENCE_NUMBER_MAX + (ackSeqno - ackWindow);
					highCap = ackSeqno;
					wraparound = true;
				}
				// process the ACK
				for (SRMP_Packet p : bufferedPackets)
				{
					if (!p.ACKed && p.inFlight)
					{
						int packetSeqno = p.header.getSeqno();
						int payloadLength = p.payload.getLength();
						//System.out.println(packetSeqno + " in [" + lowCap + ", " + highCap + "]?");
						if (wraparound)
						{
							if (packetSeqno >= lowCap || packetSeqno <= highCap)
							{
								p.ACKed = true;
								p.inFlight = false;
								lastByteAcked = packetSeqno + payloadLength - 1; // update the lastByteAcked if this ACK seqno is higher
							}
						} else {
							if (packetSeqno >= lowCap && packetSeqno <= highCap)
							{
								p.ACKed = true;
								p.inFlight = false;
								lastByteAcked = packetSeqno + payloadLength - 1; // update the lastByteAcked if this ACK seqno is higher
							}
						}
						
						if (p.ACKed)
						{
							packetsAcked++;
							packetsInFlight--;
							bytesInFlight -= p.payload.getLength();
							bytesSent += p.payload.getLength();
							System.out.println("bytes " + p.header.getSeqno() + " to " + (p.header.getSeqno()+p.payload.getLength()-1) + " ACKed, total sent: " + bytesSent + "/" + bytesTotal);
						}
					}
				}
				// update socket window
				this.window = SRMP_ackPacket.header.getWindow();
			}
		} catch (IOException e) {
			SRMP_ackPacket = null;
			//System.out.println("no more ACKs to process");
		}
		
		// retransmit any timed out packets by marking them as not inFlight
		for (SRMP_Packet p : bufferedPackets)
		{
			if (!p.ACKed && p.inFlight == true)
			{
				if ((Calendar.getInstance()).getTime().getTime() - p.timestamp.getTime() > PACKET_TIMEOUT_VALUE)
				{
					System.out.println("-- packet " + p.header.getSeqno() + " timed out");
					// queue this packet to be resent
					p.inFlight = false;
					// decrement packetsInFlight and bytesInFlight
					packetsInFlight--;
					bytesInFlight -= p.payload.getLength();
					
					System.out.println("ACKed-inFlight-total: " + packetsAcked + "-" + packetsInFlight + "-" + bufferedPackets.size() + " packets | lastByteAcked = "
							+ lastByteAcked + " | lastByteSent = " + lastByteSent + " | bytesInFlight/window = " + bytesInFlight + "/" + this.window);
				}
			}
		}
		
		// grab next eligible packet (not ACKed and not inFlight)
		SRMP_Packet SRMP_sendPacket = null;
		for (SRMP_Packet p : bufferedPackets)
		{
			if (!p.ACKed && !p.inFlight)// && (packet fits in the current window))
			{
				SRMP_sendPacket = p;
				break;
			}
		}
		// until the sliding window is full or there's no more eligible packets
		// or (there are no bytes in flight and window is less than MAX_PACKET_SIZE (we need to "poke" the receiver))
		while (((this.bufferedPackets.size()-packetsInFlight-packetsAcked) > 0 && (bytesInFlight+SRMP_sendPacket.payload.getLength()) <= this.window) || (bytesInFlight == 0 && this.window < MAX_PACKET_SIZE))
		{ // transmit new packets
			try { // transmit a buffered packet and mark it as in flight
				if (SRMP_sendPacket != null)
				{
					SRMP_sendPacket.setTimestamp((Calendar.getInstance()).getTime()); // set the packet's timestamp
					SRMP_sendPacket.inFlight = true; // mark packet as in flight
					srmp_send_through_socket(SRMP_sendPacket, false); // send through socket
					packetsInFlight++; // increment packetsInFlight counter
					bytesInFlight += SRMP_sendPacket.payload.getLength();
					lastByteSent = SRMP_sendPacket.header.getSeqno()+SRMP_sendPacket.payload.getLength()-1; // increase lastByteSent
				}
				// get the next eligible packet
				SRMP_sendPacket = null;
				for (SRMP_Packet p : bufferedPackets)
				{
					if (!p.ACKed && !p.inFlight)// && (packet fits in the current window))
					{
						SRMP_sendPacket = p;
						break;
					}
				}
			} catch (IOException e) {
				System.err.println("data packet send into socket failed");
				System.exit(1);
			}
		}
		
		String newStatus = "ACKed-inFlight-total: " + packetsAcked + "-" + packetsInFlight + "-" + bufferedPackets.size() + " packets | lastByteAcked = "
		+ lastByteAcked + " | lastByteSent = " + lastByteSent + " | bytesInFlight/window = " + bytesInFlight + "/" + this.window;
		if (!newStatus.equals(status))
		{
			status = newStatus;
			System.out.println(status);
		}
		
		return this.bufferedPackets.size()-packetsAcked;
	}
	
	public void srmp_send_through_socket(SRMP_Packet pkt, boolean timedOut) throws IOException
	{
		byte[] sendData = pkt.GetNetworkReadyPacket();
		DatagramPacket datagram = new DatagramPacket(sendData, sendData.length, destAddress, destPort);
		udp_socket.send(datagram);
		
		System.out.printf("-- sent %4s seq %05d win %05d len %04d%s\n",
				pkt.header.getTypeString(), pkt.header.getSeqno(),
				pkt.header.getWindow(), pkt.getLength(), timedOut ? " (retransmission)" : "");
	}
	
	public SRMP_Packet srmp_receive_through_socket() throws IOException
	{
		DatagramPacket datagram = new DatagramPacket(new byte[1000], 1000);
		//System.out.println("waiting for ACK for " + PACKET_TIMEOUT_VALUE + " ms...");
		udp_socket.receive(datagram);
		
		// encapsulate received data into an SRMP packet for easier processing
		byte[] receiveData = new byte[datagram.getLength()];
		System.arraycopy(datagram.getData(), 0, receiveData, 0, datagram.getLength());
		SRMP_Packet SRMP_receivePacket = new SRMP_Packet(receiveData);
		
		System.out.printf("-- rcvd %4s seq %05d win %05d len %04d\n",
				SRMP_receivePacket.header.getTypeString(), SRMP_receivePacket.header.getSeqno(),
				SRMP_receivePacket.header.getWindow(), SRMP_receivePacket.getLength());
		
		// if we receive a RESET packet, close connection
		if (SRMP_receivePacket.header.getType() == 4)
		{
			System.err.println("RESET packet received, closing connection!");
			System.exit(1);
		}
		
		return SRMP_receivePacket;
	}
	
	public int srmp_close () throws SocketException
	{
		// verify all packets have been sent and ACKed
		while (srmp_send(null, 0) > 0);
		
		udp_socket.setSoTimeout(PACKET_TIMEOUT_VALUE);
		System.out.println("\nUDP socket's receive timeout set to " + PACKET_TIMEOUT_VALUE + " ms to prepare for FIN packet");
		
		// create new SRMP packet to send
		SRMP_Packet SRMP_sendPacket = new SRMP_Packet();
		// set packet type to FIN
		SRMP_sendPacket.header.setType(3);
		// set packet seqno
		SRMP_sendPacket.header.setSeqno(this.seqno);
		// send it over the socket
		try {
			srmp_send_through_socket(SRMP_sendPacket, false);
		} catch (IOException e) {
			System.err.println("FIN packet send failed");
			return 0;
		}
		// wait for FINACK for 3 timeouts
		SRMP_Packet SRMP_receivePacket = null;
		int timeouts = 0;
		while (timeouts < FINACK_TIMEOUTS && SRMP_receivePacket == null)
		{
			try {
				SRMP_receivePacket = srmp_receive_through_socket();
			} catch (IOException e) {
				timeouts++;
				e.printStackTrace();
			}
		}
		
		// if unsuccessfully closed
		if (timeouts == FINACK_TIMEOUTS) {
			// send a RESET packet
			sendResetPacket();
		} else if (SRMP_receivePacket.header.getSeqno() == this.seqno+1) {
			// process ACK
			this.seqno = SRMP_receivePacket.header.getSeqno();
			this.window = SRMP_receivePacket.header.getWindow();
			return 1;
		} else {
			System.err.println("FINACK with incorrect seqno received");
			return 0;
		}
		
		return 0;
	}
	
	/*
	public boolean inWindow(SRMP_Packet p, int window)
	{
		int ackWindow = p.getWindow();
		int lowCap, highCap;
		boolean wraparound = false;
		if (ackSeqno - window > 0)
		{ // no wraparound
			lowCap = ackSeqno - ackWindow;
			highCap = ackSeqno;
		} else { // wraparound
			lowCap = SEQUENCE_NUMBER_MAX + (ackSeqno - ackWindow);
			highCap = ackSeqno;
			wraparound = true;
		}
	}
	*/
	
	public void sendResetPacket()
	{
		SRMP_Packet srmp_resetPacket = new SRMP_Packet();
		srmp_resetPacket.header.setType(4);
		try {
			srmp_send_through_socket(srmp_resetPacket, false);
		} catch (IOException e) {
			// do nothing on error
		}
	}
	
	public static boolean[] convertByteToBoolArray(byte b) {
        boolean[] bits = new boolean[8];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = ((b & (1 << i)) != 0);
        }
        return bits;
    }
	
	public static void printByteArray (byte[] array)
	{
		for (byte b : array) {
			for (boolean bool : convertByteToBoolArray(b))
				System.out.print(bool ? "1" : "0");
			System.out.println();
		}
	}
}
