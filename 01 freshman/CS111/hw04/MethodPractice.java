/**
 * MethodPractice.java
 *
 * Computer Science 111, Boston University
 *
 * Matthew Huynh // mhuynh@bu.edu
 * 
 * This program draws an ASCII rendition of the BU
 * Marsh Chapel using methods and loops.
 */

public class MethodPractice {
    
    public static void main(String[] args) {
        
        // tests the correctness of the method minOfThree
        // all the return values should be the integer 1
        System.out.println(minOfThree(1, 2, 3));
        System.out.println(minOfThree(1, 3, 2));
        System.out.println(minOfThree(2, 3, 1));
        System.out.println(minOfThree(2, 1, 3));
        System.out.println(minOfThree(3, 2, 1));
        System.out.println(minOfThree(3, 1, 2));
        System.out.println(minOfThree(1, 1, 2));
        System.out.println(minOfThree(1, 2, 2));
        System.out.println(minOfThree(2, 1, 1));
        System.out.println(minOfThree(2, 1, 2));
        System.out.println(minOfThree(2, 2, 1));
        
        // tests the correctness of the method printLetters
        printLetters("Rabbit");
        printLetters("");
    }
    
    // returns the smallest of the three integers passed in
    public static int minOfThree(int a, int b, int c) {
        int min;
        
        if (a < b)
            min = a;
        else
            min = b;
        if (min > c)
            min = c;
        
        return min;
    }
    
    // uses a fencepost loop to print the characters of the String, separated by commas
    public static void printLetters(String string) {
        if (string != "") {
            System.out.print(string.charAt(0));
            for (int i = 1; i < string.length(); i++) {
                System.out.print(", ");
                System.out.print(string.charAt(i));
            }
        }
    }
}