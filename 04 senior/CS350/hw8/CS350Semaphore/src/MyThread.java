
public class MyThread extends Thread {
	
	public int id;

	public MyThread(int i) {
		id = i;
	}

	public void run() {
		for (int i = 1; i <= 5; i++) {
			EnterCS();
			doWork();
			ExitCS();
		}
	}
	
	void EnterCS()
	{
		printLine("is requesting CS");
		
		// signal intent
		try {
			Main.mutexV.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.V[id] = 1;
		Main.mutexV.release();
		
		//printLine("signalling scheduler");
		Main.S.release(); // signal scheduler
		
		try {
			Main.B[id].acquire(); // wait until scheduled
			//printLine("got scheduled");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void ExitCS()
	{
		printLine("is exiting the CS");
		
		// signal that we're no longer interested in the CS
		try {
			Main.mutexV.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.V[id] = 0;
		Main.mutexV.release();
		
		// signal that we're out of the CS
		Main.mutexCS.release();
	}
	
	void doWork()
	{
		printLine("is in the CS");
	}
	
	void printLine(String s)
	{
		System.out.println("Thread " + this.id + " " + s);
	}
}