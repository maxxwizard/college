# Matthew Huynh
# CS410 - West
# February 16, 2010
# Primer (Assignment 0)

# This makefile compiles the getsections and getsyms standalone executables,
# as well as the libobjdata.a static library. It then links the two
# executables to the static library.

# adopted from: http://users.actcom.co.il/~choo/lupg/tutorials/libraries/static-lib/Makefile

# compiler/linker
CC = cc
LD = cc

# flags to to compile object files that can be used in a dynamic library
CFLAGS = -fPIC

# archiver and indexer
AR = /usr/bin/ar

# static library's object files
LIB_OBJS = objsect.o objsym.o

# static library's archive file
S_LIB_FILE = libobjdata.a

# dynamic library's filename
DL_LIB_FILE = libobjdata.so

# libraries to use when linking to the executables
LIBS = -L. -lobjdata -lbfd-2.17.50.0.6-12.el5 -ldl

# flags to pass to the C compiler
FLAGS = -liberty

# flags for static library-linked executables
STATIC_FLAGS = -Bstatic
					
# top-level rule
all: $(S_LIB_FILE) getsections getsyms $(DL_LIB_FILE) getsections_dl getsyms_dl

# getsections executable
getsections: getsections.o
	$(LD) $^ $(LIBS) $(FLAGS) $(STATIC_FLAGS) -o $@
	
# getsyms executable
getsyms: getsyms.o
	$(LD) $^ $(LIBS) $(FLAGS) $(STATIC_FLAGS) -o $@

# getsections_dl executable
getsections_dl: getsections_dl.o
	$(LD) -o $@ $^ $(LIBS) $(FLAGS)
	
# getsyms_dl executable
getsyms_dl: getsyms_dl.o
	$(LD) -o $@ $^ $(LIBS) $(FLAGS)

# libobjdata.a static library
$(S_LIB_FILE): $(LIB_OBJS)
	$(AR) rcs $(S_LIB_FILE) $(LIB_OBJS)

# libobjdata.so dynamic library
$(DL_LIB_FILE): $(LIB_OBJS)
	$(LD) -shared -o $(DL_LIB_FILE) $(LIB_OBJS)

# compile C source files into object files
%.o: %.c
	$(CC) $(CFLAGS) -c $<

# clean the directory of object files
clean:
	-rm -f *.o
	
# clean the directory of object files, libraries, executables
cleanall:
	-rm -f *.o
	-rm $(S_LIB_FILE)
	-rm $(DL_LIB_FILE)
	-rm getsections
	-rm getsyms
	-rm getsections_dl
	-rm getsyms_dl
