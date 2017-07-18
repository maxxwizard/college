import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class graphicsApp extends JFrame
{
 XCanvas theCanvas;
 int width = 800, height = 600;

 public static void main(String[] args)
 {
  new graphicsApp();
 }

 graphicsApp()
 {
  super("Java Graphics 101");

  addWindowListener( new WindowAdapter()
  {
   public void windowClosing(WindowEvent e)
   {
    System.exit(0);
   }
  } );

  setSize(width, height);
  theCanvas = new XCanvas();
  add(theCanvas, "Center");

  setVisible(true);
 }

 class XCanvas extends Canvas
 {
  XCanvas()
  {
  }

  public void update(Graphics g)
  {
   paint(g);
  }

  public void paint(Graphics g)
  {
   clearTheCanvas(g);

   //drawSomeText(g);
   //drawSomeGraphics(g);
   
   drawCheckerboard(g);
   // draw some red and black checkers on the white squares
   g.setColor( Color.red );
   g.fillOval(55, 55, 40, 40);
   g.setColor( Color.black );
   g.fillOval(155, 55, 40, 40);
  }

  public void clearTheCanvas(Graphics g)
  {
   g.setColor(Color.white);

   g.fillRect(0, 0, width, height);
  }

  public void drawSomeText (Graphics g)
  {
   Font f = new Font("TimesRoman",Font.BOLD,18);
   g.setFont(f);

   g.setColor(Color.blue);

   g.drawString("Black side", 50, 50 );
   g.drawString("Red side", 50, 500 );
  }
  
  public void drawCheckerboard(Graphics g)
  {
    g.setColor (Color.black);
    
    // draw lines
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
  }
  
  public void drawCheckerAt(String s)
  {
    int x = decodeLetter(s.charAt(0));
    int y = Character.getNumericValue(s.charAt(1));
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

  public void drawSomeGraphics( Graphics g )
  {
   
   g.setColor( Color.red );
   // draw a 50px red circle at (50, 70)
   g.fillOval(50, 70, 50, 50);

   g.setColor( Color.black );
   // draw a black oval at (150, 70)
   g.fillOval(150, 70, 50, 50);

   // draw the lines for the rectangle
   g.drawLine ( 50, 150, 110, 150 );
   g.drawLine ( 110, 150, 110, 210 );
   g.drawLine ( 110, 210, 50, 210 );
   g.drawLine ( 50, 210, 50, 150 );

   g.drawLine ( 110, 150, 170, 150 );
   g.drawLine ( 170, 150, 170, 210 );
   g.drawLine ( 170, 210, 110, 210 );
   g.drawLine ( 110, 210, 110, 150 );
   // fill in the right half of the rectangle
   g.fillRect(110, 150, 60, 60);
  }
 }
}
