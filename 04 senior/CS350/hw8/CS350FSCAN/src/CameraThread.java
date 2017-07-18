
public class CameraThread extends Thread {
	public int numRequestsServed;
	public int currentAngle = 0;
	public enum Direction {UP, DOWN};
	Direction d = Direction.UP;
	
	public CameraThread() {
		numRequestsServed = 0;
	}

	/*
	 * wait on totalReqCounter
	 * swap queues
	 * process queue
	 */
	public void run() {
		while (true)
		{
			// wait on totalReqCounter
			try {
				Main.totalReqCounter.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// signal totalReqCounter to return it to its correct value
			Main.totalReqCounter.release();
			// (writer) acquire lock on buffer queue
			try {
				//printLine("obtaining writer lock");
				Main.no_writers.acquire();
				Main.no_readers.acquire();
				//printLine("obtained writer lock");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.no_readers.release();
			// swap buffer and process queues
			FscanQueue temp = Main.bufferQueue;
			Main.bufferQueue = Main.processQueue;
			Main.processQueue = temp;
			//printLine("swapped the two queues");
			// (writer) release lock on buffer queue
			Main.no_writers.release();
			//printLine("released writer lock");
			// process queue
			processQueue();
		}
	}
	
	/* 
	 * print both queues
	 * move camera to highest/lowest request in the current direction
	 * flip direction
	 * iterate up or down of new current direction
	 *   process each request
	 *   clear reqCounts[request][]
	 */
	void processQueue()
	{
		printBothQueues();
		
		currentAngle = getStartingPosition();
		
		d = (d == Direction.UP) ? Direction.DOWN : Direction.UP;
		
		System.out.println("Direction is " + d);
		
		if (d == Direction.UP)
		{
			for (int i = currentAngle; i < Main.processQueue.reqCounts.length; i++)
			{
				int waitingThreads = Main.sum(Main.processQueue.reqCounts[i]);
				if (waitingThreads > 0)
				{
					serviceRequest(i, waitingThreads);
				}
				for (int j = 0; j < Main.processQueue.reqCounts[i].length; j++)
				{
					Main.processQueue.reqCounts[i][j] = 0;
				}
			}
		} else {
			for (int i = currentAngle; i >= 0; i--)
			{
				int waitingThreads = Main.sum(Main.processQueue.reqCounts[i]);
				if (waitingThreads > 0)
				{
					serviceRequest(i, waitingThreads);
				}
				for (int j = 0; j < Main.processQueue.reqCounts[i].length; j++)
				{
					Main.processQueue.reqCounts[i][j] = 0;
				}
			}
		}
	}
	
	int getStartingPosition()
	{
		int startPos = -1;
		if (d == Direction.UP)
		{
			for (int i = Main.processQueue.reqCounts.length-1; i >= 0 && startPos == -1; i--)
			{
				if (Main.sum(Main.processQueue.reqCounts[i]) > 0)
					startPos = i;
			}
		} else {
			for (int i = 0; i < Main.processQueue.reqCounts.length && startPos == -1; i++)
			{
				if (Main.sum(Main.processQueue.reqCounts[i]) > 0)
					startPos = i;
			}
		}
		return startPos;
	}
	
	/*
	 * Delays the thread proportional to how much the camera has to move.
	 * Signal semaphores[requestedAngle] waitingThreads times (to wake up those clients).
	 * Acquire totalReqCounter waitingThreads times.
	 * Increment numRequestsServed waitingThreads times.
	 * Then set the current angle to the serviced angle.
	 */
	void serviceRequest(int requestedAngle, int waitingThreads)
	{
		int angleDifference = Math.abs(requestedAngle-currentAngle);
		try {
			Thread.sleep(angleDifference * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.processQueue.semaphores[requestedAngle].release(waitingThreads);
		try {
			Main.totalReqCounter.acquire(waitingThreads);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		currentAngle = requestedAngle;
		numRequestsServed += waitingThreads;
		printLine("serviced angle " + requestedAngle + " for client(s) " + clientIdsForAngle(requestedAngle));
	}
	
	String clientIdsForAngle(int requestedAngle)
	{
		String s = "";
		for (int i = 0; i < Main.processQueue.reqCounts[requestedAngle].length; i++)
		{
			if (Main.processQueue.reqCounts[requestedAngle][i] > 0)
			{
				if (s != "")
					s += ", ";
				s += i;
			}
		}
		return s;
	}
	
	void printLine(String s)
	{
		System.out.println("Camera " + s);
	}
	
	void printBothQueues()
	{
		System.out.print(" buffer: ");
		Main.bufferQueue.printQueue();
		System.out.print("process: ");
		Main.processQueue.printQueue();
	}
}