package hw3;

/*
 * Matthew Huynh (mhuynh)
 * CS112 - hw03
 * February 26, 2009
 * 
 * Variable.java
 * This is a class that has two data items, a char as the name and an int as the value.
 * This class piggybacks on the Node class as a Node's data item.
 * 
 * FOR USE AS PART OF A POSTFIX "CALCULATOR"
 */

public class Variable {
	    
		private char name;
		private int value;
	    
	    public Variable ( char name, int value ) {
	        this.name = name;
	        this.value = value;
	    }

		public char getName() {
			return name;
		}

		public void setName(char name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}	    
	    
}
