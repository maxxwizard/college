# Matthew Huynh
# Christian Ling
# CS410 - West
# Assignment 2

CC = cc
LD = cc
UNAME := $(shell uname)

INCLUDES = -I. -Ireadline

RL_LIBRARY_VERSION = 6.1
ifeq ($(UNAME), Linux)
READLINE_LIB = -lreadline
CCFLAGS = -g
endif
ifeq ($(UNAME), Darwin)
READLINE_LIB = readline/libreadline.a
CCFLAGS = -g -DREADLINE_LIBRARY -DRL_LIBRARY_VERSION='"$(RL_LIBRARY_VERSION)"' $(INCLUDES)
endif

TERMCAP_LIB = -ltermcap

LIBS = $(TERMCAP_LIB) $(READLINE_LIB)

# flags to pass to the C compiler
LDFLAGS = -g $(LIBS)

# top-level rule
all: myshell matmult_p multiply matformatter test_matrix

# myshell executable)
myshell: myshell.o
	$(LD) $(LDFLAGS) -o $@ myshell.o
# matrix programs

test_matrix: test_matrix.c
	gcc -o test_matrix test_matrix.c

matmult_p: matmult_p.c
	gcc -o matmult_p matmult_p.c

multiply: multiply.c
	gcc -o multiply multiply.c

matformatter: matformatter.c
	gcc -o matformatter matformatter.c

# compile C source files into object files
.c.o:
	$(CC) $(CCFLAGS) -c $<

#myshell.o: readline/readline.h

# clean the directory of object files
clean:
	-rm -f *.o

# clean the directory of object files and executables
cleanall:
	-rm -f *.o
	-rm myshell
	-rm test_matrix
	-rm matmult_p
	-rm multiply
	-rm matformatter
