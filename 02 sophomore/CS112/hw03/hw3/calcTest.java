package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * calcTest.java
 * This is a test file for a postfix "calculator."
 * Code based on: http://cs-people.bu.edu/tvashwin/cs112_spring09/lab04_files/LinkedList.java
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

public class calcTest {

	public static void main(String[] args) throws malformedPostfixException, undefinedVariableException {
		
		Calculator c = new Calculator();
		
		c.assign("x=5");
		c.assign("y=x7+");
		int first = c.evaluate("xy*");
		System.out.println(first);
		
		c.assign("z=57*");
		c.assign("x=x8+y*");
		int sec = c.evaluate("xy+");
		System.out.println(sec);
		
		int third = c.evaluate("z");
		System.out.println(third);
		//This code will set first to 60, then it will set sec to 168, and third to 35.
		
		/*
		// STACK TEST
		CStack testStack = new CStack();
		testStack.push(300);
		testStack.push(100);
		testStack.print();
		
		System.out.println();
		testStack.pop();
		testStack.print();
		
		System.out.println();
		testStack.pop();
		testStack.print();
		*/
		
		

	}

}
