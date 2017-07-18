/*
 * Simple demonstration of TCP/IP sockets in Java
 * Derived from Client / Server written by Alexander J Skrabut (uruzrune@gmail.com)
 *
 * This may be used and modified freely in J. Keklak's CS411 class at
 * Boston University. For any other use, please obtain permission first.
 *
 * Limitations:
 *   Blocks indefinitely on any Scanner read, including keyboard.
 *   Blocks indefinitely on listener.accept() statement.
 *   Does not detect abrupt client disconnect (not 'quit')
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.IllegalStateException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class Peer
{
 private static String IP = "127.0.0.1";
 private static int PORT = 1234;

 public static void main(String args[])
 {
  Scanner keyboard = new Scanner(System.in);

  System.out.print("Enter an IP address to connect to, or <cr> to wait for a connection: ");

  String text = keyboard.nextLine();

  if ( text.length() == 0 ) // peer type [w]
  {
   ServerSocket listener = null;

   try
   {
    listener = new ServerSocket(PORT);
    IP = java.net.InetAddress.getLocalHost().getHostAddress();
   }
   catch (IOException ioe)
   {
    ioe.printStackTrace();
    System.exit(1);
   }

   System.out.println("Waiting on port " + PORT + " at IP address " + IP + "...");


   Socket inSocket = null;
   Scanner in = null;
   PrintWriter out = null;

   try
   {
    inSocket = listener.accept();

    in = new Scanner(inSocket.getInputStream());
    out = new PrintWriter(inSocket.getOutputStream());

    System.out.println("Connection from "
     + inSocket.getInetAddress().toString());

    System.out.println("Type 'quit' to quit.");
   }
   catch (IOException ioe)
   {
    ioe.printStackTrace();
    System.exit(1);
   }

   boolean done = false;

   while (!done)
   {
    String input = "";

    try
    {
     if (inSocket.isClosed())
      done = true;
     else
     {
      if (in.hasNextLine())
      {
       input = in.nextLine();

       if (input.compareTo("quit") == 0)
        done = true;
       else
       {
        System.out.println(
         "[c] said: " + input);

        System.out.print("[w]> ");

        input = keyboard.nextLine();

        if (input.compareTo("quit") == 0)
         done = true;

        out.println(input);
        out.flush();
       }
      }
     }
    }
    catch (IllegalStateException ise)
    {
     done = true;
    }
   }

   System.out.println("Exiting... Bye.");

   try
   {
    inSocket.close();
    listener.close();
   }
   catch (Exception e)
   {
    ;
   }
  }
  else //  peer type [c]
  {
   Scanner in = null;
   PrintWriter out = null;
   Socket socket = null;

   IP = text;

   try
   {
    System.out.println("Connecting to " + IP + ":" + PORT + "...");
    socket = new Socket(IP, PORT);

    in = new Scanner(socket.getInputStream());
    out = new PrintWriter(socket.getOutputStream());
   }
   catch (IOException ioe)
   {
    ioe.printStackTrace();
    System.exit(1);
   }

   System.out.println("Connected! Type 'quit' to quit.");

   boolean done = false;

   while (!done)
   {
    try
    {
     if (socket.isClosed())
      done = true;
     else
     {
      System.out.print("[c]> ");

      String input = keyboard.nextLine();

      out.println(input);
      out.flush();

      if (input.compareTo("quit") == 0)
       done = true;

      if (in.hasNextLine())
      {
       input = in.nextLine();
       System.out.println("[w] said: " + input);

       if (input.compareTo("quit") == 0)
        done = true;
      }
     }
    }
    catch (IllegalStateException ise)
    {
     done = true;
    }
   }

   System.out.println("Exiting... Bye.");

   try
   {
    socket.close();
   }
   catch (Exception e)
   {
    ;
   }
  }

  keyboard.close();
 }
}