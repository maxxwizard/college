import java.util.*;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw02
 * February 10, 2009
 * 
 * quicksort.java
 * This program quicksorts an array of 1,000,000 random integers and prints the running times for
 * the quicksort algorithm with and without an insertion sort.
 * Quicksort code provided by: http://www.cs.bu.edu/fac/byers/courses/112/S09/lectures/lec6quicksort.java
 * 
 * 1. 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
 *    The worst case asymptotic running time would be n-squared since the quicksort tree would be
 *    come out to be chain of nested calls. Each call would have sublists of size 1 and n-1.
 * 2. The worst case running time would once again be n-squared if the input array contained
 *    all elements of the same value.
 * 3. base | quicksort w/o | quicksort w/ | faster?
 *    3      0.198348394     0.209979760
 *    4      0.196255670     0.220546466
 *    4      0.195013892     0.206101893
 *    5      0.207243378     0.206475125    *
 *    5      0.197356088     0.191689167    *
 *    6      0.200643378     0.214608840
 *    6      0.200099454     0.215871570
 *    10     0.197488507     0.216536739
 *    
 *    I tried various bases and 5 seems to be the one where the inclusion of the insertion sort
 *    of small arrays seem to possibly give better performance.
 *    
 */
		
public class quicksort {
	
	public static void main(String[] args) {
		
		// Create a random number generator		
		long seed = Calendar.getInstance().getTimeInMillis();
        Random rand = new Random(seed);
		
		int N = 1000000;
		
		// Create an array of N random integers
		int[] array0 = new int[N];
		int[] array1 = new int[N];
		int randomInt;
		for (int i = 0; i < N; i++) {
			randomInt = rand.nextInt(N);
			array0[i] = randomInt;
			array1[i] = randomInt;
		}
		
		//printArray(array0);
		System.out.println();

		// Variables to time the sort algorithm
		long start, stop;
		
		// quicksort
		start = System.nanoTime();
		quicksort(array0, 0, array0.length-1);
		stop = System.nanoTime();
		//printArray(array0);
		System.out.println("Quicksort: " + (stop-start)/1000000000.0 + "s");
		
		// quicksort with base
		start = System.nanoTime();
		quicksortWithBase(array1, 0, array1.length-1);
		stop = System.nanoTime();
		//printArray(array1);
		System.out.println("Quicksort with base: " + (stop-start)/1000000000.0 + "s");
		
	}

//----------------------------------------------------------------------------------------------------------
	
	/*
	 * Prints an integer array with comma separator.
	 */
	public static void printArray(int [] a)
	{
	for (int i = 0; i < a.length; i++)
		System.out.print(a[i] + ", ");
	}
	
//----------------------------------------------------------------------------------------------------------
	
	public static void quicksortWithBase (int [] A, int a, int b)
	{
	    if ( (a < b) && (A.length > 5) )
	    {   
	       int pivot = A[b];        // or choose one element at random, swap into index b
	       int l = a; 
	       int r = b-1;

	       // Keep pivoting until the l and r indices cross over.
	       while (l <= r) {

	           while (l <= r && A[l] <= pivot)    // slide l right until it points to an
		       l++;                           	  //  element larger than pivot

	           while (l <= r && A[r] >= pivot)    // slide r left until it points to an
		       r--;                               //  element smaller than pivot

	           if (l < r)                         // swap out-of-order pairs 
		       swap (A, l, r);
	       }
	       //  Re-position the pivot into its correct slot
	       swap (A, l, b);

	       quicksort (A, a, l-1);
	       quicksort (A, l + 1, b);
	    } else { // if the sublist A becomes small enough, insertionSort it instead
	    	insertionSort(A);
	    }
	}
	
	public static void quicksort (int [] A, int a, int b)
	{
	    if (a < b)
	    {   
	       int pivot = A[b];        // or choose one element at random, swap into index b
	       int l = a; 
	       int r = b-1;

	       // Keep pivoting until the l and r indices cross over.
	       while (l <= r) {

	           while (l <= r && A[l] <= pivot)    // slide l right until it points to an
		       l++;                           	  //  element larger than pivot

	           while (l <= r && A[r] >= pivot)    // slide r left until it points to an
		       r--;                               //  element smaller than pivot

	           if (l < r)                         // swap out-of-order pairs 
		       swap (A, l, r);
	       }
	       //  Re-position the pivot into its correct slot
	       swap (A, l, b);

	       quicksort (A, a, l-1);
	       quicksort (A, l + 1, b);
	    }
	}
	
	// Swap a[i] and a[j]
	private static void swap(int[] a, int i, int j)
	{
	    int swap = a[i];
	    a[i] = a[j];
	    a[j] = swap;
	}
	
//----------------------------------------------------------------------------------------------------------
	
	public static void insertionSort( int[] a)
	{
		int j;
			
		for (int p = 1; p < a.length; p++)
		{
			int tmp = a[p];
			for (j = p; j > 0 && tmp < a[j-1]; j--)
				a[j] = a[j-1];
			a[j] = tmp;
		}
	}
	
} // End of quicksort class.