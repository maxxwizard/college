/************************
 * Matthew Huynh
 * CS410 - West
 * February 16, 2010
 * Primer (Assignment 0)
 ************************/

#include <stdio.h>

/* link to objsect.c's function */
extern void DisplaySectionTable();

int main (int argc, char *argv[])
{
	if (argc != 2) {
		printf("this program takes in one parameter: <binary_filename>\n");
	} else {
		DisplaySectionTable(argv[1]);
	}
}



