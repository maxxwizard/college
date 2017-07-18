import java.util.*;

public class mod30WheelTrialDivision {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		System.out.print("What number do you wish to factor?: ");
		int num = input.nextInt();
		int numCopy = num;
		System.out.println();
		
		int[] spokes = new int[] {29, 23, 19, 17, 13, 11, 7};
		
		int i;
		
		System.out.print("Factors: ");
		
		while (num > 1) {
			
			for (i = 0; i < spokes.length; i++)
			{
				if (num % spokes[i] == 0) {
					System.out.print(spokes[i] + " ");
					num = num / spokes[i];
				}
			}
			
			if (i == spokes.length) { // if all spokes exhausted, number must be prime
				num = 0;
			}
			
		}
			
		if (num == 0)
			System.out.println(numCopy + " is prime.");
			

	}

}
