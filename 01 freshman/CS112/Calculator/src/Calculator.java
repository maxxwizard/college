import java.util.*;

public class Calculator {
	
	public CStack stack;
	
	public Calculator()
	{
		stack = new CStack();
	}
	
	private boolean malformedSyntax(String s)
	{
		int symbolCount = 0;
		int otherCount = 0;
		
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
	
	public void assign(String assignment) throws malformedPostfixException, undefinedVariableException
	{
		if (malformedSyntax(assignment) == true)
			throw new malformedPostfixException();
		else
		{
			// if variable exists
			if (stack.variableExist(assignment.charAt(0)))
				((CStack) stack).replace(assignment.charAt(0), evaluate(assignment.substring(2)));
			// if variable doesn't exist
			else
				stack.push(assignment.charAt(0), evaluate(assignment.substring(2)));
		}
	}

	public int evaluate(String expr) throws malformedPostfixException, undefinedVariableException
	{
		//CStack temp = new CStack();
		
		char[] array = expr.toCharArray();
		
		for (int i = 0; i < array.length; i++)
		{
			//System.out.println("array item #" + i + ": " + array[i]);
			// if symbol
			if (isSymbol(array[i]))
			{
				// pop 2 from the stack
				int one = stack.pop().value;
				int two = stack.pop().value;
				// apply operator and push result onto stack
				stack.push(calculate(one, two, array[i]));
			}
			// if not a number
			else if (!isNumber(array[i]))
				// look up variable and push onto stack
				stack.push(stack.getValue(array[i]));
			// if is a number
			else if (isNumber(array[i]))
			{
				// push onto stack
				//System.out.println("passing " + array[i] + " to push method");
				stack.push(Integer.parseInt(String.valueOf(array[i])));
			}
			else
				throw new malformedPostfixException();
		}
		
		return stack.pop().value;
	}
	
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
