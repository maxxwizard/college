#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>

#define MAX_CHARS 4096


static int ROWS_A = 0;             //Static global variables that store the dimensions of the matrices
static int COLS_A = 0;

static int digits[MAX_CHARS][2];

int storeDigits(FILE *);
int convertDigitsToInt(int, int, int *, int *);
int printInts(int, int, int *);
int checkMatrix();
int transposeMatrix(int, int, int *, int *);

int main(int argc, char *argv[])
{
   
   if(storeDigits(stdin)==1)
      if(checkMatrix()==1)
	  {
	     int matrixA[ROWS_A][COLS_A];
	     convertDigitsToInt(ROWS_A, COLS_A, (int *)digits, (int *)matrixA);
             int transpose[COLS_A][ROWS_A];
	     transposeMatrix(ROWS_A, COLS_A, (int *)matrixA, (int *)transpose);
	     printInts(COLS_A, ROWS_A, (int *)transpose);
          }	     
   exit(0);
}

//We give this function the dimensions of the ORIGINAL matrix
int transposeMatrix(int rows, int cols, int *source, int *transpose)
{
   int i, j;
   for(i = 0; i < rows; i++)
      for(j = 0; j < cols; j++)
      {
          *(transpose + i + j*rows) = *(source + i*cols + j);
      }
   return 1;
}

int printInts(int rows, int cols, int *matrix)
{
   int i, j;
   for(i = 0; i < rows; i++)
   {
      for(j = 0; j < cols; j++)
         printf("%d ", *(matrix + i*cols + j) );
      printf("\n");
   }
   return 1;
}

int convertDigitsToInt(int rows, int cols, int *source, int *storage)
{
   int *digit_ptr, *matrix_ptr;
   digit_ptr = source;

   int i, j;

   for(i = 0; i < rows; i++)
      for(j = 0; j < cols; j++)
      {
         int sum = 0;
         int negative = 0;
         while(*digit_ptr < 0 && *digit_ptr != -5)
            digit_ptr += 2;
         if(*digit_ptr==-5)
         {
            negative = 1;
            digit_ptr += 2;
         }
         while(*digit_ptr > -1)
         {
            if(*(digit_ptr+1)==1)
            {
               sum += *digit_ptr;
            }
            else
            {
               sum += 10 * ( (*(digit_ptr+1)) - 1 ) * (*(digit_ptr)); 
            }
            digit_ptr += 2;
         }
         if(negative)
            *(storage + i*cols + j) = -1*sum;
         else
            *(storage + i*cols + j) = sum;
      }  
      return 1; 
}

int checkMatrix()
{
   int i = 0;
   for(i; i < MAX_CHARS;i++)      //Scans through all digits in matrix A
   {
      if(ROWS_A==0 && (digits[i][0]==-2 || digits[i][0]==-3))  //If we have not gone past the first row and we see a space or new line
         COLS_A++;                    //We increase the column count (space between elements, and the newline character adds +1 for the last element in the row)

      if(digits[i][0]==-3)            //When a newline digit is encountered...
         ROWS_A++;                    //increase the number of rows by 1
      else if(digits[i][0]==-1)       //When the "border" between the two matrices is encountered, break
         break;
   }
   return 1;
}


//Takes the digits read in from a passed file descriptor name (*fp) and stores them in
//the static two dimensional array "digits".
int storeDigits(FILE *fp)
{
   int cur_dig;
   int char_count = 0;

   while( (cur_dig = getc(stdin)) != EOF && char_count < MAX_CHARS-1)
   {
      if(cur_dig > 47 && cur_dig < 58) //Checks to see that the cur_dig is a digit from 0 to 9
      {
        
         digits[char_count][0] = cur_dig - 48;  //Converts the ASCII integer from stdin into an integer from 0 to 9 to store in digits
         char_count++;
      }

      else if(cur_dig==45)              //Checks to see that the cur_dig is a negative sign
      {
         digits[char_count][0] = -5;    //Sets the digit to a special -5 value to denote a negative sign
         digits[char_count][1] = 0;     //Sets the place value to 0 as it's a special negative digit
         char_count++;
      }

      else if(cur_dig==32)              //If not, check if it's a space
      {
         digits[char_count][0] = -2;      //Stores -2 to denote a space between matrix elements
         digits[char_count][1] = 0;       //Because it's a special negative digit, set place value to 0
         
         int prev_neg = char_count-1;   //Starts looking for the previous negative value at the previous row of the digit array
         
         while(prev_neg > -1 && digits[prev_neg][0] > 0)    //While the i-th digit is actually a number (not a special negative value)
         {                                           //and within bounds of the digit array
            prev_neg--;                  //Decreases the prev_neg index so that we check the previous element in the digit array
         }
         int i;
         for(i = prev_neg+1; i < char_count; i++)
         {
            digits[i][1] = char_count - i; //Stores the place value by setting it equal to the digit count
         }                                 
         char_count++;
      }

      else if(cur_dig==10) 		 //If not, check if it's a newline character
      {         
	digits[char_count][0] = -3;  //Sets the newline character between rows of a matrix
        digits[char_count][1] = 0;   //Sets place value to zero for special negative digits

        int prev_neg = char_count-1;   //Starts looking for the previous negative digit

        while(prev_neg > -1 && digits[prev_neg][0] > 0)
        {
           prev_neg--;
        }
        int i;
        for(i = prev_neg+1; i < char_count; i++)
        {
           digits[i][1] = char_count-i;
        }
	char_count++;
     }     
     else                               //If not, the character is invalid and so is the matrix
     {         
        fprintf(stderr, "Invalid character found in matrix.\n");
        return -1;                      //Else we return a -1 to denote an error
     }
   }

   if(cur_dig != EOF) //If we have not reached EOF yet, we ran out of space
   {
      fprintf(stderr, "Matrices contain too many characters. \nIncrease MAX_CHAR in matmult_p or reduce the number of characters in the input.\n");

      return -1;                        //Else we return a -1 to denote an error
   }
   else 
   {
      digits[char_count][0] = -1;       //Stores the special negative digit that signifies the end of matrix data
      digits[char_count][1] = 0;        //Special negative digits don't require a place value
      return 1;				//Returns successfully
   }
}

