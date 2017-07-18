import java.util.*;

/**
 * 
 */

/**
 * @author Matthew Huynh
 *
 */
public class RandGen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random uniRand = new Random();
		double T = 2.7;
		int N = 100;
		System.out.println(N + " random values from exponential distribution w/ mean = " + T);
		for (int i = 0; i < N; i++)
		{
			System.out.println(getReal(T, uniRand.nextDouble()));
		}
	}
	
	static double getReal(double mean, double uniform)
	{
		double U = uniform;
		double lambda = 1/mean;
		double V = (-1*Math.log(1-U))/lambda;
		return V;
	}

}