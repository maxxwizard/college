#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/shm.h>
int main(int argc, char *argv[])
{
   //checks to make sure that we have the required argument count
   if(argc!=8)
      _exit(1);

   //stores the arguments in named character arrays; not technically necessary
   //but helps keep track of what arguments are used in later parts of the code
   //instead of just referring to different elements of argv
   char *rowA, *colsA, *shareA_id, *colB, *colsB, *rowsB, *shareB_id, *share_id;
   rowA = argv[0];
   colsA = argv[1];
   shareA_id = argv[2];
   colB = argv[3];
   colsB = argv[4];
   rowsB = argv[5];
   shareB_id = argv[6];
   share_id = argv[7];
   
   //converts the shared memory keys from string to integer
   int aid, bid, sid;
   aid = atoi(shareA_id);
   bid = atoi(shareB_id);
   sid = atoi(share_id);

   //converts the matrices related arguments from string to integer
   int targetRow_A, columns_A, targetCol_B, columns_B, rows_B;
   targetRow_A = atoi(rowA);
   columns_A = atoi(colsA);
   targetCol_B = atoi(colB);
   columns_B = atoi(colsB);
   rows_B = atoi(rowsB);

   //attaches the shared memory segments for matrices A and B, and the product matrix using
   //the shared memory keys
   int *matrixA = shmat(aid, (void *) 0, 0);
   int *matrixB = shmat(bid, (void *) 0, 0);
   int *products = shmat(sid, (void *) 0, 0);
   int sum = 0, i, j;
 
   //performs the multiplication between the specified row in A and the specified column in B
   //by adding the individual multiplication pairs together until we get the complete dot product
   //of row A and column B
   for(i = 0; i < atoi(colsA);i++) 
   {                               
      int elemA = *(matrixA + columns_A*targetRow_A + i); //dereferences the element from matrix A
      int elemB = *(matrixB + targetCol_B + i*columns_B); //dereferences the element from matrix B
      sum += elemA*elemB;  //multiplies the elements together and adds that product to the running sum
   }
 
   //prints a message informing the user which row and column we are multiplying together and what the dot product is
   printf("Row %d in A * Col %d in B = %d\n", targetRow_A+1, targetCol_B+1, sum);
  
   //stores the dot product as the appropriate element in the product matrix
   *(products + targetRow_A*columns_B + targetCol_B) = sum;

   //detaches the shared memory segments
   shmdt(matrixA);
   shmdt(matrixB);
   shmdt(products);
  
   //exits the child safely
   _exit(0);
}
