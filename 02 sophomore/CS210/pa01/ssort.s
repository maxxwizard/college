## MATTHEW HUYNH
## CS210 - MATTA
## OCTOBER 31, 2008 
## PROGRAMMING ASSIGNMENT #1 - PART 1
##
## Start of file ssort.s

##################################################
## text segment ##
##################################################

	.text
	.globl __start

__start:

#------------------
# SORT ARRAY
#------------------
	la $a0, Array		# $a0 = &Array[0]
	la $t0, size		# $t0 = &size
	lw $a1, 0($t0)		# $a1 = size
	jal Sort		# invoke Sort method
#------------------
# PRINT TEXT
#------------------
	la $a0, text		# print "The sorted array is:  "
	li $v0, 4
	syscall
#------------------
# PRINT CURRENT ARRAY
#------------------
	la $a0, Array		# $a0 = &Array[0]
	la $t0, size		# $t0 = &size
	lw $a1, 0($t0)		# $a1 = size
	jal PrintArray		# invoke PrintArray method
#------------------
# PRINT NEW LINE
#------------------
	la $a0, CLRF		# print "\n"
	li $v0, 4
	syscall
#------------------
# EXIT
#------------------
	li $v0, 10		# exit
	syscall

#-------------------------------------------------
PrintArray:
	li $t0, 0		# i = 0
loop0:	slt $t1, $t0, $a1	# if (i < n)
	beq $t1, $zero, finish0	# end loop if !(i < n)

	sll $t2, $t0, 2		# i = i*4 (to get byte address)
	add $t3, $a0, $t2	# $t3 = &Array[i]
	lw $t4, 0($t3)		# $t4 = Array[i]
	addi $sp, $sp, -4	# malloc for 1 word
	sw $a0, 0($sp)		# preserve $a0
	move $a0, $t4		# print Array[i]
	li $v0, 1		
	syscall
	la $a0, space		# print 2 spaces
	li $v0, 4
	syscall
	syscall
	lw $a0, 0($sp)		# restore $a0
	addi $sp, $sp, 4	# dealloc for 1 word
	addi $t0, $t0, 1	# i++
	j loop0			# restart loop
finish0:jr $ra
#-------------------------------------------------
Swap:
	lw $t0, 0($a0)		# $t0 = x
	lw $t1, 0($a1)		# $t1 = y
	sw $t0, 0($a1)		# y = x
	sw $t1, 0($a0)		# x = y
	jr $ra
#-------------------------------------------------
MinPosition:
	sll $t9, $a1, 2		# $t9 = start*4 (byte address)
	add $t0, $a0, $t9	# $t0 = &Array[start]
	lw $t8, 0($t0)		# int min = Array[start]
	move $t1, $a1		# int minpos = start
	addi $t2, $a1, 1	# int index = start + 1
	add $t3, $a2, 1		# $t3 = stop + 1
loop1a:	slt $t4, $t2, $t3	# while (index <= stop)
	beq $t4, $zero, finish1
loop1b:	sll $t9, $t2, 2		# index * 4 (byte address)
	add $t5, $a0, $t9	# $t5 = &Array[index]
	lw $t6, 0($t5)		# $t6 = Array[index]
	slt $t7, $t6, $t8	# Array[index] < min
	beq $t7, $zero, loop1c
	move $t8, $t6		# min = Array[index]
	move $t1, $t2		# minpos = index
loop1c:	add $t2, $t2, 1		# index++
	j loop1a
finish1:move $v0, $t1		# return minpos
	jr $ra
#-------------------------------------------------
Sort:
	addi $sp, $sp, -12	# malloc $sp for 3 words
	sw $s0, 8($sp)		# preserve $s0, $s1, $s2
	sw $s1, 4($sp)
	sw $s2, 0($sp)

	li $s0, 0		# $s0 = 0
	li $s1, 0		# $s1 = 0
	add $s2, $a1, -1	# $s2 = n (instead of n-1)
loop2:	slt $t0, $s0, $s2	# $t0 = (i < n-1)
	beq $t0, $zero, finish2	# if $t0 = 0, go to finish2

	addi $sp, $sp, -16	# malloc $sp for 4 words
	sw $a0, 12($sp)		# preserve $a0, $a1, $a2, $ra
	sw $a1, 8($sp)
	sw $a2, 4($sp)
	sw $ra, 0($sp)
				# $a0 = $a0 (do nothing)
	move $a1, $s0		# $a1 = i
	move $a2, $s2		# $a2 = n-1
	jal MinPosition		# call MinPosition
	move $s1, $v0		# MinPos = MinPosition(Arr, i, n-1)
	lw $a0, 12($sp)		# restore $a0, $a1, $a2
	lw $a1, 8($sp)
	lw $a2, 4($sp)
	lw $ra, 0($sp)
	addi $sp, $sp, 16	# dealloc $sp for 4 words

	addi $sp, $sp, -12	# malloc $sp for 3 words
	sw $a0, 8($sp)		# preserve $a0, $a1, $ra
	sw $a1, 4($sp)
	sw $ra, 0($sp)
	move $t2, $a0		# $t2 = &Array[0]
	sll $t9, $s0, 2		# $a0 = &Array[i]
	add $a0, $t2, $t9	
	sll $t9, $s1, 2		# $a1 = &Array[MinPos]
	add $a1, $t2, $t9
	jal Swap		# call Swap
	lw $a0, 8($sp)		# restore $a0, $a1, $ra
	lw $a1, 4($sp)
	lw $ra, 0($sp)
	addi $sp, $sp, 12	# dealloc $sp for 3 words

	addi $s0, $s0, 1	# i++

	j loop2			# restart while loop
finish2:
	lw $s0, 8($sp)		# restore $s0, $s1, $s2
	lw $s1, 4($sp)
	lw $s2, 0($sp)
	addi $sp, $sp, 12	# dealloc $sp for 3 words
	jr $ra
#-------------------------------------------------

##################################################
## data segment ##
##################################################

	.data
Array:	.word 10,3,6,1,5,9,4,2
size:	.word 8
CLRF:	.asciiz "\n"
space:	.asciiz " ";
text:	.asciiz "The sorted array is:   "

