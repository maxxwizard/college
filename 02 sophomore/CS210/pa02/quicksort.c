/*
 ============================================================================
 Name        : quicksort.c
 Author      : Matthew Huynh
 Description : Quicksort in C - PA2 for CS210 (Matta)
 ============================================================================
 */

#include <stdio.h>

// strptr is an array filled with pointers to char arrays (strings)
char *strptr[] = {"mips", "spim", "Syscall", "xspim", "Quicksort", "recursion"};

/*
 * SWAP
 * Purpose: swaps two pointers in an array
 */
void swap(char *v[], int i, int j) {
	char *temp;
	
	temp = v[i];
	v[i] = v[j];
	v[j] = temp;
}

/*
 * PRINTARRAY
 * Purpose: prints an array of strings to the console
 */
void PrintArray(char *v[], int n) {
	int i;
	// cycle through array of strings
	for (i = 0; i < n; i++) {
		// print each string
		printf("%s ", v[i]);
	}
	printf("\n");
}

/*
 * STRCMP
 * Purpose: returns <0 if s<t, 0 if s==t, >0 if s>t
 */
int strcmp(char *s, char *t)
{
	int i;
	
	for (i = 0; s[i] == t[i]; i++)
		if (s[i] == '\0')
			return 0;
	return s[i] - t[i];
}

/*
 * QUICKSORT
 * Pre-reqs: Arr = pointer to unsorted array;  left = low index; right = high index
 * Purpose: sorts an array of strings
 */
void Quicksort(char *v[], int left, int right) {
	int i, last;
	if (right < left+1) //(left >= right)
		return;
	int j = (left+right)/2;
	swap(v, left, j);
	last = left;
	for (i = left+1; i < right+1; i++) { //i <= right
		char *k = v[i];
		char *l = v[left];
		int m = strcmp(k, l);
		if (m < 0) {
			last++;
			swap(v, last, i);
		}
	}
	swap(v, left, last);
	Quicksort(v, left, last-1);
	Quicksort(v, last+1, right);
}

int main() {
	// PRINT UNSORTED ARRAY
	PrintArray(strptr, 6);
	
	// QUICKSORT
	Quicksort(strptr, 0, 5);
	
	// PRINT FINAL ARRAY
	printf("\nfinal array:\n");
	PrintArray(strptr, 6);
	
	printf("\n");
	return 0;
}
