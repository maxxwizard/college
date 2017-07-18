import java.util.*;

/**
 * @author Matthew Huynh
 * CS320 Fall 2009 - Kfoury
 * Assignment #1
 * 
 * This program takes two positive integers as command line arguments
 * and displays an ASCII text approximation of the Mandelbrot set
 * using the resolution and orbit index specified by the integers.
 */
public class Mandelbrot {

	public static void main(String[] args) {
		
		int r = 5, i = 5; // program runs with defaults r = 5, i = 5
		
		if (args.length > 0) {
		    try {
		        r = Integer.parseInt(args[0]);
		        i = Integer.parseInt(args[1]);
		        
		        if (r < 1 || i < 1)
		        {
		        	throw new Exception();
		        }
		    } catch (Exception e) {
		        System.err.println("This program accepts 2 positive integers as arguments.");
		        System.exit(1);
		    }
		}
		
		System.out.println( mandelbrot(r, i) );
		
	}
	
	static String mandelbrot(int r, int i)
	{
		String temp = "";
		for (double d : distances(r, i))
		{
			temp += disp(d);
		}
		
		return split(temp,(3*r)+1,'\n');
	}
	
	static double[] distances(int r, int i)
	{
		ArrayList<Pair> points = plane(r);
		
		double[] result = new double[points.size()];
		
		int n = 0;
		for (Pair p: points)
		{
			ArrayList<Pair> orbitPoints = orbit(p,i+1);
			double distance = norm(orbitPoints.get(i));
			result[n] = distance;
			n++;
		}
		
		return result;
	}
	
	static double norm (Pair p)
	{
		return p.a*p.a + p.b*p.b;
	}
	
	static char disp (double d)
	{
		if (0.15 > d)
			return '#';
		else if (0.5 > d)
			return 'x';
		else if (1.0 > d)
			return '.';
		
		return ' ';
	}
	
	static Pair P (Pair p1, Pair p2)
	{
		double a = p2.a*p2.a - p2.b*p2.b + p1.a;
		double b = 2*p2.a*p2.b + p1.b;
		
		return new Pair(a, b);
	}
	
	// applies function P on origin point (0,0) n times
	static ArrayList<Pair> orbit (Pair start, int n)
	{
		ArrayList<Pair> result = new ArrayList<Pair>();
		
		Pair repeater = new Pair(0,0);
		result.add(repeater); // add point (0,0)
		n--;
		
		while (n != 0)
		{
			repeater = P(start, result.get(result.size()-1)); // apply function using start and last result
			result.add(repeater);
			n--;
		}
		
		return result;
	}
	
	static ArrayList<Pair> plane (int r)
	{
		ArrayList<Pair> result = new ArrayList<Pair>();
		
		double x = (-2*r), y = (-1*r), a, b;
		
		do // y coordinate loop
		{
			b = y/r;
			do // x coordinate loop
			{
				a = x/r;
				if (a >= -2 && a <= 1 && b >= -1 && b <= 1)
				{
					result.add(new Pair(a, b)); // add point if conditions are satisfied
				}
				x++;
			} while (a >= -2 && a <= 1);
			x = (-2*r); // reset x
			y++;
		} while (b >= -1 && b <= 1);
		
		return result;
	}
	
	// recursive function that inserts the character x every i-th index in string s
	static String split (String s, int i, char x)
	{
		if (i > s.length())
			return s;
		return s.substring(0,i) + x + split(s.substring(i), i, x);
	}
	
} // end of Mandelbrot class

class Pair
{
	public double a;
	public double b;
	
	public Pair()
	{
		this.a = 0;
		this.b = 0;
	}
	
	public Pair(double a, double b)
	{
		this.a = a;
		this.b = b;
	}
	
	@Override
	public String toString()
	{
		return "(" + a + "," + b + ")";
	}
} // end of Pair class
