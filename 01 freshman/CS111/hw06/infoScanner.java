import java.util.*;

public class infoScanner {

    public static void main(String[] args) {
        
        Scanner console = new Scanner(System.in);
        System.out.print("Enter you ID number: ");
        int idNumber = console.nextInt();
        console.nextLine();
        System.out.print("Enter your full name: ");
        String fullName = console.nextLine();
        System.out.print("Enter your monthly income: ");
        double income = console.nextDouble();
        
        System.out.println(idNumber);
        System.out.println(fullName);
        System.out.println(income);

   }
}