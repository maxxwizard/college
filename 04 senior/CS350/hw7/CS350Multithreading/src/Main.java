import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author Matthew Huynh
 * 
 */
public class Main {
	
	public static AtomicIntegerArray flag; // for use in Dekker's and Peterson's algorithms
	public static AtomicIntegerArray ticket; // for use in Bakery algorithm
	public static AtomicIntegerArray busyCounts; // busy loop count
	public static AtomicInteger turn = new AtomicInteger(); // default to 0
	public static volatile String output = ""; // output buffer
	static FileWrite fw;
	static final int N = 2; // threads [for bad, dekker, peterson]
	static final int M = 4; // threads [for bakery]

	public static void main(String[] args) {

		// simulate "bad" Threads
		simulateBadThreads();
		
		// simulate Dekker threads
		simulateDekkerThreads();
		
		// simulate Peterson threads
		simulatePetersonThreads();
		
		// simulate Bakery threads
		simulateBakeryThreads(M);
	}
	
	static void simulateBadThreads()
	{
		// reset variables
		BadThread[] badThreads = new BadThread[N];
		output = "";
		// simulate
		for (int i = 0; i < N; i++) {
			badThreads[i] = new BadThread(i);
			badThreads[i].start();
		}
		// wait for threads before flushing output
		for (BadThread bt : badThreads)
		{
			try {
				bt.join();
			} catch (InterruptedException e) {
			}
		}
		// flush output
		String badThreadsFile = "badThreads.txt";
		fw = new FileWrite(badThreadsFile);
		fw.writeAndClose(output);
		System.out.println("output written to " + badThreadsFile);
	}
	
	static void simulateDekkerThreads()
	{
		// reset variables
		DekkerThread[] dekkerThreads = new DekkerThread[N];
		flag = new AtomicIntegerArray(N);
		output = "";
		// simulate
		for (int i = 0; i < N; i++) {
			dekkerThreads[i] = new DekkerThread(i);
			dekkerThreads[i].start();
		}
		// wait for threads before flushing output
		for (DekkerThread bt : dekkerThreads)
		{
			try {
				bt.join();
			} catch (InterruptedException e) {
			}
		}
		// flush output
		String DekkerThreadsFile = "dekkerThreads.txt";
		fw = new FileWrite(DekkerThreadsFile);
		fw.writeAndClose(output);
		System.out.println("output written to " + DekkerThreadsFile);
	}
	
	static void simulatePetersonThreads()
	{
		// reset variables
		PetersonThread[] petersonThreads = new PetersonThread[N];
		flag = new AtomicIntegerArray(N);
		busyCounts = new AtomicIntegerArray(N);
		for (int i = 0; i < busyCounts.length(); i++)
		{
			busyCounts.set(i, 0);
		}
		output = "";
		// simulate
		for (int i = 0; i < N; i++) {
			petersonThreads[i] = new PetersonThread(i);
			petersonThreads[i].start();
		}
		// wait for threads before flushing output
		for (PetersonThread bt : petersonThreads)
		{
			try {
				bt.join();
			} catch (InterruptedException e) {
			}
		}
		// flush output
		String petersonThreadsFile = "petersonThreads.txt";
		fw = new FileWrite(petersonThreadsFile);
		fw.writeAndClose(output);
		System.out.println("output written to " + petersonThreadsFile);
		int busySum = 0;
		for (int i = 0; i < busyCounts.length(); i++)
		{
			busySum += busyCounts.get(i);
		}
		System.out.printf("average busy loop hits: %,.2f\n", (double)busySum/N);
	}
	
	static void simulateBakeryThreads(int num)
	{
		// reset variables
		BakeryThread[] bakeryThreads = new BakeryThread[num];
		ticket = new AtomicIntegerArray(num);
		for (int i = 0; i < ticket.length(); i++)
		{
			ticket.set(i, 0);
		}
		output = "";
		// simulate
		for (int i = 0; i < num; i++) {
			bakeryThreads[i] = new BakeryThread(i);
			bakeryThreads[i].start();
		}
		// wait for threads before flushing output
		for (BakeryThread bt : bakeryThreads)
		{
			try {
				bt.join();
			} catch (InterruptedException e) {
			}
		}
		// flush output
		String bakeryThreadsFile = "bakeryThreads.txt";
		fw = new FileWrite(bakeryThreadsFile);
		fw.writeAndClose(output);
		System.out.println("output written to " + bakeryThreadsFile);
	}
}
