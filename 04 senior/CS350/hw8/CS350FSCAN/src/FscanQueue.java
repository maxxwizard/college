import java.util.concurrent.Semaphore;

public class FscanQueue {
	
	public Semaphore[] semaphores;
	public Semaphore mutexReqCounts;
	public int[][] reqCounts; // 2D array, 1st dimension is angle, 2nd dimension is thread number
	
	public FscanQueue(int numCameraAngles, int numClientThreads)
	{
		mutexReqCounts = new Semaphore(1, false);
		reqCounts = new int[numCameraAngles][numClientThreads];
		semaphores = new Semaphore[numCameraAngles];
		for (int i = 0; i < numCameraAngles; i++)
		{
			semaphores[i] = new Semaphore(0, false);
			for (int j = 0; j < numClientThreads; j++)
			{
				reqCounts[i][j] = 0;
			}
		}
	}
	
	public void printQueue()
	{
		String s = "";
		for (int i = 0; i < reqCounts.length; i++)
		{
			if (s != "")
				s += "-";
			s += Main.sum(reqCounts[i]);
		}
		System.out.println("[" + s + "]");
	}
}
