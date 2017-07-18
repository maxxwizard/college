/*
 * Matthew Huynh
 * CS455 - Byers
 * HW2
 * September 28, 2010
 * 
 * sent: "U85462311"
 * received: "Confirming message from /155.41.13.56 on port 62469.  Responding with checksum message 2968."
 */

import java.io.*;
import java.net.*;

class UDPClient {
  public static void main(String args[]) throws Exception
  {
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientSocket = new DatagramSocket();
    InetAddress IPAddress = InetAddress.getByName("cs10-dhcp160.bu.edu");
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String sentence = inFromUser.readLine();
    sendData = sentence.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8997);
    clientSocket.send(sendPacket);
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientSocket.receive(receivePacket);
    String response = new String(receivePacket.getData());
    System.out.println("Response from server: " + response);
    clientSocket.close();
  }
}