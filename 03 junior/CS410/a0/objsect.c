/************************
 * Matthew Huynh
 * CS410 - West
 * February 16, 2010
 * Primer (Assignment 0)
 ************************/
 
#include "bfd.h"
#include <unistd.h>
#include <stdio.h>

void DisplaySectionInfo(bfd *abfd, asection *sect, void *obj)
{
	printf("%4d %-18s  %08x  %08x  %016x\n", sect->index, sect->name, (unsigned int)sect->rawsize, (unsigned int)sect->size, (unsigned int)sect->vma);
}

void DisplaySectionTable(const char *filepath)
{
		// "initialize magical internal data structures"
		bfd_init();

		// open file
		struct bfd *abfd;
		abfd = bfd_openr(filepath, "elf64-x86-64"); //elf64-x86-64 on lab machines (though it doesn't matter?)
		if (bfd_get_error() != 0) {
			bfd_perror("Error opening file");
			return;
		}
		
		// check file format is binary object
		if (!bfd_check_format(abfd, bfd_object)) {
			bfd_perror("Format is not binary object");
			return;
		}
		
		// display info on each section
		printf("\nSections:\n\n");
		printf(" Idx Name                rSize     cSize     VMA     \n");
		bfd_map_over_sections(abfd, &DisplaySectionInfo, NULL);
		printf("\n");
}
