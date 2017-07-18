import java.util.StringTokenizer;

class CheckersGameLogic
{
 String redCheckers = ""; // this string contains all of the red checkers
 String blackCheckers = ""; // this string contains all of the black checkers

 public void StringTokenizer101()
 {
  String sentence = "I really enjoy CS411.";
  StringTokenizer st = new StringTokenizer(sentence);

  while (st.hasMoreTokens())
  {
   System.out.println(st.nextToken());
  }
 }

 public String doOperation (String command)
 {
  String response = "";
  StringTokenizer st = new StringTokenizer(command);
  String operation = "";

  if (st.hasMoreTokens()) // get operation token
  {
   operation = st.nextToken();
  }

  if ( operation.equals("clear") )
  {
   // clear the board
    redCheckers = "";
    blackCheckers = "";

   response = "ok";
  }
  else if ( operation.equals("getboardstate") )
  {
    response = "red " + redCheckers + " black " + blackCheckers;
  } else if (operation.equals("place"))
  {
    String color = st.nextToken();
    if (!(color.toLowerCase().equals("red") || color.toLowerCase().equals("black")))
      response = "invalid";
    
    String location = st.nextToken().toLowerCase();
    String letter = location.substring(0, 1);
    String number = location.substring(1, 2);
    String validLetters = "abcdefgh";
    if (validLetters.indexOf(letter) < 0)
      response = "invalid";
    if (!(Integer.parseInt(number) >= 1 && Integer.parseInt(number) <= 8))
      response = "invalid";
    
    if (!response.equals("invalid"))
    {
      response = "ok";
      if (color.equals("red"))
      {
        if (redCheckers != "")
          redCheckers += " ";
        redCheckers += location;
        //System.out.println("red " + redCheckers);
      }
      else if (color.equals("black"))
      {
        if (blackCheckers != "")
          blackCheckers += " ";
        blackCheckers += location;
        //System.out.println("black " + blackCheckers);
      }
    }
    
  }
  else if (operation.equals("setupboard"))
  {
    redCheckers = "b8 d8 f8 h8 a7 c7 e7 g7 a6 c6 e6 g6";
    blackCheckers = "a3 c3 e3 g3 b2 d2 f2 h2 a1 c1 e1 g1";
    response = "ok";
  } else if (operation.equals("move"))
  {
    String from = st.nextToken();
    String to = st.nextToken();
    
    // move the actual game piece
    if (redCheckers.contains(from))
    {
      redCheckers = redCheckers.replace(from, to);
    } else {
      blackCheckers = blackCheckers.replace(from, to);
    }
    
    response = "ok";
  }

  return response;
 }
}