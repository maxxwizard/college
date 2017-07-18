/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw01
 * January 30, 2008
 * 
 * Wally.java
 * This program will repeatedly ask the user how many generations of Wallys
 * s/he wishes to calculate for, assuming the 1st generation is 1 newborn Wally.
 * A newborn Wally matures after one generation. A mature Wally produces one newborn
 * Wally each generation. The program outputs two calculations, one using an iterative
 * algorithm and the other using a recursive algorithm. The program will terminate
 * when a negative integer is input.
 */

import java.util.Scanner;

public class Wally {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		int generations;
		
		while (true)
		{
			System.out.print("How many generations?: ");
			generations = input.nextInt();
			
			// if generations is negative, end program
			if (generations < 0)
				break;
			
			int wallys = countWallyIterative(generations);
			int wallys2 = countWallyRecursive(generations);
			
			System.out.println("(Iterative) In " + generations + " generations, there will be " + wallys + " Wallys.");
			System.out.println("(Recursive) In " + generations + " generations, there will be " + wallys2 + " Wallys." + "\n");
		}

	}
	
	/*
	 * The recursive algorithm for calculating total Wallys after n generations
	 * is the same algorithm as calculating the Fibonacci sequence.
	 */
	public static int countWallyRecursive (int n)
	{
	    if (n == 0)
	    	return 0;
	    
	    if (n == 1)
	    	return 1;
	    
	    return countWallyRecursive(n-1) + countWallyRecursive(n-2);
	}
	
	/*
	 * The iterative algorithm for calculating total Wallys after n generations.
	 */
	public static int countWallyIterative (int n)
	{
		if (n == 0)
			return 0;
		
		int matureWallys = 0;
		int newWallys = 0;
		int oldWallys = 1;
		
		for (int g = n; g > 1; g--)
		{
			// if this isn't the first generation
			if (g != n)
				// mature wallys will create one new wally when generation starts
				newWallys = matureWallys;
			
			// all the old wallys add into the pool mature wallys
			matureWallys += oldWallys;
			
			// the new wallys turn into the old wallys
			oldWallys = newWallys;
			
			// the total number of wallys is matureWallys and oldWallys at the end of this generation
		}
		
		return matureWallys + oldWallys;
	}

}
