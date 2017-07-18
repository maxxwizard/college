/**
 * SquareRootCalculator.java
 * Computer Science 111, Boston University
 * 
 * base code provided by the course staff
 * 
 * algorithm implemented by: Matthew Huynh // mhuynh@bu.edu
 * 
 * This program computes gradually improving estimates
 * of the square root of a number using Newton's method.
 */

import java.util.*;

public class SquareRootCalculator {
    public static void main(String[] args) {
        int x;             // the number whose root we wish to find
        int numImproves;   // the number of times to improve the guess

        // Read the values from the user.
        Scanner scan = new Scanner(System.in);
        System.out.print("Input a positive integer (x): ");
        x = scan.nextInt();
        System.out.print("Number of times to improve the estimate: ");
        numImproves = scan.nextInt();
        
        // Prints the header.
        System.out.println("Estimates of the square root of " + x + ":");
        
        // Initializes the estimate and displays it.
        double estimate = x/2;
        System.out.println("    " + estimate);
        
        // Prints the current estimate after each calculation.
        for (int i = 0 ; i < numImproves; i++)
        {
            estimate = (estimate + (x/estimate)) / 2;
            System.out.println("    " + estimate);
        }

    }
}
