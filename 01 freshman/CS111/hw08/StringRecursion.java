/*
 * StringRecursion.java
 *
 * starter code by: Computer Science 111, Boston University
 * 
 * modified by: Matthew Huynh
 * username: mhuynh
 *
 * A class that contains recursive methods that operate on strings.
 */

public class StringRecursion {
    /*
     * numOccur - a recursive method that returns the number of times 
     * that the character ch occurs in the String str.
     * 
     * The main method includes two examples of using this method.
     *
     * You can also test this method by entering
     * NumOccur.numOccur(ch, str) -- where ch is replaced by a char
     * and str is replaced by a string -- in the Interactions Pane.
     */
    public static int numOccur(char ch, String str) {
        // base case
        if (str == null || str.equals("")) {
            return 0;
        }
    
        // recursive case
        int numOccurInRest = numOccur(ch, str.substring(1));
        if (ch == str.charAt(0)) {
            return 1 + numOccurInRest;
        } else {
            return numOccurInRest;
        }
    }

    /* 
     * Creates a frame for each character in a 
     * string and prints them in order.
     */
    public static void printLetters(String str) {
        // base case
        if (str.equals("") || str == null)
            return;
        
        // recursive case
        System.out.print(str.charAt(0));
        if (str.length() != 1) {
            System.out.print(", ");
            printLetters(str.substring(1));
        }
    
    }
    
    /* 
     * Creates a frame for each character in a 
     * string and prints them in reverse order.
     */
    public static void printLettersReverse(String str) {
        // base case
        if (str.equals("") || str == null)
            return;
        
        // recursive case
        if (str.length() != 1) {
            printLettersReverse(str.substring(1));
            System.out.print(", ");
        }
        System.out.print(str.charAt(0));
     
    }
    
    /* 
     * Finds the length of a given string recursively
     * by counting how many frames are created based
     * on how many characters there are in a string.
     */
    public static int length(String str) {
        // base case
        if (str.equals("") || str == null)
            return 0;
        
        // recursive case
        return 1 + length(str.substring(1));

    }
    
    /*
     * Finds the index of a character in a given string
     * by checking the first character of the string passed in.
     * Each recursive method call passes in the string minus another
     * character from the front of the string.
     */
    public static int indexOf(char ch, String str) {
        // empty string check
        if (str.equals("") || str == null)
            return -1;
        
        // base case
        if (ch == str.charAt(0)) {
            return 0;
        // recursive case
        } else {
            return 1 + indexOf(ch, str.substring(1));
        }
    }
    
    /*
     * Repeats a given string n number of times by
     * passing the string to itself with n-1 until n-1
     * is finally zero.
     */
    public static String repeat(String str, int n) {
        // base case
        if (str.equals("") || str == null || n == 0)
            return "";
        
        // returns the 
        return str + repeat(str, n-1);
    }
    
    public static void main(String[] args) {
        System.out.println("numOccur('s', \"Mississippi\") = " +
          numOccur('s', "Mississippi"));
        System.out.println("numOccur('e', \"Mississippi\") = " +
          numOccur('e', "Mississippi"));                   
    }
}
