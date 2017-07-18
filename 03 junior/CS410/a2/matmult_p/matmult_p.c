#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <string.h>
#include <setjmp.h>
#include <signal.h>
#include <sys/wait.h>
#define MAX_CHARS 4096


static int digits[MAX_CHARS][2];   //Will store individual digits in the 0th column and their place value 
                                   //in the 1st column when calculated; assumes a decimal counting system. 
                                   //In the 0th column: -1 signifies end of all matrix data,
                                   //-2 signifies a space between matrix elements
                                   //-3 signifies a new line between rows of a matrix
                                   //-4 signifies the separation between the two matrices`
                                   //-5 signifies a negative sign

static int ROWS_A = 0;             //Static global variables that store the dimensions of the matrices
static int COLS_A = 0;

static int ROWS_B = 0;
static int COLS_B = 0;

static int CHILD_COUNT = 0;       //Keeps track of the completed child process count via signals
static int MAX_CHILDREN = 0;      //The number of processes we expect to complete in order to get product

void printDigits();
int convertDigitsToInt(int, int, int *, int *);
int printInts(int, int, int *);
int storeDigits(FILE *);
int checkMatrix();
static void sig_chld(int);

int main(int argc, char *argv[])
{
   if(signal(SIGCHLD, sig_chld) == SIG_ERR)        //Registers the signal handler for children
      perror("Can't register sig_chld handler");
   if(storeDigits(stdin)!=1)                       //Reads stdin and parses it into a series of integers representing 
      fprintf(stderr,"Error storing digits.\n");   //individual digits, spaces, newlines, etc.
   if(checkMatrix()==1)
   {
      //allocates the shared memory for matrix A, matrix B, and the product matrix
      int shm_id, matA_id, matB_id;
      matA_id = shmget(IPC_PRIVATE, sizeof(int)*ROWS_A*COLS_A, 0644 );
      matB_id = shmget(IPC_PRIVATE, sizeof(int)*ROWS_B*COLS_B, 0644 );
      shm_id = shmget(IPC_PRIVATE, sizeof(int)*ROWS_A*COLS_B, 0666 );

      //attaches the shared memory to pointers
      int *pointA =  shmat(matA_id, (void *)0, 0);
      int *pointB = shmat(matB_id, (void *)0, 0);
      int *product = shmat(shm_id, (void *)0, 0);
   
      //constructs integer representation of matrix A from digits and stores it in the shared memory
      convertDigitsToInt(ROWS_A, COLS_A, (int *)digits, (int *)pointA);

      //finds a pointer to the second matrix stored in digits
      int *pointToB = (int *)digits;
      while(*(pointToB)!=-4)
         pointToB += 2;
      
      //constructs integer representation of matrix B from digits and stores it in the shared memory
      convertDigitsToInt(ROWS_B, COLS_B, (int *)pointToB, (int *)pointB);

      //calculates the expected numbe of children processes that will be created by multiplying the
      //dimensions of the product matrix together
      MAX_CHILDREN = ROWS_A*COLS_B;

      printf("\nMatrix A =\n");
      printInts(ROWS_A, COLS_A, (int *)pointA);
      printf("\nMatrix B =\n");
      printInts(ROWS_B, COLS_B, (int *)pointB);
      printf("\n");     

      int i, j;
      for(j = 0; j < ROWS_A; j++)
         for(i = 0; i < COLS_B; i++)  //iterates through each element of the product matrix
         {
            pid_t pid;
            pid = fork();             //forks to create a child to handle the multiplication

            if( pid == 0)
            {
               //all the following character arrays that are initialized and filled with sprintf
               //are arguments that are passed to the multiply function that each child will execute.
       
               //constructs arguments that identify which row from A and column from B we want to multiply
               char rowA[10];                
               char colB[10];
               sprintf(rowA, "%d", j);
               sprintf(colB, "%d", i);

               //constructs arguments that provide the necessary dimensions of A and B we need in order to
               //do the required pointer arithmetic
               char colsA[10];
               char colsB[10];
               char rowsB[10];
               sprintf(colsA, "%d", COLS_A);
               sprintf(colsB, "%d", COLS_B);
               sprintf(rowsB, "%d", ROWS_B);
             
               //constructs arguments that hold the shared memory IDs that we will use to shmat in the children
               char share_id[100];
               char shareA_id[100];
               char shareB_id[100];
               sprintf(share_id, "%d", shm_id);
               sprintf(shareA_id, "%d", matA_id);
               sprintf(shareB_id, "%d", matB_id);

               //executes the multiply program
               execlp("./multiply", rowA, colsA, shareA_id, colB, colsB, rowsB, shareB_id , share_id, (char *) 0);
            }
            else  //Parent should just continue unless we have forked last child
            {
               if(i==COLS_B-1 && j == ROWS_A-1) //we only begin waiting if we have spawned all children
               {
                  while(wait(NULL) > 0 && CHILD_COUNT<MAX_CHILDREN ) //we wait while children keep closing and
                  {                                                  //we have yet to count completion of  all child processes
                     wait(NULL);
                  }
                  printf("\nAll children processes done; printing the result matrix:\n");
                  printInts(ROWS_A, COLS_B, (int *) product);
 
                  //detaches the shared memory
                  shmdt(pointA);
                  shmdt(pointB);
                  shmdt(product);
               }
            }
         }
   }
   else
      fprintf(stderr,"Matrices A and B cannot be multiplied.\n");
   
   exit(0);
}

//Signal handler for child processes
static void sig_chld(int signo)
{
   pid_t pid;
   int status;
   if( signal(SIGCHLD, sig_chld) == SIG_ERR) 
   {
      perror("signal(SIGCHLD) error");
   }
   if( (pid = wait(&status) ) < 0) 
   {
      //perror("wait error");
   }

   //increases the global static variable child count if a child finishes
   if (WIFEXITED(status))
   {
      CHILD_COUNT++;
   }
}

//Checks to see that the two matrices can be multiplied correctly by checking that the # of elements 
//in each row is consistent, and that the number of columns in matrix A matches the number of rows in matrix B
//checkMatrix also stores the dimensions of the two matrices in the static variables COLS_A, ROWS_A, COLS_B, ROWS_B
int checkMatrix()
{
   int i = 0;
   for(i; i < MAX_CHARS;i++)      //Scans through all digits in matrix A
   {
      if(ROWS_A==0 && (digits[i][0]==-2 || digits[i][0]==-3))  //If we have not gone past the first row and we see a space or new line
         COLS_A++;                    //We increase the column count (space between elements, and the newline character adds +1 for the last element in the row)

      if(digits[i][0]==-3)            //When a newline digit is encountered...
         ROWS_A++;                    //increase the number of rows by 1
      else if(digits[i][0]==-4)       //When the "border" between the two matrices is encountered, break
         break;
   }
   i++;
   for(i; i < MAX_CHARS;i++)     //Repeats for matrix B
   { 
      if(ROWS_B==0 && (digits[i][0]==-2 || digits[i][0]==-3))
         COLS_B++;
      if(digits[i][0]==-3)
         ROWS_B++;
      else if(digits[i][0]==-1)
         break;
   }

   if(COLS_A!=ROWS_B)           //If columns of A do not match the rows of B we cannot multiply and return -1
      return -1;
   else
      return 1;
}

//Simple function to iterate through a two dimensional integer array and print all elements in proper
//order and spacing, provided we also have the dimensions of the matrix
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

//converts an array of digits constructed with storeDigits into integer form
//function requires the dimensions of the matrix that we are extracting from digits
//*if we are attempting to extract matrix B from the digit array (i.e, the second matrix stored)
//it is the caller's responsibility to ensure that the source pointer points to the 
//very first element of the second matrix
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

//Simple function to print out all the digits stored in the static global array digits
void printDigits()
{
   int i = 0;
   int cur_digit = digits[i][0];
   while(cur_digit != -1)  //While we have not reached the end of matrix data
   {
      if(cur_digit>-1) //Just print digit if it's part of an actual integer value
      {
         printf("%d", cur_digit);
         i++;
         cur_digit = digits[i][0];
      }
      else if(cur_digit == -5)
      {
         printf("-");
         i++;
         cur_digit = digits[i][0];
      }
      else if(cur_digit == -2)
      {
         printf(" ");
         i++;
         cur_digit = digits[i][0];
      }
      else if(cur_digit == -3)
      {
         printf("\n");
         i++;
         cur_digit = digits[i][0];
      }
      else
      {
         printf("*\n");
         i++;
         cur_digit = digits[i][0];
      }
   }
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
         if(digits[char_count-1][0]==-3) //If the previous character is also a newline, we must be at the division between matrices
         {
            digits[char_count][0] = -4;  //Sets the boundary character between matrices
            digits[char_count][1] = 0;   //Sets the place value to 0 as it's a special negative
         }
         else
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
