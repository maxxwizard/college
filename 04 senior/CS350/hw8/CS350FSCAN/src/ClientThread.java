import java.util.Random;

public class ClientThread extends Thread {
	Random r;
	public int id;
	int numOfRequests;

	public ClientThread(int i, int numRequests) {
		id = i;
		r = new Random();
		numOfRequests = numRequests;
	}

	/* while (numOfRequests > 0)
	 * {
	 *   sleep for a random period of time
  	 *   request = a random int from 0 to 7
  	 *   (reader) acquire lock on buffer queue
  	 *   acquire mutexReqCounts
  	 *   reqCounts[request]++
  	 *   release mutexReqCounts
  	 *   signal totalReqCounter
  	 *   (reader) release lock on buffer queue
  	 *   wait on the semaphores[request]
  	 *   numOfRequests--;
  	 * }
	 */
	public void run() {
		while (numOfRequests > 0)
		{
			randomSleep();
			int request = r.nextInt(8);
			// get reader lock
			try {
				//printLine("obtaining reader lock");
				Main.no_writers.acquire();
				//printLine("obtained reader lock");
				Main.counter_mutex.acquire();
				int prev = Main.nreaders;
				Main.nreaders++;
				Main.counter_mutex.release();
				if (prev == 0)
					Main.no_readers.acquire();
				Main.no_writers.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// "read"
			try
			{
				Main.bufferQueue.mutexReqCounts.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.bufferQueue.reqCounts[request][this.id] = 1;
			printLine("requested angle " + request);
			Main.bufferQueue.mutexReqCounts.release();
			Main.totalReqCounter.release();
			// release reader lock
			try {
				Main.counter_mutex.acquire();
				//printLine("released reader lock");
				Main.nreaders--;
				int current = Main.nreaders;
				Main.counter_mutex.release();
				if (current == 0)
					Main.no_readers.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// wait on camera
			try {
				Main.bufferQueue.semaphores[request].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			numOfRequests--;
		}
	}
	
	void printLine(String s)
	{
		System.out.println("Thread " + this.id + " " + s);
	}
	
	void randomSleep()
	{
		try {
			Thread.sleep(r.nextInt(20));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}