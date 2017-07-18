# Matthew Huynh
# CS410 - West
# Assignment 3

CC = cc
LD = cc

CCFLAGS = -g

LIBS = -lm

# flags to pass to the C compiler
LDFLAGS = -g $(LIBS)

# top-level rule
all: mychecksum

# mychecksum executable
mychecksum: mychecksum.o
	$(LD) $(LDFLAGS) -o $@ $<

# compile C source files into object files
.c.o:
	$(CC) $(CCFLAGS) -c $<
	
# clean the directory of object files
clean:
	-rm -f *.o

# clean the directory of object files and executables
cleanall:
	-rm -f *.o
	-rm mychecksum
	-rm temp1
	-rm temp2