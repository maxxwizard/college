import java.util.*;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw02
 * February 10, 2009
 * 
 * threeSorts.java
 * This program will ask the user for N and array sort order. It will
 * generate arrays of N random integers and sort each one in decreasing order
 * using each of the three algorithms (insertion, merge, quicksort).
 * It will print the average running time for each algorithm.
 * Insertion sort code provided by: http://mathbits.com/MathBits/Java/arrays/InsertionSort.htm
 * Merge sort code provided by: http://users.cs.fiu.edu/~weiss/dsaajava2/code/
 * Quicksort code provided by: http://www.cs.princeton.edu/introcs/42sort/QuickSort.java.html 
 */
		
public class threeSorts {
	
	public static void main(String[] args) {
		
		// Create a random number generator		
		long seed = Calendar.getInstance().getTimeInMillis();
        Random rand = new Random(seed);
		
		// Ask user for arguments
        Scanner input = new Scanner(System.in);
		System.out.print("N?: ");
		int N = input.nextInt();
		System.out.print("(1) Random or (2) Sequential ?: ");
		int sort = input.nextInt();
		
		// Create 3 arrays of N size
		int[] array0 = new int[N];
		int[] array1 = new int[N];
		int[] array2 = new int[N];
		int randomInt;
		
		if (sort == 1) {
			// Fill the 3 arrays with N random integers (the arrays are identical)
			for (int i = 0; i < N; i++) {
				randomInt = rand.nextInt(N);
				//array0[i] = randomInt;
				//array1[i] = randomInt;
				array2[i] = randomInt;
			}
		} else {
			// Create 3 identical arrays of increasing integers (i.e. an ascending sorted array)
			fillArraySeq(array0);
			fillArraySeq(array1);
			fillArraySeq(array2);
		}
		
		//printArray(array0);
		System.out.println();

		// Variables to time the sort algorithms
		long start, stop;
		
		// insertion sort
		start = System.nanoTime();
		insertionSort(array0);
		stop = System.nanoTime();
		//printArray(array0);
		System.out.println("Insertion Sort: " + (stop-start)/1000000000.0 + "s");
		
		// merge sort
		start = System.nanoTime();
		mergeSort(array1);
		stop = System.nanoTime();
		//printArray(array1);
		System.out.println("Merge Sort: " + (stop-start)/1000000000.0 + "s");
		
		// quicksort
		start = System.nanoTime();
		quicksort(array2);
		stop = System.nanoTime();
		//printArray(array2);
		System.out.println("Quicksort: " + (stop-start)/1000000000.0 + "s");
		
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
	
	public static void fillArraySeq (int [] a) {
		for (int i = 0; i < a.length; i++)
			a[i] = i;
	}
	
//----------------------------------------------------------------------------------------------------------
	
	/*
	 * Simple insertion sort (descending).
	 * @param a an array of integers.
	 */
	public static void insertionSort( int[] a)
{
	 int j;  // the number of items sorted so far
     int key;  // the item to be inserted
     int i;  

     for (j = 1; j < a.length; j++)    //Notice starting with 1 (not 0)
     {
           key = a[ j ];
           for(i = j - 1; (i >= 0) && (a[ i ] < key); i--)   //Move smaller values up one position
          {
                 a[ i+1 ] = a[ i ];
          }
         a[ i+1 ] = key;    //Insert key into proper position
     }
}

//----------------------------------------------------------------------------------------------------------	
	
	/*
	 * Internal method that makes recursive calls.
	 * @param a an array of integers.
	 * @param tmpArray an array to place the merged result.
	 * @param left the left-most index of the subarray
	 * @param right the right-most index of the subarray.
	 */
	private static void mergeSort (int [] a, int [] tmpArray, int left, int right)
{
	if (left < right)
	{
		int center = (left+right)/2;
		mergeSort(a, tmpArray, left, center);
		mergeSort(a, tmpArray, center+1, right);
		merge(a, tmpArray, left, center+1, right);
	}
}

	/*
	 * Mergesort algorithm (descending)
	 * @param a an array of integers
	 */
	public static void mergeSort (int [] a)
{
	int [] tmpArray = new int[a.length];
	
	mergeSort(a, tmpArray, 0, a.length-1);
}
	
	/*
	 * Internal method that merges two sorted halves of a subarray.
	 * @param a an array of integers.
	 * @param tmpArray an array to place the merged result.
	 * @param leftPos the left-most index of the subarray.
	 * @param rightPos the index of the start of the second half.
	 * @param rightEnd the right-most index of the subarray.
	 */
	private static void merge (int [] a, int [] tmpArray, int leftPos, int rightPos, int rightEnd)
{
	int leftEnd = rightPos-1;
	int tmpPos = leftPos;
	int numElements = rightEnd - leftPos + 1;
	
	// Main loop
	while (leftPos <= leftEnd && rightPos <= rightEnd)
		if (a[leftPos] >= a[rightPos])
			tmpArray[tmpPos++] = a[leftPos++];
		else
			tmpArray[tmpPos++] = a[rightPos++];
	
	while (leftPos <= leftEnd) // Copy rest of first half
		tmpArray[tmpPos++] = a[leftPos++];
	
	while (rightPos <= rightEnd) // Copy rest of right half
		tmpArray[tmpPos++] = a[rightPos++];
	
	// Copy tmpArray back
	for (int i = 0; i < numElements; i++, rightEnd--)
		a[rightEnd] = tmpArray[rightEnd];
}
	
//----------------------------------------------------------------------------------------------------------
	/*
	 * Quicksort algorithm (descending).
	 * @param a an array of integers
	 */
	public static void quicksort(int[] a)
	{
		shuffle(a);                        // to guard against worst-case
		quicksort(a, 0, a.length - 1);
	}
	
	public static void quicksort(int[] a, int left, int right)
	{
	    if (right <= left) return;
	    int i = partition(a, left, right);
	    quicksort(a, left, i-1);
	    quicksort(a, i+1, right);
	}

	private static int partition(int[] a, int left, int right)
	{
	    int i = left - 1;
	    int j = right;
	    while (true) {
	        while (greater(a[++i], a[right]))      // find item on left to swap
	            ;                               // a[right] acts as sentinel
	        while (greater(a[right], a[--j]))      // find item on right to swap
	            if (j == left) break;           // don't go out-of-bounds
	        if (i >= j) break;                  // check if pointers cross
	        exch(a, i, j);                      // swap two elements into place
	    }
	    exch(a, i, right);                      // swap with partition element
	    return i;
	}

	// is x > y ?
	private static boolean greater(int x, int y)
	{
	    return (x > y);
	}

	// exchange a[i] and a[j]
	private static void exch(int[] a, int i, int j)
	{
	    int swap = a[i];
	    a[i] = a[j];
	    a[j] = swap;
	}

	// shuffle the array a
	private static void shuffle(int[] a)
	{
	    int N = a.length;
	    for (int i = 0; i < N; i++) {
	        int r = i + (int) (Math.random() * (N-i));   // between i and N-1
	        exch(a, i, r);
	    }
	}

}