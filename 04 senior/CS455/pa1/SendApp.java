import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SendApp {
	
	final static int SUCCESS = 1;
	final static int FAILURE = 0;
	final static int MAX_PACKET_DATA_SIZE = 994; // 1000 byte packets (6-byte headers)

	/**
	 * @param args
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
		String hostName = "", fileName = "";
		int destPort = 0, srcPort = 0;
		
		if (args.length != 4)
		{
			System.err.println("usage: ./SendApp <dest hostname> <dest port> <src port> <input fileName>");
			System.exit(1);
		} else {
			hostName = args[0];
			destPort = Integer.parseInt(args[1]);
			srcPort = Integer.parseInt(args[2]);
			fileName = args[3];
		}
		
		// open a SRMP socket
		SRMP_Send_Socket srmp_socket = new SRMP_Send_Socket();
		int socketOpen = srmp_socket.srmp_open(hostName, destPort, srcPort);
		// if socket was successfully opened
		if (socketOpen == SUCCESS)
		{
			int currentPos = 0;
			// start sending the file
			byte[] fileBytes = getBytesFromFile(fileName);
			System.out.println("bytes to transfer: " + fileBytes.length + "\n");
			while (currentPos <= fileBytes.length-1)
			{
				// grab another min(MAX_PACKET_DATA_SIZE, fileBytes.length-currentPos)-sized chunk of the file
				int nextPacketSize = Math.min(MAX_PACKET_DATA_SIZE, (fileBytes.length)-currentPos);
				if (nextPacketSize == 0)
					break;
				
				// send it over the socket
				srmp_socket.srmp_send(Arrays.copyOfRange(fileBytes, currentPos, currentPos+nextPacketSize), nextPacketSize);
				
				// move currentPos forward by packet size
				currentPos += nextPacketSize;
			}
			System.out.println("all file chunks sent to socket buffer");
			// verify all packets have been sent and ACKed, send FIN packet, close the socket
			if (srmp_socket.srmp_close() == SUCCESS)
				System.out.println("socket successfully closed");
			else
			{
				System.err.println("socket close failed!");
				System.exit(1);
			}
		} else {
			System.err.println("srmp socket creation failed!");
			System.exit(1);
		}
	}
	
	/*
	 * modified from: http://www.java-tips.org/java-se-tips/java.io/reading-a-file-into-a-byte-array.html
	 */
	public static byte[] getBytesFromFile(String pathname) throws IOException
	{
		File file = new File(pathname);
		System.out.println("read in file '" + file.getCanonicalPath() + "'");
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

}
