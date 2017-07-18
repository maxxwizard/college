/**
 * CS350 Homework 1 #5
 * Map uniformly distributed random variables to 
 * exponentially distributed random variables.
 * 
 * @author Susana Fong
 *
 */

import java.util.Random;

public class RandomVariables {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Random rand = new Random();
		double mean = 10;
		
		for ( int i = 0; i < 100; i++ )
		{
			double uniform = rand.nextDouble();
			double exponential = uniformToExponential( mean, uniform );
			
			System.out.println( exponential );
		}
	}
	
	public static double uniformToExponential( double mean, double uniform )
	{
		double lambda = 1 / mean;
		double exponential = ( -1 * Math.log( 1 - uniform ) )/ lambda;
		
		return exponential;
	}
}
