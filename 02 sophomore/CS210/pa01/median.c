/*  
 *  MATTHEW HUYNH
 *  CS210 - MATTA
 *  OCTOBER 31, 2008 
 *  PROGRAMMING ASSIGNMENT #1 - PART 2
 *
 *  Start of file median.c
 *
 */

#include <stdio.h>

int Array[9] = {10, 3, 6, 1, 7, 5, 9, 4, 2};

void PrintArray(int Arr[], int n) { 
  int i;
  for (i=0; i < n; i++) {
  	printf("%d  ", Arr[i]); 
   } 
}

void Swap(int *x, int *y) { 

  int temp;

  temp = *x;
   *x = *y;
   *y = temp; 
} 

int MinPosition(int Arr[], int start, int stop) { 
    int min = Arr[start]; 
    int minpos = start; 
    int index = start + 1; 
    while (index <= stop) { 
        if (Arr[index] < min) { 
           min = Arr[index]; 
           minpos = index;
	} 
         
        index++; 
    }
     
    return minpos; 
}

void Sort(int Arr[], int n) { 
    int i = 0; 
    int MinPos; 
    while (i < n - 1) { 
        MinPos = MinPosition(Arr, i, n-1); 
        Swap(&Arr[i], &Arr[MinPos]); 
        i++; 
    }
}

int Median(int Arr[], int n) {
    Sort(Arr, n);
    int A = Arr[n/2-1];
    int B = Arr[n/2];

    if (n % 2 != 0) { 	// if n is odd
        return B;
    }

    return A + (B-A)/2; // if n is even
}


int main() {
  printf("The array is:  ");
  PrintArray(Array, 9);
  printf("\n median = %d \n", Median(Array, 9));
  printf("The sorted array is:   ");
  PrintArray(Array, 9);
  printf("\n"); 
}
