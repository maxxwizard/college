/*
 * Matthew Huynh
 * CS410 - West
 * Assignment 1
 */

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <limits.h>
#include <string.h>

static int DEBUG = 0;

/* function type that is called for each filename */
typedef int Myfunc (const char *, const struct stat *, int);

/* function prototypes */
static Myfunc	myfunc;
static int		myftw (char *, Myfunc *);
static int		dopath (Myfunc *);
static void 	searchInFile(const char *pathname);
static int 		checkFileTextFormat(const char *pathname);
static void 	outputMatchesInFile(const char *pathname);
char * 			substring (const char* str, size_t begin, size_t len);
char * 			path_alloc (int * size);

/* global variables */
static long	nreg, nlnk, ndir; 		/* count of regular and directory files */
static char *svalue = NULL; 	/* search string */
static char *fvalue = NULL;
static int 	lflag = 0;
static char symlinkedDirs[1000000];
static char filesSearched[1000000];

/* path alloc stuff */
#ifdef PATH_MAX
static int pathmax = PATH_MAX;
#else
static int pathmax = 0;
#endif
#define PATH_MAX_GUESS 1024

int main (int argc, char *argv[])
{
	int ret;
	char *pvalue = NULL;
	int c;
	
	// Get the options and their arguments.
	while ((c = getopt (argc, argv, "p:f:ls:")) != -1)
		switch (c)
		{
			case 'p':
				pvalue = optarg;
				break;
			case 'f':
				if (strcmp(optarg, "c") != 0 && strcmp(optarg, "h") != 0 && strcmp(optarg, "S") != 0) {
					fprintf(stderr, "Option -f requires an argument of 'c', 'h', or 'S'.\n");
					return 1;
				}
				fvalue = optarg;
				break;
			case 'l':
				lflag = 1;
				break;
			case 's':
				svalue = optarg;
				// check for bad regular expressions
				/* cases where regex starts with a control char */
				if (*svalue == '*' || *svalue == '?') {
					fprintf(stderr, "regex argument of '%s' to -s option is malformed.\n", svalue);
					return 1;
				}
				char validChars[] = "abcdefghijklmnopqrstuvwxyz1234567890.*?";
				char* ptr = svalue;
				/* cases where regex contains characters other than alphanumeric or '.', '*', or '?' */
				while (*ptr != '\0') { 
					char* firstChar = substring(ptr, 0, 1);
					if (strchr(validChars, *firstChar) == NULL) {
						fprintf(stderr, "regex argument of '%s' to -s option is malformed.\n", svalue);
						return 1;
					}
					ptr++;
				}
				break;
			case '?':
				if (optopt == 'p' || optopt == 'f' || optopt == 's')
					fprintf(stderr, "Option -%c requires an argument.\n", optopt);
				else if (isprint (optopt)) {
					fprintf(stderr, "Unknown option '-%c'.\n", optopt);
					return 1;
				}
			default:
				abort ();
		}
		
	if (pvalue == NULL || svalue == NULL)
	{
		fprintf(stderr, "usage: finds -p pathname [-f c|h|S] [-l] -s searchString\n");
		return 1;
	}
	
	printf("\n"); // linebreak after program execution
	
	// Print the options. (debugging)
	if (DEBUG)
		printf ("pvalue = %s, fvalue = %s, lflag = %d, svalue = %s\n\n", pvalue, fvalue, lflag, svalue);
	
	// search in initial directory
	ret = myftw(pvalue, myfunc);
	// search in symlinked directories
	char symlinkedDir [PATH_MAX];
	char * ptr;
	ptr = strtok (symlinkedDirs, ":");
	while (ptr != NULL) {
		if (DEBUG)
			printf("searching in symlinked dir '%s'\n", ptr);
		myftw(ptr, myfunc);
		ptr = strtok(NULL, ":");
	}
	
	// Print count of regular and directory files. (debugging)
	if (DEBUG) {
		printf("\nregular files  : %ld\n", nreg);
		printf("symbolic links : %ld\n", nlnk);
		printf("directory files: %ld\n", ndir);
	}
	
	printf("\n"); // linebreak before next shell prompt
	
	exit(ret);
}

#define FTW_F		1 	/* file other than directory */
#define FTW_D		2 	/* directory */
#define FTW_DNR	3 	/* directory that can't be read */
#define FTW_NS		4	/* file that we can't stat */

/* contains full pathname for every file */
static char *fullpath;

static int myftw (char *pathname, Myfunc *func)
{
	int len;
	fullpath = path_alloc(&len);
	strncpy(fullpath, pathname, len);
	fullpath[len-1] = 0;
	return (dopath(func));
}

static int dopath (Myfunc* func)
{
	struct stat 	statbuf;
	struct dirent 	*dirp;
	DIR				*dp;
	int				ret;
	char				*ptr;
	
	if (lstat(fullpath, &statbuf) < 0)	/* stat error */
		return (func(fullpath, &statbuf, FTW_NS));
	
	if (S_ISDIR(statbuf.st_mode) == 0) /* not a directory */
		return (func(fullpath, &statbuf, FTW_F));
		
	// It's a directory. First call func() for the directory,
	// then process each filename in the directory.
	if ((ret = func(fullpath, &statbuf, FTW_D)) != 0)
		return(ret);
	
	ptr = fullpath + strlen(fullpath); /* point to end of fullpath */
	*ptr++ = '/';
	*ptr = 0;
	
	if ((dp = opendir(fullpath)) == NULL) /* can't read directory */
		return (func(fullpath, &statbuf, FTW_DNR));
	
	while ((dirp = readdir(dp)) != NULL) {
		if (strcmp(dirp->d_name, ".") == 0 || strcmp(dirp->d_name, "..") == 0)
			continue;	/* ignore dot and dot-dot */
		
		strcpy(ptr, dirp->d_name);		/* append name after slash */
		
		if ((ret = dopath(func)) != 0)	/* recursive */
			break;
	}
	
	ptr[-1] = 0;	/* erase everything from slash onwards */
	
	if (closedir(dp) < 0)
		fprintf(stderr, "Can't close directory %s", fullpath);
	
	return(ret);
}

static int myfunc (const char *pathname, const struct stat *statptr, int type)
{
	switch (type) {
	case FTW_F:
		switch (statptr->st_mode & S_IFMT) {
		case S_IFREG:
			nreg++;
			outputMatchesInFile(pathname); /* output matches to svalue in this regular file */
			break;
		case S_IFLNK:
			nlnk++;
			if (lflag == 1) {
				char symlinkPath [PATH_MAX];
				struct stat statbuf;
				// read the symlink
				if (readlink(pathname, symlinkPath, PATH_MAX) < 0)
					fprintf(stderr, "unable to read symlink '%s'\n", pathname);			
				if (DEBUG)
					printf("symlink '%s' contains: '%s'\n", pathname, symlinkPath);
				// if symlink is a directory, record it so we can traverse it later
				if (lstat(symlinkPath, &statbuf) < 0) {
					if (DEBUG) fprintf(stderr, "can't lstat symlink '%s'\n", pathname);
					break;
				}
				if (S_ISDIR(statbuf.st_mode)) {
					if (DEBUG) printf("recording symlinked dir '%s'\n", symlinkPath);
					strcat(symlinkedDirs, symlinkPath);
					strcat(symlinkedDirs, ":");
				} else {// otherwise process it like a file
					if (DEBUG) printf("calling outputMatchesInFile on '%s'\n", symlinkPath);
					outputMatchesInFile(symlinkPath);
				}
				
			}
			break;
		case S_IFDIR:
			fprintf(stderr, "%s is a directory so it should have type = FTW_D\n", pathname);
		}
		break;
	
	case FTW_D:
		ndir++;
		break;
		
	case FTW_DNR:
		fprintf(stderr, "Can't read directory %s\n", pathname);
		break;
		
	case FTW_NS:
		fprintf(stderr, "Stat error for %s\n", pathname);
		break;
		
	default:
		fprintf(stderr, "Unknown type %d for pathname %s\n", type, pathname);
	}
	
	return(0);
}

/* Prints out all matches to global svalue in a file, followed by the pathname. */
static void outputMatchesInFile(const char *pathname)
{
	if (strstr(filesSearched, pathname) == NULL) {
	
		strcat(filesSearched, ":");
		strcat(filesSearched, pathname);

		char extension [2]; // should be [c, '\0'] or something of the sort
	
		// If fvalue is set, only check files ending in that extension.
	
		if (fvalue != NULL) {
			if (strrchr(pathname, '.') != NULL)
				strncpy(extension, substring(strrchr(pathname, '.'), 1, 1), 1); /* grab the extension (without dot) */
			extension[1] = '\0'; /* null-terminate the extension string */
		}
		
		if (fvalue == NULL || strstr(fvalue, extension) != NULL) {
			if (checkFileTextFormat(pathname) == 1) {
				if (DEBUG)
					printf("\n%-21s %s\n", "regular text file:", pathname);
				FILE * pFile;
				pFile = fopen(pathname, "rt");
				if (pFile == NULL)
					perror("Error opening regular file.");

				char mystring [LINE_MAX] = "";
				char output [LINE_MAX] = "";

				while (fgets(mystring, LINE_MAX, pFile) != NULL) {	/* grab line from file */
					if (strstr(mystring, svalue))			/* if search string found */
						strcat(output, mystring); 					/* add line to output */
				}

				if (strlen(output) != 0)
					printf("%s\n%s\n", output, pathname);		/* output matching lines and pathname */

				fclose(pFile);
			} else {
				if (DEBUG)
					printf("\n%-21s %s\n", "regular binary file:", pathname);
			}
		}
	}
}

/* Returns 1 if a file is deemed text format (as opposed to binary). */
static int checkFileTextFormat(const char *pathname)
{
	FILE * pFile;
	pFile = fopen(pathname, "rt");
	if (pFile == NULL)
		perror("Error opening regular file.");

	int i;
	int c;
	for (i = 0; i < 100; i++) {
		c = fgetc (pFile);
		
		if (c == EOF || c <= 127) // check if c is within the first 128 ASCII values
			continue;
		else
			return 0;
	}
	
	fclose(pFile);
	
	return 1;
}

char* substring (const char* str, size_t begin, size_t len) 
{ 
	if (str == 0 || strlen(str) == 0 || strlen(str) < begin || strlen(str) < (begin+len)) 
		return 0;

	return strndup(str + begin, len); 
}

char* path_alloc (int * size)
{
	char *ptr;
	int errno;

	if (pathmax == 0) {
		errno = 0;
		if( (pathmax = pathconf("/", PATH_MAX)) < 0) {
			if (errno == 0)
				pathmax= PATH_MAX_GUESS;
			else
				printf("pathconf error for _PC_PATH_MAX");
		}
		else {
			pathmax++;
		}
	}
	
	if ((ptr = malloc(pathmax + 1)) == NULL)
		printf("malloc error for pathname");
		
	if (size != NULL )
		*size = pathmax + 1;
		
	return(ptr);
}
