import java.math.*;

public class nChooseK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(nChooseK(155, 2));
		System.out.println(nChoose2(155));
		

	}
	
	  // Compute n choose k, which is the number of ways you can choose k objects
	  // out of a set of n.  It is equal to n! / (k! * (n-k)!) .
	public static BigInteger nChooseK(int n, int k)
	  {
	    return fact(n).divide(fact(k).multiply(fact(n-k)));
	  }
	
	public static BigInteger nChoose2(int n)
	  {
	    return fact(n).divide(fact(n-2).multiply(new BigInteger("2")));
	  }

	  public static BigInteger fact(int n) {
		    if (n <= 1) {
		      return(new BigInteger("1"));
		    } else {
		      BigInteger bigN = new BigInteger(String.valueOf(n));
		      return(bigN.multiply(fact(n - 1)));
		    }
	  }

}
