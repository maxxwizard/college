## MATTHEW HUYNH
## CS210 - MATTA
## OCTOBER 22, 2008 
## 
## Start of file range.s
##
## Question:
## "nums" is an array of "N" words.
## Count the number of elements and
## add up the values of elements in "nums"
## whose value is greater than 40 and less than 90,
## and print out their number and sum
##
## Output format must be:
## "count = 4"
## "sum = 67"

##################################################
## text segment ##
##################################################

	.text
	.globl __start

__start:

# Any changes above this line will be discarded by
# mipsmark. Put your answer between dashed lines.
#-------------- start cut -----------------------

	la $s0, nums		# $s0 = &array[0]
	lw $s1, N		# $s1 = size of array
	sll $t0, $s1, 2		# $t0 = 4 * size of array
	add $s2, $s0, $t0 	# $s2 = &array[size]
	add $s4, $zero, $s0	# $s4 = &array[0]

loop:	lw $t7, 0($s4)		# $t7 = array[i]

	slti $t1, $t7, 90    	# if array[i] > 90
	beq $t1, $zero, fail	# then go to fail
	slti $t2, $t7, 40	# if array[i] < 40
	bne $t2, $zero, fail	# then go to fail

	addi $s3, $s3, 1	# increment counter
	add $s5, $s5, $t7	# add value onto sum

	j fail			# go onto next element in array

fail:	addi $s4, $s4, 4	# &array[i] = &array[i+4]
	slt $t3, $s4, $s2	# if (&array[i] < &array[size])
	beq $t3, $zero, finish	# then go to finish
	j loop			# restart loop

finish:	la $a0, ans1		# load "count = " into $a0
	li $v0, 4		# print it
	syscall

	move $a0, $s3		# load counter into $a0
	li $v0, 1		# print it
	syscall

	la $a0, newLine		# load new line into $a0
	li $v0, 4		# print it
	syscall

	la $a0, ans2		# load "sum = " into $a0
	li $v0, 4		# print it
	syscall

	move $a0, $s5		# load sum into $a0
	li $v0, 1		# print it
	syscall

	li $v0, 10		# exit program
	syscall

	.data
newLine:.asciiz "\n"

#-------------- end cut -----------------------
# Any changes below this line will be discarded by
# mipsmark. Put your answer between dashed lines.

##################################################
## data segment ##
##################################################

	.data
nums: 	.word 3,68,2,60,2000,51,99,102
N: 	.word 8
ans1: 	.asciiz "count = "
ans2: 	.asciiz "sum = "

