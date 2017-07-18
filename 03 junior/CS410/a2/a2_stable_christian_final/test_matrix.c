#include <stdio.h>
int main()
{
   //Convention assumes that matrices will be written to stdout;
   //this stdout will be piped into the stdin of the matrix multiplier.

   //The accepted format of a matrix is as follows:
   //1) Each row of the matrix is written as a series of integers separated by spaces
   //2) A '\n' newline character separates each row from the next
   //3) The two matrices are separated by a blank line; this is implemented by having a
   // '\n' character by itself.
   //4) We allow for negative signs but they MUST be at the very beginning of the integer

   printf("10 20\n30 40\n50 60\n");	//Creates matrix A
   printf("\n");                //Prints a blank line to separate matrices A and B
   printf("10 20 30 40\n50 60 70 80\n");  //Creates a 2x4 matrix B

}
