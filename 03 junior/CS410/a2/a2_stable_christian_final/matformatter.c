#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>

#define MAX_INTS 4096
#define BUFF_SIZE 512

static int STORAGE[MAX_INTS];

static int ROWS_A = 0;             //Static global variables that store the dimensions of the matrices
static int COLS_A = 0;

int getData(FILE *);
int fillMatrix(int *);

int main(int argc, char *argv[])
{
   getData(stdin);
   printTranspose(ROWS_A, COLS_A, (int *)STORAGE);
   exit(0);
}

int printTranspose(int rows, int cols, int *matrix)
{
   int i, j;
   for(i = 0; i < COLS_A; i++)
   {
      for(j = 0; j < ROWS_A; j++)
	     printf("%d ", *(matrix + j*COLS_A + i));
       printf("\n");
   }
    return 1;	
}

int getData(FILE *fp)
{   
   int store_index = 0;
   
   char buffer [BUFF_SIZE];
   while(fgets(buffer, BUFF_SIZE, fp)!=(char *)0)
   {
     ROWS_A++;
     char * integers;
     integers = (char *)strtok(buffer, " \n");
     while(integers != NULL)
     {
       if(ROWS_A==1)
          COLS_A++;
       STORAGE[store_index] = atoi(integers);
	   store_index++;
       integers = (char *) strtok(NULL, " \n");
     }
     
   }
   return 1;
}

