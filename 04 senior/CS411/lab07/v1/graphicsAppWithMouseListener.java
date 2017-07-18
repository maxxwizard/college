import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;

public class graphicsAppWithMouseListener extends JFrame
{
 XCanvas theCanvas;
 int width = 800, height = 600;

 public static void main(String[] args)
 {
  javax.swing.SwingUtilities.invokeLater(new Runnable()
  {
   public void run()
   {
    new graphicsAppWithMouseListener();
   }
  });
 }

 graphicsAppWithMouseListener()
 {
  super("Java Graphics App with Mouse Listener");

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
  String mostRecentMouseEvent = "";
  int checkerX = 50;
  int checkerY = 50;
  int checkerDiameter = 40;
  boolean checkerLocked = false;

  XCanvas()
  {
   addMouseListener( new MouseAdapter()
   {
    public void mousePressed(MouseEvent mouseEvent)
    {
     int mouseX = mouseEvent.getX();
     int mouseY = mouseEvent.getY();

     mostRecentMouseEvent = String.format(
      "Mouse button was pressed at %d, %d", mouseX, mouseY );

     System.out.println(mostRecentMouseEvent);
     
     if (checkerLocked)
     {
       checkerLocked = false;
       System.out.println("checker is now unlocked");
     } else if ((mouseX > checkerX && mouseX < (checkerX+checkerDiameter)) && (mouseY > checkerY && mouseY < (checkerY+checkerDiameter)))
     {
       System.out.println("checker is now locked");
       checkerLocked = true;
     }

     repaint();
    }

    public void mouseReleased(MouseEvent mouseEvent)
    {
     int mouseX = mouseEvent.getX();
     int mouseY = mouseEvent.getY();

     mostRecentMouseEvent = String.format(
      "Mouse button was released at %d, %d", mouseX, mouseY );

     System.out.println(mostRecentMouseEvent);

     repaint();
    }
   } );

   addMouseMotionListener( new MouseMotionAdapter()
   {
    public void mouseMoved(MouseEvent mouseEvent)
    {
     int mouseX = mouseEvent.getX();
     int mouseY = mouseEvent.getY();

     mostRecentMouseEvent = String.format(
      "Mouse was moved to %d, %d", mouseX, mouseY );

     System.out.println(mostRecentMouseEvent);
     
     if (checkerLocked)
     {
       checkerX = mouseX - (checkerDiameter/2);
       checkerY = mouseY - (checkerDiameter/2);
     }

     repaint();
    }

    public void mouseDragged(MouseEvent mouseEvent)
    {
     int mouseX = mouseEvent.getX();
     int mouseY = mouseEvent.getY();

     mostRecentMouseEvent = String.format(
      "Mouse was dragged (moved with button down) to %d, %d", mouseX, mouseY );

     System.out.println(mostRecentMouseEvent);

     repaint();
    }
   } );
  }

  public void update(Graphics g)
  {
   paint(g);
  }

  public void paint(Graphics g)
  {
   clearDrawingArea(g);
   drawChecker(g, checkerX, checkerY);
   drawMouseInfo(g);
  }

  public void clearDrawingArea(Graphics g)
  {
   g.setColor(Color.white);

   g.fillRect(0, 0, width, height);
  }

  public void drawMouseInfo(Graphics g)
  {
   Font f = new Font("TimesRoman",Font.BOLD,18);
   g.setFont(f);

   g.setColor(Color.blue);

   g.drawString("Most recent mouse event: " + mostRecentMouseEvent, 50, 525);
  }

  public void drawChecker(Graphics g, int x, int y)
  {
    // draw a red checker
   g.setColor( Color.red );
   g.fillOval(x, y, checkerDiameter, checkerDiameter);
  }
 }
}
