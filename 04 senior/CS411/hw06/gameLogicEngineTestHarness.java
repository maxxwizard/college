public class gameLogicEngineTestHarness
{
 public static void main(String[] args)
 {
  CheckersGameLogic engine = new CheckersGameLogic();
  String command = "";
  String response = "";

  //engine.StringTokenizer101(); // a quick demo of StringTokenizer

  // run tests on the game engine

  /////////////////////////////////////////////

  // Test #1: clear the board

  command = "clear";

  response = engine.doOperation(command);

  // is response correct?

  if ( response.equals("ok") )
  {
   System.out.println("Test #1 passed.");
  }
  else
  {
   System.out.println("Test #1 failed.");
  }

  /////////////////////////////////////////////

  // Test #2: check state of the board

  // Start with a cleared board. Do not assume any state from the previous test.
  engine = new CheckersGameLogic();
  
  // Get the state of the board.
  command = "getboardstate";
  response = engine.doOperation(command);
  
  // Is the response correct for this command?
  if (response.equals("red  black "))
    System.out.println("Test #2 passed.");
  else
    System.out.println("Test #2 failed.");

  /////////////////////////////////////////////

  // Test #3: place a checker on the board

  // Start with a cleared board.
  engine = new CheckersGameLogic();

  // Place a checker on the board.
  command = "place red b2";
  response = engine.doOperation(command);

  // Is the response correct for this command?
  if (response.equals("ok"))
    System.out.println("Test #3 passed.");
  else
    System.out.println("Test #3 failed.");

  /////////////////////////////////////////////

  // Test #4a: check state of the board

  // Start with a cleared board.
  engine = new CheckersGameLogic();

  // Get the state of the board.
  command = "getboardstate";
  response = engine.doOperation(command);

  // Is the response correct for this command?
  if (response.equals("red  black "))
    System.out.println("Test #4a passed.");
  else
    System.out.println("Test #4a failed.");

  /////////////////////////////////////////////

  // Test #4b: check state of the board

  // Start with a cleared board.
  engine = new CheckersGameLogic();

  // Place a checker on the board.
  command = "place red b2";
  response = engine.doOperation(command);

  // Get the state of the board.
  command = "getboardstate";
  response = engine.doOperation(command);

  // Is the response correct for this command?
  if (response.equals("red b2 black "))
    System.out.println("Test #4b passed.");
  else
    System.out.println("Test #4b failed.");

  /////////////////////////////////////////////

  // Test #5: set up board to start a game
  // Assumptions:
  // - From bottom to top, rows increase from 1 to 8.
  // - From left to right, columns increase from a to h.

  // Start with a cleared board.
  engine = new CheckersGameLogic();
  
  // Set up the board.
  command = "setupboard";
  engine.doOperation(command);
  
  // Get the state of the board.
  command = "getboardstate";
  response = engine.doOperation(command);
  
  // Is the response correct for this command?
  if (response.equals("red b8 d8 f8 h8 a7 c7 e7 g7 a6 c6 e6 g6 black a3 c3 e3 g3 b2 d2 f2 h2 a1 c1 e1 g1"))
    System.out.println("Test #5 passed.");
  else
    System.out.println("Test #5 failed.");

  /////////////////////////////////////////////

  // Test #6: test the "move" command
  
  // Start with a setup board.
  engine = new CheckersGameLogic();
  command = "setupboard";
  response = engine.doOperation(command);
  
  // Move a red and a black piece.
  command = "move a6 b5";
  engine.doOperation(command);
  command = "move a3 b4";
  engine.doOperation(command);
  
  // Get the state of the board.
  command = "getboardstate";
  response = engine.doOperation(command);
  
  // Is the game state consistent with the move?
  if (response.equals("red b8 d8 f8 h8 a7 c7 e7 g7 b5 c6 e6 g6 black b4 c3 e3 g3 b2 d2 f2 h2 a1 c1 e1 g1"))
  {
    System.out.println("Test #6 passed.");
  } else {
    System.out.println("Test #6 failed.");
  }
 }

}
