package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * malformedPostfixException.java
 * This is a custom Exception class.
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

@SuppressWarnings("serial")
public class malformedPostfixException extends Exception {
	
	public String getMessage()
    {
		return "malformedPostFixException";
    }
	
}
