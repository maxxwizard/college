# Matthew Huynh
# CS410 - West
# Assignment 1

# compiler/linker
CC = cc
LD = cc

# flags to to compile object files that can be used in a dynamic library
CFLAGS = -fPIC

# flags to pass to the C compiler
FLAGS = -g
					
# top-level rule
all: finds

# finds executable
finds: main.o
	$(LD) $^ $(LIBS) $(FLAGS) -o $@
	
# compile C source files into object files
%.o: %.c
	$(CC) $(CFLAGS) -c $<

# clean the directory of object files
clean:
	-rm -f *.o
	
# clean the directory of object files and executables
cleanall:
	-rm -f *.o
	-rm finds
