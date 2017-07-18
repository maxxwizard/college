import java.util.*;
import java.io.*;

public class fileReader {

        public static int madness(int a, int b) {
            a++;
            b = 2*a - 3;
            System.out.println(a + " " + b);
            return b;
        }
        
        public static void main(String[] args) {
            int a = 1;
            int b = 3;
            a = madness(b, a);
            System.out.println(a + " " + b);
            b = madness(a, a) - 5;
            System.out.println(a + " " + b);
            madness(b, b);
            System.out.println(a + " " + b);
        }
        
}