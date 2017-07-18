/**
 * AreaCalculator.java
 * Computer Science 111, Boston University
 * 
 * base code provided by the course staff
 * 
 * algorithm implemented by: Matthew Huynh // mhuynh@bu.edu
 * 
 * This program computes the area of a circular plot of land
 * and displays it in different units.
 */

import java.util.*;

public class AreaCalculator {
    public static void main(String[] args) {
        int diameter;                // the diameter of the plot in feet
        
        // Read the values from the user.
        Scanner scan = new Scanner(System.in);
        System.out.print("Input the diameter, measured to the nearest foot: ");
        diameter = scan.nextInt();
        
        int radius = diameter / 2;   // the radius of the plot in feet
        
        // Prints the header.
        System.out.println("The area is:");
        
        // Prints out the area in square feet.
        System.out.println("    " + (Math.PI*radius*radius) + " square feet");
        
        // Prints out the area in square yards.
        System.out.println("    " + (Math.PI*radius*radius/9) + " square yards");
        
        // Prints out the integral number of square yards
        System.out.print("    " + (int)(Math.PI*radius*radius/9) + " square yards");
        
        // Prints out the remaining area in square feet                    
        System.out.print(" and " + ((Math.PI*radius*radius/9)%1)*9 + " square feet");
        
    }
}
