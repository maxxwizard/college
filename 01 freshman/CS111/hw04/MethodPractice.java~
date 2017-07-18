/**
 * DrawMarsh2.java
 *
 * Computer Science 111, Boston University
 *
 * Matthew Huynh // mhuynh@bu.edu
 * 
 * This program draws an ASCII rendition of the BU
 * Marsh Chapel using methods and loops.
 */

public class DrawMarsh2 {
    
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
    
    // prints NUM copies of the character CH on the current output line
    public static void printChars(char ch, int num) {
        for (int i = 0; i < num; i++) {
            System.out.print(ch);
        }
    }
    
    // draws a vertical column (used for left, middle, and right columns)
    public static void drawVerticalColumn() {
        
        printChars('|',TOP_HEIGHT/2); 
    }
    
    // draws the peak of the figure
    public static void drawPeak() {
        
        printChars(' ',TOP_HEIGHT/2);
        printChars(' ',TOP_HEIGHT*2);
        printChars('+',TOP_HEIGHT/2);
        printChars(' ',TOP_HEIGHT*2);
        printChars(' ',TOP_HEIGHT/2);
        
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
            printChars(' ',numSpaces);
            System.out.print("_/");
            printChars(':',numChars);
            
            drawVerticalColumn();
            
            // draw right side
            printChars(':',numChars);
            System.out.print("\\_");
            printChars(' ',numSpaces);
            
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
            printChars(':',numColons);
            System.out.print("/");
            printChars('.',numDots);
            
            drawVerticalColumn();
            
            // draw right side
            printChars('.',numDots);
            System.out.print("\\");
            printChars(':',numColons);
            
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
            printChars(':',numColons);
            System.out.print("|");
            printChars('.',numDots);
            
            drawVerticalColumn();
            
            // draw right side
            printChars('.',numDots);
            System.out.print("|");
            printChars(':',numColons);
            
            drawVerticalColumn();
            
            System.out.println();
            
        }
        
    }
    
    // draws the divider (long line of equal signs)
    public static void drawDivider() {
        
        drawVerticalColumn();
        
        printChars('=',2*TOP_HEIGHT);
        printChars('=',TOP_HEIGHT/2);
        printChars('=',2*TOP_HEIGHT);
        
        drawVerticalColumn();
        
        System.out.println();
    }
    
}