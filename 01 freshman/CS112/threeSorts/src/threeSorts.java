import java.util.*;

/*
 * Matthew Huynh (mhuynh)
 * CS112A1 - hw02
 * February 14, 2008
 * 
 * threeSorts.java
 * This program will ask the user for N and t as input. It will generate
 * t sequences of N random integers and sort each one in increasing order
 * using each of the three algorithms (insertion, merge, quicksort). It
 * will print the average running time for each algorithm. The insertion
 * sort and merge sort code is provided by our course textbook, while the
 * quicksort code is provided by: http://www.cs.princeton.edu/introcs/42sort/QuickSort.java.html 
 */
		
public class threeSorts {
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		long start, stop;
		
		Random rand = new Random();
		
		// Ask user for arguments
		System.out.print("t?: ");
		int t = input.nextInt();
		System.out.print("N?: ");
		int N = input.nextInt();
		
		int[] array = new int[t];
		
		int i;

		// insertion sort
		for (i = 0; i < t; i++)
			array[i] = rand.nextInt(N);
		start = System.nanoTime();
		insertionSort(array);
		stop = System.nanoTime();
		//printArray(array);
		System.out.println("Insertion Sort: " + (stop-start)/1000000000.0 + "s");

		// merge sort
		for (i = 0; i < t; i++)
			array[i] = rand.nextInt(N);	
		start = System.nanoTime();
		mergeSort(array);
		stop = System.nanoTime();
		//printArray(array);
		System.out.println("Merge Sort: " + (stop-start)/1000000000.0 + "s");
		
		// quicksort
		for (i = 0; i < t; i++)
			array[i] = rand.nextInt(N);
		start = System.nanoTime();
		quicksort(array);
		stop = System.nanoTime();
		//printArray(array);
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
	
	/*
	 * Simple insertion sort.
	 * @param a an array of Comparable items.
	 */
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

//----------------------------------------------------------------------------------------------------------	
	
	/*
	 * Internal method that makes recursive calls.
	 * @param a an array of Comparable items.
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
	 * Mergesort algorithm
	 * @param a an array of Comparable items
	 */
	public static void mergeSort (int [] a)
{
	int [] tmpArray = new int[a.length];
	
	mergeSort(a, tmpArray, 0, a.length-1);
}
	
	/*
	 * Internal method that merges two sorted halves of a subarray.
	 * @param a an array of Comparable items.
	 * @param tmpArray an array to place the merged result.
	 * @param leftPos the left-most index of the subarray.
	 * @param rightPos the index of the start of the second half.
	 * @param rightEnd the right-most idnex of the subarray.
	 */
	private static void merge (int [] a, int [] tmpArray, int leftPos, int rightPos, int rightEnd)
{
	int leftEnd = rightPos-1;
	int tmpPos = leftPos;
	int numElements = rightEnd - leftPos + 1;
	
	// Main loop
	while (leftPos <= leftEnd && rightPos <= rightEnd)
		if (a[leftPos] <= a[rightPos])
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
	        while (less(a[++i], a[right]))      // find item on left to swap
	            ;                               // a[right] acts as sentinel
	        while (less(a[right], a[--j]))      // find item on right to swap
	            if (j == left) break;           // don't go out-of-bounds
	        if (i >= j) break;                  // check if pointers cross
	        exch(a, i, j);                      // swap two elements into place
	    }
	    exch(a, i, right);                      // swap with partition element
	    return i;
	}

	// is x < y ?
	private static boolean less(int x, int y)
	{
	    return (x < y);
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