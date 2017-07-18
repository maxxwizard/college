package hw05;

/**
 * Matthew Huynh
 * CS112A1 - hw5
 * April 9, 2009
 *
 * Word.java
 * A custom class for use in a separate chaining hash table.
 */

public class Word {

	public String key;
	public String value;
	public Word next;
	
	public Word()
	{
		value = "";
		key = "";
		next = null;
	}
	
	public Word(String value, String key)
	{
		this.value = value;
		this.key = key;
		next = null;
	}
		
}
