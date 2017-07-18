import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.util.StringTokenizer;

class Renderer
{
 public CheckersCanvas theCanvas;
 private CheckersGameLogic theLogicEngine;
 public String playerColorText = "Red";

 // use data members to avoid 'magic numbers'

 private final Point boardUL = new Point(50,125);
 private final int squareSize = 60;
 private final int checkerSize = 50;

 public boolean drawResignButton = true;

 private Point resignButtonUL = new Point(430, 650);
 private Point resignButtonSize = new Point(100, 25);
 private int resignButtonXOffset = 25;

 public boolean drawYesNoButtons = false;

 private Point yesButtonUL = new Point(200, 80);
 private Point yesButtonSize = new Point(50, 25);
 private int yesButtonXOffset = 15;

 private Point noButtonUL = new Point(300, 80);
 private Point noButtonSize = new Point(50, 25);
 private int noButtonXOffset = 15;

 public String message = "";
 public boolean drawMessage = true;
 private Point messageLocation = new Point(50, 65);

 public boolean drawPlayerColorLabel = true;
 private Point playerColorLabelUL = new Point(50, 650);

 private int lowerTextYOffset = 18;

 private String activeChecker = "";
 private Point activeCheckerLocation = new Point();

 public Renderer(CheckersGameLogic logicEngine, int width, int height)
 {
  theCanvas = new CheckersCanvas(width, height);
  theLogicEngine = logicEngine;
 }

 public void requestRepaint()
 {
  theCanvas.repaint();
 }
 
 public boolean pixelInYesButton (int x, int y)
  {
    return false;
  }
  
  public boolean pixelInNoButton (int x, int y)
  {
    return false;
  }
  
  public boolean pixelInResignButton (int x, int y)
  {
    return false;
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

  Point getCenterInPixels(String token)
  {
   char chx = token.charAt(0);
   char chy = token.charAt(1);
   Point p = new Point();

   if (chx == 'a')
   {
    p.x = 0;
   }
   else if (chx == 'b')
   {
    p.x = 1;
   }
   else if (chx == 'c')
   {
    p.x = 2;
   }
   else if (chx == 'd')
   {
    p.x = 3;
   }
   else if (chx == 'e')
   {
    p.x = 4;
   }
   else if (chx == 'f')
   {
    p.x = 5;
   }
   else if (chx == 'g')
   {
    p.x = 6;
   }
   else if (chx == 'h')
   {
    p.x = 7;
   }

   if (chy == '1')
   {
    p.y = 0;
   }
   else if (chy == '2')
   {
    p.y = 1;
   }
   else if (chy == '3')
   {
    p.y = 2;
   }
   else if (chy == '4')
   {
    p.y = 3;
   }
   else if (chy == '5')
   {
    p.y = 4;
   }
   else if (chy == '6')
   {
    p.y = 5;
   }
   else if (chy == '7')
   {
    p.y = 6;
   }
   else if (chy == '8')
   {
    p.y = 7;
   }

   Point pixelPt = new Point();

   pixelPt.x = boardUL.x + p.x * squareSize + ( squareSize / 2 );
   pixelPt.y = boardUL.y + squareSize * 8 - p.y * squareSize - ( squareSize / 2 );

   return pixelPt;
  }

  public void drawCheckerboard(Graphics g)
  {
   // draw the checkerboard

   drawBoard(g);

   // draw the checkers

   String boardstate = theLogicEngine.boardstate;
    // get the board state from the logic engine

   StringTokenizer st = new StringTokenizer(boardstate);

   while (st.hasMoreTokens()) // get tokens
   {
    String token = st.nextToken();

    // process the token

    if ( token.equals("red") )
    {
     g.setColor(Color.red);
    }
    else if ( token.equals("black") )
    {
     g.setColor(Color.black);
    }
    else
    {
     Point pt = getCenterInPixels(token);

     g.fillOval(pt.x - ( checkerSize / 2 ), pt.y - ( checkerSize / 2 ),
      checkerSize, checkerSize);
    }
   }
  }

  public void drawUIElements(Graphics g)
  {
   // draw the labels and buttons

   Font f = new Font("TimesRoman",Font.BOLD,18);
   g.setFont(f);

   g.setColor(Color.black);

   if ( drawMessage )
   {
    g.drawString(message, messageLocation.x, messageLocation.y);
   }

   if ( drawResignButton )
   {
    g.drawRect(resignButtonUL.x, resignButtonUL.y,
     resignButtonSize.x, resignButtonSize.y );

    g.drawString("Resign", resignButtonUL.x + resignButtonXOffset,
     resignButtonUL.y + lowerTextYOffset );
   }

   if ( drawYesNoButtons )
   {
    g.drawRect(yesButtonUL.x, yesButtonUL.y,
     yesButtonSize.x, yesButtonSize.y );

    g.drawString("Yes", yesButtonUL.x + yesButtonXOffset,
     yesButtonUL.y + ( 3 * yesButtonSize.y / 4 ) );

    g.drawRect(noButtonUL.x, noButtonUL.y,
     noButtonSize.x, noButtonSize.y );

    g.drawString("No", noButtonUL.x + noButtonXOffset,
     noButtonUL.y + ( 3 * noButtonSize.y / 4 ) );
   }

   if ( drawPlayerColorLabel )
   {
    String label = "Player: " + playerColorText;

    g.drawString(label, playerColorLabelUL.x,
     playerColorLabelUL.y + lowerTextYOffset );
   }
  }

  public void drawBoard( Graphics g )
  {
   for ( int row = 0; row < 8; row++ )
   {
    Color squareColor = Color.white;

    if ( row % 2 == 0 )
    {
     squareColor = Color.black;
    }

    for ( int col = 0; col < 8; col++ )
    {
     int urx, ury, llx, lly;

     urx = boardUL.x + col * squareSize;
     ury = boardUL.y + row * squareSize;
     llx = urx + squareSize;
     lly = ury + squareSize;

     g.setColor(squareColor);

     g.fillRect(urx, ury, squareSize, squareSize);

     squareColor = squareColor == Color.black ? Color.white : Color.black;

     g.setColor(Color.black);

     g.drawLine ( urx, ury, llx, ury );
     g.drawLine ( llx, ury, llx, lly );
     g.drawLine ( llx, lly, urx, lly );
     g.drawLine ( urx, lly, urx, ury );
    }
   }
  }
 }
}