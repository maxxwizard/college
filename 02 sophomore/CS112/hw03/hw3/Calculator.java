package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * Calculator.java
 * This is a postfix Calculator, implemented with a linked list to
 * hold variables and an array-based stack to do calculations.
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

public class Calculator {
	
	public CStack stack;
	public VList<Variable> variables;
	
	// Default constructor
	public Calculator()
	{
		stack = new CStack();
		variables = new VList<Variable>();
	}
	
	// Checks if an assignment postfix string is malformed
	// logic: there should always be at least one more variable than there are operators
	private boolean malformedAssignmentSyntax(String s)
	{
		int symbolCount = 0;
		int otherCount = 0;
		
		// there should always be an "equals" sign at string[1]
		if (s.charAt(1) != '=')
			return true;
		
		for (int i = 2; i < s.length(); i++)
		{
			if (isSymbol(s.charAt(i)))
				symbolCount++;
			else
				otherCount++;
		}
		
		return (otherCount - symbolCount != 1);
	}
	
	// Checks if an evaluate postfix string is malformed
	// logic: there should always be at least one more variable than there are operators
	private boolean malformedEvaluateSyntax(String s)
	{
		int symbolCount = 0;
		int otherCount = 0;
		
		for (int i = 0; i < s.length(); i++)
		{
			if (isSymbol(s.charAt(i)))
				symbolCount++;
			else
				otherCount++;
		}
		
		return (otherCount - symbolCount != 1);
	}
	
	// Parses a postfix assignment string in order to assign a variable a value
	public void assign(String assignment) throws malformedPostfixException, undefinedVariableException
	{
		if (malformedAssignmentSyntax(assignment) == true)
			throw new malformedPostfixException();
		else
		{
			// if variable exists
			if (variables.variableExist(assignment.charAt(0)))
				variables.setVariableValue(assignment.charAt(0), evaluate(assignment.substring(2)));
			// if variable doesn't exist
			else
				variables.addToTail(new Variable(assignment.charAt(0), evaluate(assignment.substring(2))));
		}
	}

	// Evaluates a postfix string and returns the result
	public int evaluate(String expr) throws malformedPostfixException, undefinedVariableException
	{
		if (malformedEvaluateSyntax(expr) == true)
		{
			throw new malformedPostfixException();
		} else
		{
			char[] array = expr.toCharArray();
			
			for (int i = 0; i < array.length; i++)
			{
				// if symbol
				if (isSymbol(array[i]))
				{
					// pop 2 from the stack
					int one = stack.pop();
					int two = stack.pop();
					// apply operator and push result onto stack
					stack.push(calculate(one, two, array[i]));
				}
				// if not a number
				else if (!isNumber(array[i]))
					// look up variable and push onto stack
					stack.push(variables.getVariableValue(array[i]));
				// if is a number
				else if (isNumber(array[i]))
				{
					// push onto stack
					stack.push(Integer.parseInt(String.valueOf(array[i])));
				}
				else
					throw new malformedPostfixException();
			}
			
			return stack.pop();
		}
	}
	
	// Helper method
	public int calculate(int one, int two, char oper)
	{
		if (oper == '+')
			return (one + two);
		if (oper == '-')
			return (one - two);
		if (oper == '*')
			return (one * two);
		if (oper == '/')
			return (one / two);
		
		return 0;
	}
	
	public boolean isSymbol(char c)
	{
		return (c == '+' || c == '-' || c == '*' || c == '/');
	}
	
	public boolean isNumber(char c)
	{
		return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9');
	}
			
}
