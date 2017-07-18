

public class calcTest {

	public static void main(String[] args) throws malformedPostfixException, undefinedVariableException {
		
		Calculator c = new Calculator();
		
		c.assign("x=5");
		c.stack.print();
		c.assign("y=x7+");
		c.stack.print();
		c.assign("z=57*");
		c.stack.print();
		int first = c.evaluate("xy*");
		System.out.println("first: " + first);
		c.assign("x=x8+y*");
		c.stack.print();
		
		/*
		// STACK TEST
		c.stack.push('x',200);
		c.stack.push(300);
		c.stack.push(100);
		c.stack.print();
		
		System.out.println();
		c.stack.pop();
		c.stack.print();
		
		System.out.println();
		c.stack.pop();
		c.stack.print();
		
		System.out.println();
		c.stack.pop();
		c.stack.print();
		*/

	}

}
