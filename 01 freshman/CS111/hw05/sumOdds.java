import java.util.*;

public class sumOdds {

    public static void main(String[] args) {

    // System.out.println(sumOdds(3));
    
    Random rand = new Random();
    for (int i = 0; i < 20; i++) {
        int b = 5 * rand.nextInt(10);
        System.out.print(b + " ");
    }  
    
    }
    
    public static int sumOdds(int n) {
        int odd = 1;
        int sum = 0;
        int count = 1;

        while (count <= n) {
            sum = sum + odd;
            odd = odd + 2;
            count++;
        }
            
        return sum;
   }
}