/**
 * DrawMarsh.java
 *
 * Computer Science 111, Boston University
 *
 * Matthew Huynh // mhuynh@bu.edu
 * 
 * This program draws an ASCII rendition of the BU
 * Marsh Chapel using simple methods and nested loops.
 */

public class DrawMarsh {
    
    // a class constant that determines how big
    // the chapel will be drawn
    public static final int TOP_HEIGHT = 4;
    
    public static void main(String[] args) {
        
        drawPeak();
        drawTop();
        drawMid();
        drawBottom();
        drawDivider();
        drawBottom();
        drawDivider();

    }
    
    // draws a vertical column (used for left, middle, and right columns)
    public static void drawVerticalColumn() {
        
        for (int i = 0; i < TOP_HEIGHT/2; i++)
            System.out.print("|");    
    }
    
    // draws the peak of the figure
    public static void drawPeak() {
        
        for (int i = 0; i < TOP_HEIGHT/2; i++)
            System.out.print(" ");
        for (int i = 0; i < TOP_HEIGHT*2; i++)
            System.out.print(" ");
        for (int i = 0; i < TOP_HEIGHT/2; i++)
            System.out.print("+");
        for (int i = 0; i < TOP_HEIGHT*2; i++)
            System.out.print(" ");
        for (int i = 0; i < TOP_HEIGHT/2; i++)
            System.out.print(" ");
        
        System.out.println();
    }
    
    // draws the top of the figure (left and right "stairs")
    public static void drawTop() {
        int numChars = 0;
        int numSpaces = 0;
        
        for (int i = 0; i < TOP_HEIGHT; i++) {
            
            drawVerticalColumn();
            
            numChars = i*2;
            numSpaces = TOP_HEIGHT*2 - 2 - numChars;
            
            // draw left side
            for (int j = 0; j < numSpaces; j++)
                System.out.print(" ");
            System.out.print("_/");
            for (int j = 0; j < numChars; j++)
                System.out.print(":");
            
            drawVerticalColumn();
            
            // draw right side
            for (int j = 0; j < numChars; j++)
                System.out.print(":");
            System.out.print("\\_");
            for (int j = 0; j < numSpaces; j++)
                System.out.print(" ");
            
            drawVerticalColumn();
            
            System.out.println();
        }
        
    }
    
    // draws the middle of the figure (left and right "ramps")
    public static void drawMid() {
        int numColons = 0;
        int numDots = 0;
        
        for (int i = 0; i < TOP_HEIGHT; i++) {
            
            drawVerticalColumn();
            
            numDots = i;
            numColons = TOP_HEIGHT*2 - 1 - numDots;
            
            // draw left side
            for (int j = 0; j < numColons; j++)
                System.out.print(":");
            System.out.print("/");
            for (int j = 0; j < numDots; j++)
                System.out.print(".");
            
            drawVerticalColumn();
            
            // draw right side
            for (int j = 0; j < numDots; j++)
                System.out.print(".");
            System.out.print("\\");
            for (int j = 0; j < numColons; j++)
                System.out.print(":");
            
            drawVerticalColumn();
            
            System.out.println();
            
        }
        
    }
    
    public static void drawBottom() {
        int numColons = 0;
        int numDots = 0;
        
        for (int i = 0; i < TOP_HEIGHT; i++) {
            
            drawVerticalColumn();
            
            numDots = TOP_HEIGHT;
            numColons = TOP_HEIGHT*2 - 1 - numDots;
            
            // draw left side
            for (int j = 0; j < numColons; j++)
                System.out.print(":");
            System.out.print("|");
            for (int j = 0; j < numDots; j++)
                System.out.print(".");
            
            drawVerticalColumn();
            
            // draw right side
            for (int j = 0; j < numDots; j++)
                System.out.print(".");
            System.out.print("|");
            for (int j = 0; j < numColons; j++)
                System.out.print(":");
            
            drawVerticalColumn();
            
            System.out.println();
            
        }
        
    }
    
    // draws the divider (long line of equal signs)
    public static void drawDivider() {
        
        drawVerticalColumn();
        
        for (int i = 0; i < 2*TOP_HEIGHT; i++)
            System.out.print("=");
        for (int i = 0; i < TOP_HEIGHT/2; i++)
            System.out.print("=");
        for (int i = 0; i < 2*TOP_HEIGHT; i++)
            System.out.print("=");  
        
        drawVerticalColumn();
        
        System.out.println();
    }
    
}