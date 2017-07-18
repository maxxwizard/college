/************************
 * Matthew Huynh
 * CS410 - West
 * February 16, 2010
 * Primer (Assignment 0)
 ************************/
 
#include <stdio.h>

/* link to objsym.c's function */
extern void DisplaySymbolTable();

int main (int argc, char *argv[]) {

	if (argc != 2) {
		printf("this program takes in one parameter: <binary_filename>\n");
	} else {
		DisplaySymbolTable(argv[1]);
	}

}
