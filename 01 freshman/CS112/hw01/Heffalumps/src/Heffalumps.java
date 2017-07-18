/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw01
 * January 30, 2008
 * 
 * Heffalumps.java
 * This program will repeatedly ask the user how many generations of Heffalumps
 * and Woozles s/he wishes to calculate for, assuming the 1st generation has
 * 1 Heffalump and 1 Woozle. A heffalump produces 3 heffalumps and 2 woozles in its
 * lifetime. A woozle produces 1 heffalump and 5 woozles in its lifetime. The
 * program will terminate when any negative integer is input.
 */

import java.util.Scanner;

public class Heffalumps {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		int generations;
		int[] family = new int[2];
		
		while (true)
		{
			// ask user how many generations
			System.out.print("How many generations?: ");
			generations = input.nextInt();
			
			// if generations is negative, end program
			if (generations < 0)
				break;
			
			if (generations == 0)
				family = count (0, 0, generations);
			else
				family = count (1, 1, generations);
			
			System.out.println("In " + generations + " generations, there will be " +
							   family[0] + " Heffalumps and " + family[1] + " Woozles.");
			
		}

	}
	
	/* 	
	 * returns a 2-element integer array with calculated counts of heffalumps and woozles
	 * after (int)generations with the first element is the count of heffalumps,
	 * and the second element being the count of woozles
	 */
	public static int[] count (int numHeffalumps, int numWoozles, int generations)
	{
		int[] temp = {numHeffalumps, numWoozles};
		int currGenHeffalumps = numHeffalumps;
		int currGenWoozles = numWoozles;
		int nextGenWoozles = 0;
		int nextGenHeffalumps = 0;
		
		while (generations > 1)
		{
			// latest generation breeds new offspring
			nextGenHeffalumps = currGenHeffalumps*3 + currGenWoozles*1;
			nextGenWoozles = currGenHeffalumps*2 + currGenWoozles*5;
			// the new offspring are added to the overall total
			temp[0] += nextGenHeffalumps;
			temp[1] += nextGenWoozles;
			// all the newly bred heffalumps and woozles are now the latest generation
			currGenHeffalumps = nextGenHeffalumps;
			currGenWoozles = nextGenWoozles;
			
			generations--;
		}
		
		return temp;
	}

}