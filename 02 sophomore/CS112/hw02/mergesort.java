import java.util.*;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw2
 * February 11, 2009
 * 
 * mergesort.java
 * This program performs a mergesort on an array of integers
 * by utilizing an iterative algorithm that performs merges
 * of larger and larger size using nested loops.
 */

public class mergesort {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		
		// Ask user for N
		System.out.print("N?: ");
		int N = input.nextInt();
		
		int[] array = new int[N];
		
		for (int i = 0; i < N; i++)
			array[i] = rand.nextInt(N);	
		
		printArray(array);
		
		System.out.println();
		
		mergeSort(array, array.length);
		
		printArray(array);

	}
	
//----------------------------------------------------------------------------------------------------------
	
	/*
	 * Prints an integer array with comma separator.
	 */
	public static void printArray(int [] a)
	{
		System.out.print(a[0]);
		for (int i = 1; i < a.length; i++)
			System.out.print(", " + a[i]);
	}
	
//----------------------------------------------------------------------------------------------------------
	
	/*
	 * Merge from one array into another
	 */
	   static void merge (int[] c, int[] d, int lt, int md, int rt)
	   {
		  // Merge c[lt:md] and c[md+1:rt] to d[lt:rt]
	      int i = lt,       // cursor for first segment
	          j = md+1,     // cursor for second
	          k = lt;       // cursor for result

	      // merge until i or j exits its segment
	      while ( (i <= md) && (j <= rt) )
	         if (c[i] <= c[j])  d[k++] = c[i++];
	         else               d[k++] = c[j++];

	      // take care of left overs
	      while ( i <= md )
	         d[k++] = c[i++];
	      while ( j <= rt )
	         d[k++] = c[j++];
	   }

	/*
	 * Perform one pass through the two arrays, using Merge() above
	 */
	   static void mergePass (int x[], int y[], int s, int n)
	   {
		  // Merge adjacent segments of size s.
	      int i = 0;

	      while (i <= n - 2 * s)
	      {	
	    	 //Merge two adjacent segments of size s
	         merge (x, y, i, i+s-1, i+2*s-1);
	         i = i + 2*s;
	      }
	      
	      // fewer than 2s elements remain
	      if (i + s < n)
	         merge (x, y, i, i+s-1, n-1);
	      else
	         for (int j = i; j <= n-1; j++)
	            y[j] = x[j];   // copy last segment to y
	   }

	/*
	 * Main driver
	 */
	   public static void mergeSort (int a[], int n)
	   {// Sort a[0:n-1] using merge sort.
	      int   s = 1;      // segment size
	      int[] b = new int [n];

	      while (s < n)
	      {  mergePass (a, b, s, n); // merge from a to b
	         s += s;                 // double the segment size
	         mergePass (b, a, s, n); // merge from b to a
	         s += s;                 // again, double the segment size
	      }
	      
	   }
	   
}