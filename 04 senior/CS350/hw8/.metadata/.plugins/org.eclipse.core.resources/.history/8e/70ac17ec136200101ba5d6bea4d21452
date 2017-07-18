import java.util.concurrent.*;

public class Main {

	static int[] V;
	static Semaphore[] B;
	static Semaphore mutexV = new Semaphore(1, false);
	//static Semaphore mutexB = new Semaphore(1, false);
	static Semaphore S = new Semaphore(0, false);
	static Semaphore mutexCS = new Semaphore(1, false);
	final static int numProcesses = 5;
	static Scheduler SCHED;
	
	public static void main(String[] args) {
		V = new int[numProcesses];
		B = new Semaphore[numProcesses];
		SCHED = new Scheduler();
		SCHED.start();
		for (int i = 0; i < numProcesses; i++)
		{
			V[i] = 0;
			B[i] = new Semaphore(0, false);
		}
		MyThread[] myThreads = new MyThread[numProcesses];
		for (int i = 0; i < myThreads.length; i++) {
			myThreads[i] = new MyThread(i);
			myThreads[i].start();
		}
		// wait for processes to die
		for (int i = 0; i < myThreads.length; i++) {
			try {
				myThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// kill the scheduler
		SCHED.stop();
	}

}
