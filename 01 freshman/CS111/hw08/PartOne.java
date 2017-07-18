/*
 * PartOne.java
 *
 * Computer Science 111, Boston University
 * 
 * A class that contains methods from Part I of PS 8.
 */

public class PartOne {
    public static void countDown(int n) {
        if (n <= 0) {
            return;
        }
        System.out.println(n);
        countDown(n - 1);
    }
    
    public static int mystery(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return 1 + mystery(a + 1, b - 2);
        }
    }

    public static boolean search(int num, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean searchRecursive(int num, int[] arr, int pos) {
        if (pos == arr.length)
            return false;
        
        if (arr[pos] == num)
            return true;
        
        return searchRecursive(num, arr, pos+1);
    }
}