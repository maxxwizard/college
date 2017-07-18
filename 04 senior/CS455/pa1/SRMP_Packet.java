import java.util.Date;

public class SRMP_Packet {
	
	SRMP_Header header;
	SRMP_Payload payload;
	Date timestamp;
	boolean ACKed;
	boolean inFlight; // marks the packet as having being in-flight
	
	public SRMP_Packet()
	{
		this.header = new SRMP_Header();
		this.payload = new SRMP_Payload();
		ACKed = false;
		inFlight = false;
	}
	
	public SRMP_Packet(byte[] pkt)
	{
		byte[] tmpHeader = new byte[6];
		System.arraycopy(pkt, 0, tmpHeader, 0, 6);
		this.header = new SRMP_Header(tmpHeader);
		if (pkt.length > 6)
		{
			byte[] tmpPayload = new byte[pkt.length-6];
			System.arraycopy(pkt, 5, tmpPayload, 5, pkt.length-6);
			this.payload = new SRMP_Payload(tmpPayload);
		} else {
			this.payload = new SRMP_Payload();
		}
		ACKed = false;
		inFlight = false;
	}
	
	public int getLength()
	{
		return this.header.getLength() + this.payload.getLength();
	}
	
	/**
	 * Returns the packet in a network-order byte array.
	 * 
	 */
	public byte[] GetNetworkReadyPacket()
	{
		byte[] nrHeader = this.header.GetNetworkReadyHeader();
		byte[] nrPayload = this.payload.GetNetworkReadyPayload();
		byte[] packet;
		if (nrPayload != null) // only attach the payload if it is a data packet
		{
			packet = new byte[nrHeader.length + nrPayload.length];
			System.arraycopy(nrHeader, 0, packet, 0, nrHeader.length);
			System.arraycopy(nrPayload, 0, packet, nrHeader.length, nrPayload.length);
		} else {
			packet = new byte[nrHeader.length];
			System.arraycopy(nrHeader, 0, packet, 0, nrHeader.length);
		}
		
		return packet;
	}
	
	public class SRMP_Payload {
		private byte[] data;
		
		public SRMP_Payload()
		{
			this.data = null;
		}
		
		public SRMP_Payload(byte[] data)
		{
			this.data = data;
		}
		
		public byte[] GetNetworkReadyPayload()
		{
			return data;
		}

		void setData(byte[] data) {
			this.data = data;
		}

		byte[] getData() {
			return data;
		}
		
		int getLength() {
			return data == null ? 0 : data.length;
		}
	}
	
	public class SRMP_Header {
		private byte[] type; 	/* 	 */
		private byte[] window; 	/* 	specifies the receiver's advertised
						   			window in bytes and is used only in
						   			packets of type 1 (ACK) */
		private byte[] seqno;	/* 	the sequence number of the packet */
		
		public SRMP_Header ()
		{
			this.type = new byte[2];
			this.window = new byte[2];
			this.seqno = new byte[2];
		}
		
		public SRMP_Header(byte[] header)
		{
			this.type = new byte[2];
			this.type[0] = header[0];
			this.type[1] = header[1];
			this.window = new byte[2];
			this.window[0] = header[2];
			this.window[1] = header[3];
			this.seqno = new byte[2];
			this.seqno[0] = header[4];
			this.seqno[1] = header[5];
		}
		
		public byte[] GetNetworkReadyHeader()
		{
			byte[] header = new byte [type.length + window.length + seqno.length];
			System.arraycopy(type, 0, header, 0, type.length);
			System.arraycopy(window, 0, header, type.length, window.length);
			System.arraycopy(seqno, 0, header, type.length+window.length, seqno.length);
			return header;
		}

		/**
		 * Sets the type of SRMP packet.
		 * @param type DATA = 0, ACK = 1, SYN = 2, FIN = 3, RESET = 4
		 */
		void setType(int type) {
			this.type = intToByteArray(type);
		}

		int getType() {
			return byteArrayToInt(this.type);
		}
		
		String getTypeString() {
			switch (getType())
			{
			case 0:
				return "DATA";
			case 1:
				return "ACK";
			case 2:
				return "SYN";
			case 3:
				return "FIN";
			case 4:
				return "RESET";
			default:
				return "error!";
			}
		}

		void setWindow(int window) {
			this.window = intToByteArray(window);
		}

		int getWindow() {
			return byteArrayToInt(window);
		}

		void setSeqno(int seqno) {
			this.seqno = intToByteArray(seqno);
		}

		int getSeqno() {
			return byteArrayToInt(seqno);
		}
		
		int getLength()
		{
			return (this.seqno == null && this.type == null && this.window == null) ? 0 : 6;
		}
	}
	
	public static final byte[] intToByteArray(int value) {
		return new byte[] {
                (byte)(value >>> 8),
                (byte)value};
	}
	
	public static final int byteArrayToInt(byte[] b)
	{
		return (b[0]&0xff)<<8 | (b[1]&0xff);
	}

	void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	Date getTimestamp() {
		return timestamp;
	}
}

