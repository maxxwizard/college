import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.util.StringTokenizer;

class Renderer
{
 public CheckersCanvas theCanvas;
 private CheckersGameLogic theLogicEngine;
 public boolean drawResignButton = false;
 public boolean drawYesNoButtons = false;
 public boolean drawPlayerColorLabel = false;
 public String playerColorText = "Red";
 public String message = "";

 public Renderer(CheckersGameLogic logicEngine, int width, int height)
 {
  theCanvas = new CheckersCanvas(width, height);
  theLogicEngine = logicEngine;
 }

 public void requestRepaint()
 {
  theCanvas.repaint();
 }

 class CheckersCanvas extends Canvas
 {
  int width, height;

  CheckersCanvas(int width, int height)
  {
   this.width = width;
   this.height = height;
  }

  public void update(Graphics g)
  {
   paint(g);
  }

  public void paint(Graphics g)
  {
   clearDrawingArea(g);
   drawCheckerboard(g);
   drawUIElements(g);
  }

  public void clearDrawingArea(Graphics g)
  {
   g.setColor(Color.white);
   g.fillRect(0, 0, width, height);
  }

  public void drawCheckerboard(Graphics g)
  {
    // draw the checkerboard

    g.setColor (Color.gray);
    
    // lines
    g.drawLine ( 50, 50, 450, 50 ); // top line
    g.drawLine ( 450, 50, 450, 450 ); // right line
    g.drawLine ( 450, 450, 50, 450 ); // bottom line
    g.drawLine ( 50, 450, 50, 50 ); // left line
    
    // fill in rectangles
    for (int i = 1; i <= 8; i++)
    {
      for (int j = 1; j <= 8; j++)
      {
        // fill in the black squares
        int offset = j % 2;
        if (i % 2 != offset)
        {
          g.fillRect (i*50, j*50, 50, 50);
        }
      }
    }

   // draw the checkers

   String boardstate = theLogicEngine.boardstate;
    // get the board state from the logic engine

   StringTokenizer st = new StringTokenizer(boardstate);

   if (st.hasMoreTokens()) // get tokens
   {
    String token = st.nextToken();

    // process the token

    if (token.equals("red"))
    {
      // draw the red pieces
      token = st.nextToken();
      while (st.hasMoreTokens() && !token.equals("black"))
      {
        drawCheckerAt(g, token, Color.red);
        token = st.nextToken();
      }
      //System.out.println("entering black while loop");
      // draw the black pieces
      while (st.hasMoreTokens())
      {
        token = st.nextToken();
        drawCheckerAt(g, token, Color.black);
      } 
    }
   }

   // draw the labels and buttons
   g.setColor(Color.black);
   g.drawString(message, 50, 25);

  }
  
  public void drawCheckerAt(Graphics g, String s, Color c)
  {
    int x = decodeLetter(s.charAt(0));
    int y = Character.getNumericValue(s.charAt(1));
    g.setColor(c);
    g.fillOval(50*x+5, 50*y+5, 40, 40);
  }
  
  public int decodeLetter(char c)
  {
    switch (c)
    {
      case 'a':
        return 1;
      case 'b':
        return 2;
      case 'c':
        return 3;
      case 'd':
        return 4;
      case 'e':
        return 5;
      case 'f':
        return 6;
      case 'g':
        return 7;
      case 'h':
        return 8;
      default:
        return -1;
    }
  }

  public void drawUIElements(Graphics g)
  {
    g.setColor(Color.black);
    
    if (drawResignButton)
    {
      g.drawRect(400, 500, 50, 20);
      g.drawString("Resign", 405, 515);
    }
    
    if (drawYesNoButtons)
    {
      g.drawRect(380, 10, 30, 20);
      g.drawString("Yes", 385, 25);
      g.drawRect(420, 10, 30, 20);
      g.drawString("No", 425, 25);
    }
    
    if (drawPlayerColorLabel)
    {
      g.drawString("Player: " + playerColorText, 50, 515);
    }
  }
 }
}