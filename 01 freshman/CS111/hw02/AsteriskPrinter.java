public class AsteriskPrinter {
 
  public static void main(String[] args) 
  {
for (int i = 0; i < 6; i++)
{
  for (int j = 0; j < i; j++)
    System.out.print(" ");
  for (int k = 0; k < 6 - i; k++)
    System.out.print("*");
  System.out.println();
}
  }
  
}