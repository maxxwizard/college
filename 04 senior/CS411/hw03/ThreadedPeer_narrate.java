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

public class ThreadedPeer_narrate
{
 private static String IP = "127.0.0.1";
 private static int PORT = 1234;
 private static Scanner keyboard = null;

 public static void main(String args[])
 {
  Narrator.narrate("@0: the code is about to read a line from the keyboard");

  keyboard = new Scanner(System.in);

  System.out.print("Enter an IP address to connect to, or <cr> to wait for a connection: ");

  String text = keyboard.nextLine();

  if ( text.length() == 0 ) // peer type [w]
  {
   Narrator.narrate("@1: the user has typed in an empty line");

   Narrator.narrate("@2: a new thread is started, waiting on that specific port and keyboard");

   WaitingThread thread = new WaitingThread("WaitingThread", PORT, keyboard);
  }
  else // peer type [c]
  {
   Narrator.narrate("@3: the user typed in an IP address");

   Narrator.narrate("@4: a new thread is started, waiting on the System.in from a specific port");

   IP = text;

   ConnectingThread thread = new ConnectingThread("ConnectingThread", IP, PORT, keyboard);
  }

  Narrator.narrate("@5: the main loop has ended and there should be either a WaitingThread or ConnectingThread in existence");
 }
}

class Narrator
{
 private static boolean doNarration = true;

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
  Narrator.narrate("@6: new instance of WaitingThread instantiating");

  PORT = port;
  this.keyboard = keyboard;

  runner = new Thread(this, threadName);
  runner.start();
 }

 public void run()
 {
  Narrator.narrate("@7: the WaitingThread has been started");

  ServerSocket listener = null;

  try
  {
   Narrator.narrate("@8: the thread starts listening on a socket");

   listener = new ServerSocket(PORT);

   Narrator.narrate("@9: the external IP address of thread's host machine is found");

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
   Narrator.narrate("@10: the WaitingThread is listening on the socket...");

   inSocket = listener.accept();

   Narrator.narrate("@11: a packet has been received");

   Narrator.narrate("@12: the socket's input and output streams are encapsulated as Java constructs");

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
     Narrator.narrate("@13: the program waits for the next string line from the socket");

     if (in.hasNextLine())
     {
      Narrator.narrate("@14: the program gets the next string line from the socket");

      input = in.nextLine();

      if (input.compareTo("quit") == 0)
       done = true;
      else
      {
       System.out.println(
        "[c] said: " + input);

       Narrator.narrate("@15: the system prompts the server for a line of text");

       System.out.print("[w]> ");

       input = keyboard.nextLine();

       Narrator.narrate("@16: a line has been read from standard input");

       if (input.compareTo("quit") == 0)
        done = true;

       Narrator.narrate("@17: the line that's been read in is sent into the socket");

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

    Narrator.narrate("@18: something illegal happened so the server is sending the quit command to the client");

    out.println("quit");
    out.flush();
   }
  }

  Narrator.narrate("@19: the program is ending because the 'done' variable has been set to true");

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
  Narrator.narrate("@20: the main thread has already ended. if it closed standard input, the socket encapsulation of the keyboard used by the ongoing sub-thread would be broken.");

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
  Narrator.narrate("@21: ConnectingThread is being instantiated");

  IP = waitingIP;
  PORT = waitingPort;
  this.keyboard = keyboard;

  runner = new Thread(this, threadName);
  runner.start();
 }

 public void run()
 {
  Narrator.narrate("@22: ConnectingThread is being started");

  Scanner in = null;
  PrintWriter out = null;
  Socket socket = null;

  try
  {
   System.out.println("Connecting to " + IP + ":" + PORT + "...");

   Narrator.narrate("@23: a socket is made connecting to the specified IP and PORT");

   socket = new Socket(IP, PORT);

   Narrator.narrate("@24: the socket's input and output streams are encapsulated as Java constructs");

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
     Narrator.narrate("@25: waiting for user input");

     System.out.print("[c]> ");

     String input = keyboard.nextLine();

     Narrator.narrate("@26: user input received");

     Narrator.narrate("@27: send the user input over the socket");

     out.println(input);
     out.flush();

     if (input.compareTo("quit") == 0)
      done = true;

     if (in.hasNextLine())
     {
      input = in.nextLine();

      Narrator.narrate("@28: received a message");

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

    Narrator.narrate("@29: something bad happened, client sends quit command to server");

    out.println("quit");
    out.flush();
   }
  }

  Narrator.narrate("@30: the program is ending because either server or client said quit");

  System.out.println("Exiting... Bye.");

  try
  {
   socket.close();
  }
  catch (Exception e)
  {
  }

  // Part 2 of Bonus:
  Narrator.narrate("@31: the main thread has already ended. if it closed standard input, the socket encapsulation of the keyboard used by the ongoing sub-thread would be broken.");

  keyboard.close();
 }
}