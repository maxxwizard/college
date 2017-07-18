#include <unistd.h>
#include "iofunctions.h"

/* returns the string length (excluding null terminator) */
int strlen_c (const char *str)
{
	const char *s;
	s = str;
	while (*s)
		s++;
	return (s - str);
}

/* returns 0 on success, -1 on failure (more or less chars were written) */
int print_c (int filedes, const char *str)
{
	int length = strlen_c(str);
		
	if (write(filedes, str, length) != length) {
		write(2, "Error writing entire string to STDOUT\n", 38);
		return -1;
	}
	
	return 0;
}

/* returns -1 on not equal, 0 on equal */
int strEqual_c(const char *str1, const char *str2)
{
	while (*str1 == *str2++)
		if (*str1++ == 0)
			return 0;
	return -1;
}

/* 
 * Returns a pointer to the padded string.
 * padCharacter: character to pad with
 * padSide:      'l' for padding on left, 'r' for padding on right
 */
/*
char *paddedStr (int numOfChars, const char *padCharacter, const char *padSide, const char *str)
{
	int length = strlen(str);
	char result[numOfChars+1]; // +1 for null terminator
	int i, j;
	
	if (*padSide == 'r') {
		for (i = 0; i < length; i++) // copy over the string (minus null-terminator)
			result[i] = str[i];
		for (i = length; i < numOfChars; i++) // pad the string
			result[i] = *padCharacter;
	} else if (*padSide == 'l') {
		for (i = 0; i < (numOfChars-length); i++) // pad from index 0 up to the difference
			result[i] = *padCharacter;
		for (j = 0; i < numOfChars; i++, j++) // copy over the string (minus null-terminator)
			result[i] = str[j];
	}
	result[numOfChars] = '\0'; // null-terminate the string
	return result; // return the result
}
*/
