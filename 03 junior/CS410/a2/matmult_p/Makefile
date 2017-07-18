M_OBJ = matmult_p test_matrix matformatter multiply

all : matformatter.c test_matrix.c matmult_p.c multiply.c
	gcc -o matformatter matformatter.c
	gcc -o test_matrix test_matrix.c
	gcc -o matmult_p matmult_p.c
	gcc -o multiply multiply.c
formatter : matformatter.c
	gcc -o matformatter matformatter.c
test : test_matrix.c
	gcc -o test_matrix test_matrix.c
mult_p : matmult_p.c
	gcc -o matmult_p matmult_p.c
multiply : multiply.c
	gcc -o multiply multiply.c
clean :
	rm $(M_OBJ) 
