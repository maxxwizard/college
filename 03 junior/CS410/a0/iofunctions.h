#ifndef IOFUNCTIONS_H_
#define IOFUNCTIONS_H_
int strlen_c (const char *str);
int print_c (int filedes, const char *str);
int strEqual_c (const char *str1, const char *str2);
char *paddedStr (int numOfChars, const char *padCharacter, const char *padSide, const char *str);
#endif /* IOFUNCTIONS_H_ */
