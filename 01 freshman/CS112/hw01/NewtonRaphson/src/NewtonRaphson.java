/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw01
 * January 30, 2008
 * 
 * NewtonRaphson.java
 * This program will repeatedly ask the user for 4 arguments (k, n, x0, iterations)
 * that will be used to calculate the kth root of a number n.
 */

import java.util.Scanner;

public class NewtonRaphson {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		int k;

		while (true)
		{
			// ask user for k
			System.out.print("k?: ");
			k = input.nextInt();
			
			// if k is negative, end program
			if (k < 0)
				break;
			
			// ask user for n
			System.out.print("n?: ");
			float n = input.nextFloat();
			
			//ask user for x0
			System.out.print("x0?: ");
			float x0 = input.nextFloat();
			
			//ask user for iterations
			System.out.print("iterations?: ");
			int iter = input.nextInt();
			
			// print out approximation of kth root
			System.out.println("approximation: " + approximate(k, n, x0, iter) + "\n");
			
		}

	}
	
	/*
	 * A recursive function that will approximate the kth root of a number n,
	 * using x0 as the starting guess and iter iterations. The approximation
	 * in general becomes more accurate as the iterations increase.
	 */
	public static float approximate (int k, float n, float x0, int iter)
	{
		if (iter == 0)
			return x0;
		
		float nextX = 1/(float)k * ((float)(k-1)*x0 + (float) (n/Math.pow(x0,k-1)));
		
		return approximate (k, n, nextX, iter-1);
	}

}
