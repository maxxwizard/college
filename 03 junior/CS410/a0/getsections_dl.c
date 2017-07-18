/************************
 * Matthew Huynh
 * CS410 - West
 * February 16, 2010
 * Primer (Assignment 0)
 ************************/
 
#include <stdio.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <sys/stat.h>

int main (int argc, char *argv[])
{
	const int STATS_MODE = 1;

	if (argc != 3) {
		printf("this program takes in two parameters: <binary_filename> <RTLD_LAZY | RTLD_NOW>\n");
	} else {
		
		FILE * pSTATS;
		if (STATS_MODE) {
			// open the STATS file for appending
			pSTATS = fopen("./stats", "aw+");
			printf("\nstats file opened for appending...\n");
		}

		// determine the dynamic library symbol resolving mode
		int dlopenMode;
		if (strcmp(argv[2], "RTLD_LAZY") == 0) {
			dlopenMode = RTLD_LAZY;
		} else if (strcmp(argv[2], "RTLD_NOW") == 0) {
			dlopenMode = RTLD_NOW;
		}

		// run program
		#define rdtsc(x)	__asm__ __volatile__("rdtsc \n\t" : "=A" (*(x)))
		unsigned long long start, finish;
		void* lib_handle;
		int i;
		// load the library, while benchmarking it
		rdtsc(&start);
		lib_handle = dlopen("./libobjdata.so", dlopenMode);
		rdtsc(&finish);
		if (!lib_handle) {
			bfd_perror(dlerror());
			return 1;
		}
					
		// calulate time in seconds from the cycle counter
		// kanga.bu.edu has a Core 2 Duo 3000.00 MHz = 3000000 Hz (cycles per second)
		double elapsedTime = (finish-start) / 3000000.0;
		
		// write result to screen and stats file
		if (STATS_MODE) {
			printf("%.5fs\n", elapsedTime);
			
			if (fprintf(pSTATS, "%.5f\n", elapsedTime) < 0) {
				bfd_perror("write to STATS file failed\n");
				return 1;
			}
				
			// close STATS file
			fclose(pSTATS);
			printf("stats file closed...\n");
		}
		
		// call DisplaySectionTable() in the shared library
		void (*lib_func)();
		const char *error;
		lib_func = dlsym(lib_handle, "DisplaySectionTable");
		error = dlerror();
	    if (error) {
			bfd_perror(error);
			return 1;
    	}
		(*lib_func)(argv[1]); // call the shared library function	
		
		// close the library
		dlclose(lib_handle);
	}
}
