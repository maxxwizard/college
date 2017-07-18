## MATTHEW HUYNH
## CS210 - MATTA
## NOVEMBER 19, 2008 
## PROGRAMMING ASSIGNMENT #2
##
## Start of file quicksort.s

##################################################
## text segment ##
##################################################

	.text
	.globl __start

__start:
#------------------
# PRINT CURRENT ARRAY
#------------------
	la $a0, array		# $a0 = &array[0]
	la $t0, size		# $t0 = &size
	lw $a1, 0($t0)		# $a1 = size
	jal PrintArray		# call PrintArray method
#------------------
# PERFORM QUICKSORT
#------------------
	la $a0, array		# $a0 = &array[0]
	li $a1, 0			# $a1 = 0
	la $t0, size		# $t0 = &size
	lw $t1, 0($t0)		# $t1 = size
	addi $t1, $t1, -1	# $t1 = size-1
	move $a2, $t1		# $a2 = size-1
	jal Quicksort		# call Quicksort method
#------------------
# PRINT TEXT
#------------------
	la $a0, text		# print "\nfinal array:\n"
	li $v0, 4
	syscall
#------------------
# PRINT FINAL ARRAY
#------------------
	la $a0, array		# $a0 = &array[0]
	la $t0, size		# $t0 = &size
	lw $a1, 0($t0)		# $a1 = size
	jal PrintArray		# call PrintArray method
#------------------
# EXIT
#------------------
	li $v0, 10			# exit
	syscall

#-------------------------------------------------
# PrintArray - prints an array of strings to the console
# $a0 = char *v[0] (pointer to first element of array)
# $a1 = int n (size of array)
#-------------------------------------------------
PrintArray:
	addi $sp, $sp, -4	# malloc for 1 word
	sw $a0, 0($sp)		# preserve $a0
	move $t5, $a0		# $t5 = $a0
	li $t0, 0			# i = 0
L0:	slt $t1, $t0, $a1	# if (i < n)
	beq $t1, $zero, F0	# end loop if !(i < n)
		sll $t2, $t0, 2		# $t2 = i*4 (to get byte address)
		add $t3, $t5, $t2	# $t3 = &Array[i]
		lw $t4, 0($t3)		# $t4 = Array[i]
		move $a0, $t4		# print Array[i]
		li $v0, 4
		syscall
		la $a0, space		# print a space
		li $v0, 4
		syscall
		addi $t0, $t0, 1	# i++
	j L0				# restart loop
F0: la $a0, CLRF
	li $v0, 4
	syscall
	lw $a0, 0($sp)		# restore $a0
	addi $sp, $sp, 4	# dealloc for 1 word
	jr $ra
#-------------------------------------------------
# Swap - swaps two pointers in an array
# $a0 = char *v[0] (pointer to first element of array)
# $a1 = int i (element in array to be swapped)
# $a2 = int j (element in array to be swapped)
#-------------------------------------------------
Swap:
	addi $sp, $sp, -24	# save $t0-t5
	sw $t0, 0($sp)
	sw $t1, 4($sp)
	sw $t2, 8($sp)
	sw $t3, 12($sp)
	sw $t4, 16($sp)
	sw $t5, 20($sp)
	sll $t0, $a1, 2		# $t0 = byte address offset for element i
	sll $t1, $a2, 2		# $t1 = byte address offset for element j
	add $t2, $t0, $a0	# $t2 = i's byte address
	lw $t3, 0($t2)		# $t3 = i
	add $t4, $t1, $a0	# $t4 = j's byte address
	lw $t5, 0($t4)		# $t5 = j
	sw $t3, 0($t4)		# j = i
	sw $t5, 0($t2)		# i = j
	lw $t0, 0($sp)		# restore $t0-t5
	lw $t1, 4($sp)
	lw $t2, 8($sp)
	lw $t3, 12($sp)
	lw $t4, 16($sp)
	lw $t5, 20($sp)
	addi $sp, $sp, 24
	jr $ra
#-------------------------------------------------
# StrCmp - returns <0 if s<t, 0 if s==t, >0 if s>t
# $a0 = char *s (pointer to string)
# $a1 = char *t (pointer to string)
# $v0 = int (returned)
#-------------------------------------------------
StrCmp:
	addi $sp, $sp, -24	# save $t0-t5
	sw $t0, 0($sp)
	sw $t1, 4($sp)
	sw $t2, 8($sp)
	sw $t3, 12($sp)
	sw $t4, 16($sp)
	sw $t5, 20($sp)
	move $t0, $a0		# $t0 = byte address of s
	move $t1, $a1		# $t1 = byte address of t
L1:	lb $t2, 0($t0)		# $t2 = s[i]
	lb $t3, 0($t1)		# $t3 = t[i]
	sub $t4, $t2, $t3	# $t4 = s[i] - t[i]
	bne $t4, $zero, F1a	# if ($t4 != 0), return s[i] - t[i]
	sub $t5, $t2, $zero	# if (s[i] == '\0')
	beq $t5, $zero, F1b	# return 0
	addi $t0, $t0, 1	# go to next char/byte in string s
	addi $t1, $t1, 1	# go to next char/byte in string t
	j L1				# restart loop			
F1a:move $v0, $t4		# return s[i] - t[i]
	j F1c
F1b:li $v0, 0			# return 0
	j F1c
F1c:lw $t0, 0($sp)		# restore $t0-t5
	lw $t1, 4($sp)
	lw $t2, 8($sp)
	lw $t3, 12($sp)
	lw $t4, 16($sp)
	lw $t5, 20($sp)
	addi $sp, $sp, 24
	jr $ra				# end method
#-------------------------------------------------
# Quicksort - sorts an array of strings
# $a0 = char *v[0] (pointer to first element of array)
# $a1 = int left (low index)
# $a2 = int right (high index)
#-------------------------------------------------
Quicksort:
	addi $sp, $sp, -4	# save $ra
	sw $ra, 0($sp)
	addi $t2, $a1, 1	# if (right < left+1)
	slt $t3, $a2, $t2
	bne $t3, $zero, F2c	# 	return;
	add $t0, $a1, $a2	# int j = (left+right);
	srl $t1, $t0, 1		# j = j /2;
	addi $sp, $sp, -4	# swap(v, left, j);
	sw $a2, 0($sp)
	move $a2, $t1
	jal Swap
	lw $a2, 0($sp)
	addi $sp, $sp, 4
	move $s1, $a1		# last = left;
	addi $t0, $a1, 1	# $t0 = left+1
	move $s0, $t0		# i = left+1
	addi $t1, $a2, 1	# $t1 = right+1
L2:	slt $t2, $s0, $t1	# for (i = left+1; i < right+1; i++)
	beq $t2, $zero, F2b	# end for loop if condition test fails
	sll $t3, $s0, 2		# char *k = v[i];
	add $t4, $a0, $t3
	lw $t6, 0($t4)
	sll $t3, $a1, 2		# char *l = v[left];
	add $t5, $a0, $t3
	lw $t7, 0($t5)
	addi $sp, $sp, -8	# int m = strcmp(k, l);
	sw $a0, 0($sp)
	sw $a1, 4($sp)
	move $a0, $t6
	move $a1, $t7
	jal StrCmp
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	addi $sp, $sp, 8
	slt $t6, $v0, $zero	# if (m < 0)
	beq $t6, $zero, F2a
	addi $s1, $s1, 1	# 	last++;
	addi $sp, $sp, -8	# 	swap(v, last, i);
	sw $a1, 0($sp)
	sw $a2, 4($sp)
	move $a1, $s1
	move $a2, $s0
	jal Swap
	lw $a1, 0($sp)
	lw $a2, 4($sp)
	addi $sp, $sp, 8
F2a:add $s0, $s0, 1		# i++
	j L2				# restart for loop
F2b:addi $sp, $sp, -4	# swap(v, left, last);
	sw $a2, 0($sp)
	move $a2, $s1
	jal Swap
	lw $a2, 0($sp)
	addi $sp, $sp, 4
	addi $sp, $sp, -8	# Quicksort(v, left, last-1);
	sw $ra, 0($sp)
	sw $a2, 4($sp)
	addi $a2, $s1, -1
	jal Quicksort
	lw $ra, 0($sp)
	lw $a2, 4($sp)
	addi $sp, $sp, 8
	addi $sp, $sp, -8	# Quicksort(v, last+1, right);
	sw $ra, 0($sp)
	sw $a1, 4($sp)
	addi $a1, $s1, 1
	jal Quicksort
	lw $ra, 0($sp)
	lw $a1, 4($sp)
	addi $sp, $sp, 8
F2c:lw $ra, 0($sp)		# restore $ra
	addi $sp, $sp, 4
	jr $ra				# end method

##################################################
## data segment ##
##################################################

	.data
array:	.word word0, word1, word2, word3, word4, word5
word0:	.asciiz "mips"
word1:	.asciiz "spim"
word2:	.asciiz "Syscall"
word3:	.asciiz "xspim"
word4:	.asciiz "Quicksort"
word5:	.asciiz "recursion"
size:	.word 6
CLRF:	.asciiz "\n"
space:	.asciiz " "
text:	.asciiz "\nfinal array:\n"
