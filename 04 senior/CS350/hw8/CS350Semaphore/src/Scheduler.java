
public class Scheduler extends Thread {
	

	public Scheduler() {
	}

	public void run() {
		while (true)
		{
			// block on semaphore S
			try {
				Main.S.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// make sure CS is clear
			try {
				//System.out.println("waiting for CS to clear");
				Main.mutexCS.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// select process Pi with smallest index i
			// for which V[i] is "true" and wake it by
			// signaling B[i]
			try {
				Main.mutexV.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean scheduled = false;
			for (int i = 0; i < Main.V.length && !scheduled; i++)
			{
				if (Main.V[i] == 1)
				{
					// wake a thread into the CS
					//System.out.println("scheduling thread " + i);
					Main.B[i].release();
					scheduled = true;
				}
			}
			Main.mutexV.release();
		}
	}
}