#Calculator
#You will recieve marks based 
#on functionality of your calculator.
#REMEMBER: invalid inputs are ignored.
#=================================================
#Name: Cassiel Moroney
#ID: 260662020
#=================================================

	.data
compute:	.space 100 #raw IO data

print:		.space 100 #data for writing

leftVar:	.float	0.0
rightVar:	.float	0.0
operator: 	.word	0

leftVarBool:	.byte	0
rightVarBool:	.byte	0	

storedVal:	.word	0

zero:		.float	0.0
base:		.float	10.0

noNumStr:	.asciiz "Must input at least one number."
noOpStr:	.asciiz "Must input at least one operator."

#Have fun!

	.text
#TODO:
#main procedure, that will call your calculator
#=================================================
#
# $s0-7: constants for the calculator
# $a0: argument that tells program where to go after the "clear" function
# $a1: where unparsed data is temporarily stored 
# $a2: length of unparsed data
#
#=================================================
main:
	li $s0, 10 #code for enter
	li $s1, 99 #c(lear)
	li $s2, 113 #q(uit)
	li $s3, 43 #+
	li $s4, 45 #-
	li $s5, 47 #/
	li $s6, 42 #*
	li $s7, 32 #space
	
freshStart:
	la $a1, compute
	#addi $a1, $a1, 4
	li $a2, 0 			#l=0
		
loop:	jal read
	beq $v0, $s0, calculator 	#\n case
	move $a0, $v0
	jal write
	
	beq $v0, $s2, exit 		#q case
	li $a0, -1
	beq $v0, $s1, fullClear 	#c case
	beq $v0, $s7, parse 		#space case
	addi $a1, $a1, -4
	sw $v0, ($a1) 			#else, push char into the array
	addi $a2, $a2, 1 		#l++
	j loop

#=================================================
#
# $t0: current point in memory
# #t1: i counter
#
#=================================================
fullClear:
	sb $0, leftVarBool		#setting bools to 0 is as good as emptying variables themselves
	sb $0, rightVarBool
	sw $0, operator
	
	l.s $f9, storedVal		# for some reason, can't set floats to 0 any other way
	sub.s $f9, $f9, $f9
	s.s $f9, storedVal
	
clear:  move $t6, $a0			#save $a0
	la $t0, compute 		#move backwards until at original address
	addi $a2, $a2, 1 		#restore length
	li $t1, 0 			#i=0
	
clearLoop:
	sw $0, ($t0) 			#make every cell null
	addi $t1, $t1, 1 		#i++
	bne $t1, $a2, clearLoop 	#for i<length
	move $a0, $t6			#restore parameter
	beq $a0, -1, freshStart		#if called from parse, basically - return for next input
	jr $ra				#if called from calculator, basically - return to calculating

#takes in a string of digits or operators
#if digits, calculates the base-10 value of the string
#=================================================
#
# $a0: return to calculator after parsing+clear
# $a1: place in stack
# $a2: length of input
#
# $v0: current digit, before becoming a float
# f1: current digit
# $f12: sum of the digits - the value of the # inputted

# $t6: saves $a0 (frees up $a0 for testing syscalls)
#
# power fn:
# $f1: digit being multiplied
# $f4: constant 10 for base
# $t1: j
#
#=================================================
parse: 	move $t6, $a0			#saving parameter
	sub.s $f7, $f7, $f7 		#setting equal to 0.0 or the float zero didn't work  ¯\_(ツ)_/¯
	sub.s $f12, $f12, $f12
	li $t0, 0 			#i=0
	addi $a2, $a2, -1 		#length
	
	lw $v0, ($a1) 			
	beq $v0, $s3, storeOp		#check if an operator
	beq $v0, $s4, storeOp
	beq $v0, $s5, storeOp
	beq $v0, $s6, storeOp
	
parseLoop: 
	lw $v0, ($a1) 			#load again (for looping purposes)
	beq $v0, $s4, negative
	addi $v0, $v0, -48 		#convert from ascii to int
	
	mtc1 $v0, $f1			#convert to float
	cvt.s.w $f1, $f1
	l.s $f4, base
	li $t1, 0 			#j=0
pow:	beq $t0, $t1, powOver		#power fn
	mul.s $f1, $f1, $f4
	addi $t1, $t1, 1
	j pow
	
powOver:add.s $f7, $f7, $f1		#sum exponents
	addi $t0, $t0, 1
	bgt $t0, $a2, chooseVar		#if done, save data
	addi $a1, $a1, 4
	j parseLoop

negative: mov.s $f6, $f7		#make negative
	sub.s $f7, $f7, $f6
	sub.s $f7, $f7, $f6
	
chooseVar: #since the function parses the string continuously, it must decide which variable to occupy
	#if more than 2 integers are inputted, the leftmost (oldest) one is shifted out
	lb $t0, rightVarBool
	lb $t1, leftVarBool
	
	beqz $t0, goRight	#if right is empty, fill
	l.s $f8, rightVar	#else, shift left
	s.s $f8, leftVar
	li $t1, 1 		#and flag left as full
	sb $t1, leftVarBool
	#continue to add into the right
	
goRight: s.s $f7, rightVar	#store right
	li $t0, 1 		#flag right as full
	sb $t0, rightVarBool 
	
	move $a0, $t6		#restore parameter
	j clear			#clear stack for next data
	
storeOp : la $t0, operator	#store the symbol
	sw $v0, 0($t0)		#needs no flag bc != is proof enough

	move $a0, $t6		#restore parameter
	j clear			#clear stack for next data
		

#calculator procedure, that will deal with the input
#=================================================
calculator:

	li $a0, 1
	jal parse			#get last input
	
	#clear stack for next data
	lb $t0, rightVarBool		#load booleans
	lb $t1, leftVarBool
	lw $t2, operator
	
	beqz $t0, noNumbers		#errors
	beqz $t2, noOp

	#  Operation Number <enter is pressed>
	#  uses prior result as the first number
	bnez $t1, getLeft		#if no leftmost #, there is only one #
	l.s $f1, storedVal		#so retrieve the stored value
	j getRight
	
	#  Number Operation Number <enter is pressed>
	#  Must display the result on the screen
getLeft: l.s $f1, leftVar
getRight: l.s $f2, rightVar
	lw $t2, operator
	
	beq $t2, $s3, cAdd		#move to the operators (finally!)
	beq $t2, $s4, cSub
	beq $t2, $s5, cDiv
	beq $t2, $s6, cMul
	
return:	s.s $f12, storedVal		#Store answer for future operations

	# Returns the new result to the display
	li $a0, 32 # space
	jal write
	li $a0, 61 # =
	jal write
	li $a0, 32 # space
	jal write
	
	mul.s $f12, $f12, $f4 		#$f4 is the constant 10.0
	mul.s $f12, $f12, $f4		#multiply by 100 to have a final # that is rounded to two decimals
	cvt.w.s $f12, $f12		#convert float to int
	mfc1 $v1, $f12

intSave: la $t4, print			#get address of new stack
	li $t3, 0			#j=0
	blt $v1, $0, printNegative	#if negative output
intLoop: beqz $v1, nxt			#div by 10 until 0
	div $v1, $v1, 10
	mfhi $t1			#push the mods
	sb $t1, 0($t4)
	addi $t4, $t4, -1
	addi $t3, $t3, 1		#j++
	j intLoop
	
printNegative:				#if negative, print minus sign and make the input positive
	mul $v1, $v1, -1
	li $a0, 45
	jal write
	j intLoop
	
nxt:	addi $t4, $t4, 1		#account for over-stepping at the end of last loop
	addi $t3, $t3, -1
writeAnswLoop:	
	blt $t3, $0, nxxt
	beq $t3, 1, decimal 		#if only two chars left, assume a decimal
afterDecimal:
	lb $t2, 0($t4)			#pop from stack
	move $a0, $t2
	addi $a0, $a0, 48 		#back to ascii
	jal write			#write!
	addi $t4, $t4, 1
	addi $t3, $t3, -1		#j--
	j writeAnswLoop

decimal: li $a0, 46			#print decimal point
	jal write
	j afterDecimal

nxxt:	sb $0, rightVarBool		#clear variables
	sb $0, leftVarBool
	sw $0, operator
	
	j main
	
cAdd: 	add.s $f12, $f1, $f2		#float operations
	j return

cSub:	sub.s $f12, $f1, $f2
	j return

cMul:	mul.s $f12, $f1, $f2
	j return
	
cDiv:	div.s $f12, $f1, $f2
	j return

noNumbers:				#error messages
	li $v0, 4
	la $a0, noNumStr
	syscall
	j error

noOp:	li $v0, 4
	la $a0, noOpStr
	syscall
	j error

error:	li $v0, 17
	li $a0, -1
	syscall

exit:	li $v0, 10			#exit
	syscall

#driver for getting input from MIPS keyboard
#=================================================
read:  	lui $t0, 0xffff #ffff0000
rdLoop:	lw $t1, 0($t0) #control
	andi $t1,$t1,0x0001
	beq $t1,$zero,rdLoop
	lw $v0, 4($t0) #data	
	jr $ra
	
#driver for putting output to MIPS display
#=================================================
write:	lui $t0, 0xffff
wLoop:	lw $t1, 8($t0)
	andi $t1, $t1, 0x0001
	beq $t1, $zero, wLoop
	sw $a0, 12($t0)
	jr $ra
