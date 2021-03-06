import java.util.concurrent.*;

public class Main {

	static int NUM_PROCESSES = 5;
	static int NUM_REQUESTS = 5;
	static Semaphore semaphores[];
	static volatile int turn = 0;

	public static void main(String[] args) {
		// process command line arguments
		if (args.length != 0 && args.length != 2)
		{
			System.err.println("usage: java Main <numOfProcesses=5> <numOfRequestsPerClient=5>");
			System.exit(1);
		}
		
		if (args.length == 2)
		{
			NUM_PROCESSES = Integer.parseInt(args[0]);
			NUM_REQUESTS = Integer.parseInt(args[1]);
		}
		
		// initialize N semaphores
		semaphores = new Semaphore[NUM_PROCESSES];
		for (int i = 0; i < NUM_PROCESSES; i++)
		{
			if (i == 0)
				semaphores[i] = new Semaphore(1);
			else
				semaphores[i] = new Semaphore(0);
		}
		
		// create threads
		MyThread[] myThreads = new MyThread[NUM_PROCESSES];
		for (int i = 0; i < myThreads.length; i++) {
			myThreads[i] = new MyThread(i, NUM_REQUESTS);
			myThreads[i].start();
		}
		
		// wait for threads to die
		for (int i = 0; i < myThreads.length; i++) {
			try {
				myThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * General idea is that a process should signal the next one.
	 * 
	 * wait(semaphores[pid])
	 * print statement
	 * turn = (turn + 1) % N
	 * signal(semaphores[turn])
	 */
	public static void TakeTurns(int pid)
	{
		try {
			semaphores[pid].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("It is the turn of process " + pid);
		
		turn = (turn + 1) % NUM_PROCESSES;
		
		semaphores[turn].release();
	}

}
