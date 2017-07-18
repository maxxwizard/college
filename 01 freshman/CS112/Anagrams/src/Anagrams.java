import java.io.*;
import java.util.*;

/**
 * Matthew Huynh
 * CS112A1 - hw5
 * April 15, 2008
 *
 * Anagrams.java
 * A program that queries the user for an input search word and
 * prints out all the anagrams found in a dictionary file.
 */

/**
 * @author Matthew Huynh
 *
 */
public class Anagrams {
	
	public static final String DICTIONARY = "TWL.txt";
    public static final Scanner CONSOLE = new Scanner(System.in);

	public static void main(String[] args) {
		
		// LOAD WORDS INTO HASH TABLE
		Scanner dictionary = null;
		try {
			dictionary = new Scanner(new File(DICTIONARY));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary file '" + DICTIONARY + "' not found!");
			e.printStackTrace();
		}
        HashTable table = new HashTable();
        while (dictionary.hasNextLine())
        {
        	String data = dictionary.nextLine();
        	String key = alphagram(data);
        	Word word = new Word(data, key);
        	table.insert(word);
        	
        	//System.out.println(data + " | " + key);
        }     

		// CONTINUALLY PROMPT USER FOR STRING
        while (true)
        {
        	System.out.print("Word to find anagrams for?: ");
        	String input = CONSOLE.nextLine();
        	
        	if (input != "" || input != null)
        	{
        		// COMPUTE ALPHAGRAM OF INPUT STRING
        		String keyToFind = alphagram(input).toUpperCase();
        		// HASH IT AND SEARCH FOR ANAGRAMS WORDLIST
        		WordList list = table.getItem(keyToFind);
        		// PRINT ANAGRAMS
        		printAnagrams(list, input, keyToFind);
    		}
    	}
    }// END MAIN

	public static String alphagram (String s) {
		char[] data = s.toCharArray();
		bubbleSort(data);
		String anagram = "";
		
		for (int i = 0; i < data.length; i++)
			anagram += data[i];
		
		return anagram;
	}
	
	public static void bubbleSort(char[] a)
	{
		char temp;
		for (int i = 0; i < a.length-1; i++) {
			for (int j = 0; j < a.length-1-i; j++)
				if (a[j+1] < a[j]) {  
					temp = a[j]; 
					a[j] = a[j+1];
					a[j+1] = temp;
				}
		}
	}
	
	public static void printAnagrams(WordList list, String input, String key)
	{
		Word current = list.head;
		int count = 0;
		
		// count words with matching key
		while (current.next != null)
		{
			if (current.key.equalsIgnoreCase(key) == true)
				count++;
			current = current.next;
		}
		
		// if one or less words found, no anagrams
		if (count <= 1) {
			System.out.println("No anagrams were found.");
		} else {
			current = list.head;
			while (current != null)
			{
				// check that value and key are same as input
				if (current.value.equalsIgnoreCase(input) == false && current.key.equalsIgnoreCase(key) == true)
				{
					System.out.print(current.value);
					if (count > 1) {
						System.out.print(", ");
						count--;
					}
				}
				current = current.next;
			}
			System.out.println();
		}
	}

}
