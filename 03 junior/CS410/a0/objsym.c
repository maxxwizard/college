/************************
 * Matthew Huynh
 * CS410 - West
 * February 16, 2010
 * Primer (Assignment 0)
 ************************/
 
#include <stdio.h>
#include "bfd.h"

void DisplaySymbolInfo(asymbol *sym)
{
	printf("%016x  %s\n", sym->section->vma + sym->value, sym->name);
}

void DisplaySymbolTable(const char *filepath)
{
	// "initialize magical internal data structures"
	bfd_init();

	// open file
	struct bfd *abfd;
	abfd = bfd_openr(filepath, "elf64-x86-64"); //elf64-x86-64 on lab machines (though it doesn't matter?)
	if (bfd_get_error() != 0) {
		printf("Error opening file '%s': %s\n", filepath, bfd_errmsg(bfd_get_error()));
		return;
	}
	
	// check file format is binary object
	if (!bfd_check_format(abfd, bfd_object)) {
		printf("format is not binary object: %s\n", bfd_errmsg(bfd_get_error()));
		return;
	}
	
	// display info on each symbol
	long storage_needed;
	asymbol **symbol_table;
	long number_of_symbols;
	long i;
	
	storage_needed = bfd_get_symtab_upper_bound(abfd);
	
	if (storage_needed < 0) {
		printf("Error reading symbol table in file '%s': %s", filepath, bfd_errmsg(bfd_get_error()));
		return;
	}
	
	if (storage_needed == 0) {
		printf("No symbols found in file '%s'.", filepath);
		return;
	}
	
	symbol_table = xmalloc (storage_needed);
	number_of_symbols = bfd_canonicalize_symtab(abfd, symbol_table);
	
	if (number_of_symbols < 0) {
		printf("Error reading symbol table in file '%s': %s", filepath, bfd_errmsg(bfd_get_error()));
		return;
	}
	
	for (i = 0; i < number_of_symbols; i++)
		DisplaySymbolInfo(symbol_table[i]);
}
