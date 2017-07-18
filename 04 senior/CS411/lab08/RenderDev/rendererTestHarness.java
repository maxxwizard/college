import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.util.Scanner;

public class rendererTestHarness extends JFrame
{
	int width = 800, height = 600;
	static Renderer theRenderer;
	static CheckersGameLogic theLogicEngine;

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new rendererTestHarness();
			}
		});


		// run rendering tests;
		// tests are selected by entering the test number using the keyboard

		boolean done = false;

		Scanner keyboard = new Scanner(System.in);

		while(!done)
		{
			System.out.print("Enter test number: ");

			String test = keyboard.nextLine();

			try
			{
				int testnumber = Integer.parseInt(test);

				switch ( testnumber )
				{
				case 1:

					// draw an empty checkerboard

					theLogicEngine.boardstate = "red black ";
					theRenderer.requestRepaint();

					break;

				case 2:

					// draw a checkerboard with one red checker

					theLogicEngine.boardstate = "red b2 black ";
					theRenderer.requestRepaint();

					break;

				case 3:

					// draw a checkerboard with one black checker

					theLogicEngine.boardstate = "red black e7";
					theRenderer.requestRepaint();

					break;

				case 4:

					// draw a checkerboard with two checkers with the text message
					// "It's your opponent's turn"

					theRenderer.message = "It's your opponent's turn";
					theLogicEngine.boardstate = "red b2 black e7";

					theRenderer.requestRepaint();

					break;

				case 5:

					// draw a fully-initialized checkerboard,
					// with an "It's your turn" message and "Resign" button

				case 6:

					// draw a fully-initialized checkerboard with all UI elements,
					// including a "Resign button,
					// a "Black challenges you to another game.  Accept?" message,
					// and "Yes" and "No" buttons.

					break;

				default:

					System.out.println ("Test " + testnumber + " not implemented yet." );

					break;
				}
			}
			catch(Exception e)
			{
				System.out.println("Invalid test number");
			}
		}

		keyboard.close();
	}

	rendererTestHarness()
	{
		super("Renderer Test Harness");

		addWindowListener( new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		} );

		setSize(width, height);

		theLogicEngine = new CheckersGameLogic();
		theRenderer = new Renderer(theLogicEngine, width, height);

		add(theRenderer.theCanvas, "Center");

		setVisible(true);
	}
}

