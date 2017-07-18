/*
 * Matthew Huynh (mhuynh)
 * CS111 B1
 * 09-11-2007       
 * 
 * This program currently has methods to write the letters T, E, R, I, E, S in ASCII block letters. 
 * The default main method will print out the phrase "TERRIERS RISE" in a vertical orientation.    
 */

public class BlockLetterWriter {
 
  // prints out the phrase "TERRIERS RISE" in a vertical orientation
  public static void main(String[] args) 
  {
      writeT();
      System.out.println("");
      writeE();
      System.out.println("");
      writeR();
      System.out.println("");
      writeR();
      System.out.println("");
      writeI();
      System.out.println("");
      writeE();
      System.out.println("");
      writeR();
      System.out.println("");
      writeS();
      System.out.println("");
      System.out.println("");
      System.out.println("");
      writeR();
      System.out.println("");
      writeI();
      System.out.println("");
      writeS();
      System.out.println("");
      writeE();
  }
  
  // prints the ASCII block letter E
  public static void writeE() 
  {
      System.out.println("    +------");
      System.out.println("    |");
      System.out.println("    +----");
      System.out.println("    |");
      System.out.println("    +------");    
    }
  
  // prints the ASCII block letter T
  public static void writeT() 
  {
      System.out.println("    ---+---");
      System.out.println("       |");
      System.out.println("       |");
      System.out.println("       |");
      System.out.println("       |");    
      System.out.println("       |");       
    }
   
  // prints the ASCII block letter R
  public static void writeR() 
  {
      System.out.println("    +----");
      System.out.println("    |    \\");
      System.out.println("    |     |");
      System.out.println("    |____/");
      System.out.println("    | \\");    
      System.out.println("    |  \\");
      System.out.println("    |   \\");
  }    
  
  // prints the ASCII block letter I
  public static void writeI() 
  {
      System.out.println("    ---+---");
      System.out.println("       |");
      System.out.println("       |");
      System.out.println("       |");
      System.out.println("       |");    
      System.out.println("    ---+---");       
  }
  
  // prints the ASCII block letter S
  public static void writeS() 
  {
      System.out.println("    -----");
      System.out.println("   /     \\");
      System.out.println("   \\");
      System.out.println("    -----");                       
      System.out.println("         \\");
      System.out.println("   \\     /");    
      System.out.println("    -----");       
  }
}