/*
 * Matthew Huynh
 * CS410 - West
 * Assignment 3
 */

#include <string.h>
#include <stdio.h>
#include <sys/stat.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <math.h>

#define DEBUG 0

int main (int argc, char *argv[])
{
	int			fdin, fdout;
	char		*src, *dst;
	struct stat statbuf;
	
	if (argc != 3) {
		fprintf(stderr, "usage: %s <inputFile> <outputFile>\n", argv[0]);
		exit(EXIT_FAILURE);
	}
	
	if ((fdin = open(argv[1], O_RDONLY)) < 0)
		fprintf(stderr, "can't open %s for reading\n", argv[1]);
	
	if ((fdout = open(argv[2], O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)) < 0)
		fprintf(stderr, "can't creat %s for writing\n", argv[2]);
	
	// get size of input file
	if (fstat(fdin, &statbuf) < 0)
		fprintf(stderr, "fstat error\n");
	
	// set size of output file
	// each 26-char line turns into a 31-char line [25 digits, space, 1-char checksum, space, 2-char count, newline char]
	int fdoutBytes = ceil(statbuf.st_size / 26.0) * 31;
	if (lseek(fdout, fdoutBytes-1, SEEK_SET) == -1)
		fprintf(stderr, "lseek error\n");
	if (write(fdout, "", 1) != 1)
		fprintf(stderr, "write error\n");
	
	if ((src = mmap(0, statbuf.st_size, PROT_READ, MAP_SHARED, fdin, 0)) == MAP_FAILED)
		fprintf(stderr, "mmap error for input\n");
	
	if ((dst = mmap(0, fdoutBytes, PROT_READ | PROT_WRITE, MAP_SHARED, fdout, 0)) == MAP_FAILED)
		fprintf(stderr, "mmap error for output\n");
	
	char checksum = '0';
	unsigned short int count = 0;
	// iterate through entire file
	while (*src != '\0') {
		// if we encounter a 1
		if (*src == '1') {	
			checksum = (checksum == '0') ? '1' : '0'; // we flip the checksum bit
			count++; // increment the count
		}
		// else if we encounter a newline
		else if (*src == '\n') { 
			// we print the checksum bit and count to output
			*dst = ' ';
			dst++;
			*dst = checksum;
			dst++;
			*dst = ' ';
			dst++;
			dst += sprintf(dst, "%.2hu", count);
			//reset the checksum bit and count
			checksum = '0';
			count = 0;
		}
		// copy the character (digit or newline)
		memcpy(dst, src, 1);
		if (DEBUG) printf("'%c' (%p)\n", *src, src);
		dst++;
		src++;
		
	}
	
	//msync(dst, statbuf.st_size, MS_SYNC);
	
}
