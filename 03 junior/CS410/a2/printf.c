#include <stdio.h>
#include <stdlib.h>

int main()
{
  int i;
  char test[5];
  for (i = 1; i < 5; i++) {
    sprintf(test, "%d", i);
    printf("test: %s\n", test);
  }
}
