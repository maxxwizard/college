import java.util.concurrent.*;

public class Main {
	
	static int numClients = 5;
	static int numRequests = 3;
	final static int numCameraAngles = 8;
	
	// for readers-writers (readers = clients, writer = camera) 
	static Semaphore no_writers = new Semaphore(1, false);
	static Semaphore no_readers = new Semaphore(1, false);
	static Semaphore counter_mutex = new Semaphore(1, false);
	static int nreaders = 0;
	
	static Semaphore totalReqCounter = new Semaphore(0, false); // used to denote the number of requests pending (in both queues)
	static Semaphore mutexBufferQueue = new Semaphore(1, false); // used to hold the buffer queue for swapping
	static FscanQueue bufferQueue;
	static FscanQueue processQueue;

	public static void main(String[] args) {
		
		if (args.length != 0 && args.length != 2)
		{
			System.err.println("usage: java Main <numOfClients> <numOfRequestsPerClient>");
			System.exit(1);
		}
		
		if (args.length == 2)
		{
			numClients = Integer.parseInt(args[0]);
			numRequests = Integer.parseInt(args[1]);
		}
		
		// initialize synchronization variables
		bufferQueue = new FscanQueue(numCameraAngles, numClients); 	// at start, set 1st queue to buffer (clients using this queue)
		processQueue = new FscanQueue(numCameraAngles, numClients); // and 2nd queue to process (camera using this queue)
		
		// start camera thread
		CameraThread cameraThread = new CameraThread();
		cameraThread.start();
		
		// start client threads
		ClientThread[] clientThreads = new ClientThread[numClients];
		for (int i = 0; i < numClients; i++)
		{
			clientThreads[i] = new ClientThread(i, numRequests);
			clientThreads[i].start();
		}
		
		// wait for client threads to finish
		for (int i = 0; i < numClients; i++)
		{
			try {
				clientThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// display how many requests were served (should equal to numClients*numRequests)
		System.out.println("\nexpected requests: " + numClients*numRequests);
		System.out.println("serviced requests: " + cameraThread.numRequestsServed);
		
		// stop camera thread
		cameraThread.stop();

	}
	
	static public int sum(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i++)
		{
			sum += array[i];
		}
		return sum;
	}

}
