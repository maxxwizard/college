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

#define BUFF_SIZE 512
#define MAX_INTS 4096 


static int STORAGE[MAX_INTS];

static int ROWS_A = 0;             //Static global variables that store the dimensions of the matrices
static int COLS_A = 0;

static int ROWS_B = 0;
static int COLS_B = 0;

static int MATA_ID = 0;
static int MATB_ID = 0;
static int PROD_ID = 0;

static int CHILD_COUNT = 0;       //Keeps track of the completed child process count via signals
static int MAX_CHILDREN = 0;      //The number of processes we expect to complete in order to get product


int convertDigitsToInt(int, int, int *, int *);
int printInts(int, int, int *);
int getData(FILE *);
int fillMatrices(int *, int *);
static void sig_chld(int);

int main(int argc, char *argv[])
{
   if(signal(SIGCHLD, sig_chld) == SIG_ERR)        //Registers the signal handler for children
      perror("Can't register sig_chld handler");
   
   getData(stdin);
   
   //allocates memory for matrix A, matrix B, and the product matrix
   MATA_ID = shmget(IPC_PRIVATE, sizeof(int)*ROWS_A*COLS_A, 0644 );
   MATB_ID = shmget(IPC_PRIVATE, sizeof(int)*ROWS_B*COLS_B, 0644 );
   PROD_ID =  shmget(IPC_PRIVATE, sizeof(int)*ROWS_A*COLS_B, 0666 );
   
   //attaches the shared memory to pointers
   
   int *pointA =  shmat(MATA_ID, (void *)0, 0);
   int *pointB = shmat(MATB_ID, (void *)0, 0);
   int *product = shmat(PROD_ID, (void *)0, 0);
   
   if(fillMatrices((int *) pointA, (int *) pointB)!=1)
   {
      perror("Matrices can't be multiplied");
      exit(1);
   }

   //calculates the expected numbe of children processes that will be created by multiplying the
   //dimensions of the product matrix together
   MAX_CHILDREN = ROWS_A*COLS_B;
   //allocates the shared memory for matrix A, matrix B, and the product matrix

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
            sprintf(share_id, "%d", PROD_ID);
            sprintf(shareA_id, "%d", MATA_ID);
            sprintf(shareB_id, "%d", MATB_ID);

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

               //printf("\nAll children processes done; printing the result matrix:\n");
               printInts(ROWS_A, COLS_B, (int *) product);
 
               //detaches the shared memory
               shmdt(pointA);
               shmdt(pointB);
               shmdt(product);
            }
         }
      }
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

//Takes the digits read in from a passed file descriptor name (*fp) and stores them in
//the static two dimensional array "digits".
int getData(FILE *fp)
{
   int store_index = 0;
   
   char buffer [BUFF_SIZE];
   while(fgets(buffer, BUFF_SIZE, fp)!=(char *)0)
   {
     if(buffer[0]=='\n')
        break;
     else
     {
        ROWS_A++;
        char * integers;
        integers = strtok(buffer, " \n");
        while(integers != NULL)
        {
           if(ROWS_A==1)
              COLS_A++;
           STORAGE[store_index] = atoi(integers);
		   store_index++;
           integers = strtok(NULL, " \n");
        }
      }
   }

   while(fgets(buffer, BUFF_SIZE, fp)!=(char *)0)
   {
      ROWS_B++;
      char * integers;
      integers = strtok(buffer, " \n");
      while(integers != NULL)
      {
         if(ROWS_B==1)
            COLS_B++;
         STORAGE[store_index] = atoi(integers);
		 store_index++;
         integers = strtok(NULL, " \n");
      }
   }

   if(COLS_A==ROWS_B)
      return 1;
   else
      return -1;
}

int fillMatrices(int *matA, int *matB)
{
   int i,j;
   for(i = 0; i < ROWS_A; i++)
      for(j = 0; j < COLS_A; j++)
      {
	     *(matA + i*COLS_A + j) = STORAGE[(i*COLS_A)+j];
      }

   for(i = 0; i < ROWS_B; i++)
      for(j = 0; j < COLS_B; j++)
      {
	     *(matB + i*COLS_B + j) = STORAGE[(ROWS_A*COLS_A)+(i*COLS_B)+j];
      }

   return 1;
}
