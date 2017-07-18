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

public class ThreadedPeer
{
 private static String IP = "127.0.0.1";
 private static int PORT = 1234;
 private static Scanner keyboard = null;

 public static void main(String args[])
 {
  Narrator.narrate("@0: <explain what the next three lines do>");

  keyboard = new Scanner(System.in);

  System.out.print("Enter an IP address to connect to, or <cr> to wait for a connection: ");

  String text = keyboard.nextLine();

  if ( text.length() == 0 ) // peer type [w]
  {
   Narrator.narrate("@1: <explain why the flow of control gets here>");

   Narrator.narrate("@2: <explain what the next line does>");

   WaitingThread thread = new WaitingThread("WaitingThread", PORT, keyboard);
  }
  else // peer type [c]
  {
   Narrator.narrate("@3: <explain why the flow of control gets here>");

   Narrator.narrate("@4: <explain what the next two lines do>");

   IP = text;

   ConnectingThread thread = new ConnectingThread("ConnectingThread", IP, PORT, keyboard);
  }

  Narrator.narrate("@5: <explain why the flow of control gets here when it does>");
 }
}

class Narrator
{
 private static boolean doNarration = false;

 public static void narrate(String statement)
 {
  if ( doNarration )
  {
   System.out.println(statement);
  }
 }
}

class WaitingThread implements Runnable
{
 private static String IP = "127.0.0.1";
 private int PORT = 0;
 Scanner keyboard = null;

 Thread runner;

 public WaitingThread(String threadName, int port, Scanner keyboard)
 {
  Narrator.narrate("@6: <explain why the flow of control gets here>");

  PORT = port;
  this.keyboard = keyboard;

  runner = new Thread(this, threadName);
  runner.start();
 }

 public void run()
 {
  Narrator.narrate("@7: <explain why the flow of control gets here>");

  ServerSocket listener = null;

  try
  {
   Narrator.narrate("@8: <explain what the next line does>");

   listener = new ServerSocket(PORT);

   Narrator.narrate("@9: <explain what the next line does>");

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
   Narrator.narrate("@10: <explain what the next line does>");

   inSocket = listener.accept();

   Narrator.narrate("@11: <explain how the flow of control gets here>");

   Narrator.narrate("@12: <explain what the next two lines do>");

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
     Narrator.narrate("@13: <explain what the next line does>");

     if (in.hasNextLine())
     {
      Narrator.narrate("@14: <explain what the next line does>");

      input = in.nextLine();

      if (input.compareTo("quit") == 0)
       done = true;
      else
      {
       System.out.println(
        "[c] said: " + input);

       Narrator.narrate("@15: <explain what the next two lines do>");

       System.out.print("[w]> ");

       input = keyboard.nextLine();

       Narrator.narrate("@16: <explain how the flow of control gets here>");

       if (input.compareTo("quit") == 0)
        done = true;

       Narrator.narrate("@17: <explain what the next two lines do>");

       out.println(input);
       out.flush();
      }
     }
    }
   }
   catch (IllegalStateException ise)
   {
    System.out.println("Exception: " + ise.toString());

    done = true;

    Narrator.narrate("@18: <explain what the next two lines do>");

    out.println("quit");
    out.flush();
   }
  }

  Narrator.narrate("@19: <explain how the flow of control gets here>");

  System.out.println("Exiting... Bye.");

  try
  {
   inSocket.close();
   listener.close();
  }
  catch (Exception e)
  {
  }

  // Part 1 of Bonus:
  Narrator.narrate("@20: <explain why this code is here rather than at the end of the main procedure>");

  keyboard.close();
 }
}

class ConnectingThread implements Runnable
{
 private String IP = "127.0.0.1";
 private int PORT = 1234;

 Scanner keyboard = null;

 Thread runner;

 public ConnectingThread(String threadName, String waitingIP, int waitingPort, Scanner keyboard)
 {
  Narrator.narrate("@21: <explain why the flow of control gets here>");

  IP = waitingIP;
  PORT = waitingPort;
  this.keyboard = keyboard;

  runner = new Thread(this, threadName);
  runner.start();
 }

 public void run()
 {
  Narrator.narrate("@22: <explain why the flow of control gets here>");

  Scanner in = null;
  PrintWriter out = null;
  Socket socket = null;

  try
  {
   System.out.println("Connecting to " + IP + ":" + PORT + "...");

   Narrator.narrate("@23: <explain what the next line does>");

   socket = new Socket(IP, PORT);

   Narrator.narrate("@24: <explain what the next two lines do>");

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
     Narrator.narrate("@25: <explain what the next two lines do>");

     System.out.print("[c]> ");

     String input = keyboard.nextLine();

     Narrator.narrate("@26: <explain how the flow of control gets here>");

     Narrator.narrate("@27: <explain what the next two lines do>");

     out.println(input);
     out.flush();

     if (input.compareTo("quit") == 0)
      done = true;

     if (in.hasNextLine())
     {
      input = in.nextLine();

      Narrator.narrate("@28: <explain how the flow of control gets here>");

      System.out.println("[w] said: " + input);

      if (input.compareTo("quit") == 0)
       done = true;
     }
    }
   }
   catch (IllegalStateException ise)
   {
    System.out.println("Exception: " + ise.toString());

    done = true;

    Narrator.narrate("@29: <explain what the next two lines do>");

    out.println("quit");
    out.flush();
   }
  }

  Narrator.narrate("@30: <explain how the flow of control gets here>");

  System.out.println("Exiting... Bye.");

  try
  {
   socket.close();
  }
  catch (Exception e)
  {
  }

  // Part 2 of Bonus:
  Narrator.narrate("@31: <explain why this code is here rather than at the end of the main procedure>");

  keyboard.close();
 }
}