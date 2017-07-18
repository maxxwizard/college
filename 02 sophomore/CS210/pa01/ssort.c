#include <stdio.h>

int Array[8] = {10, 3, 6, 1, 5, 9, 4, 2};

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


int main() { 
  Sort(Array, 8);
  printf("The sorted array is:   ");    
  PrintArray(Array, 8); 
  printf("\n"); 
}
