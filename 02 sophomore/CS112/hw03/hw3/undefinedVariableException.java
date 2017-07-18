package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * undefinedVariableException.java
 * This is a custom Exception class.
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

@SuppressWarnings("serial")
public class undefinedVariableException extends Exception {
	
	public String getMessage()
    {
		return "undefinedVariableException";
    }
	
}