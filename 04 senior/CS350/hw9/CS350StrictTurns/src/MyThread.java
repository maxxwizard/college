
public class MyThread extends Thread {
	
	public int id;
	public int numRequests;

	public MyThread(int i, int numOfRequests) {
		id = i;
		numRequests = numOfRequests;
	}

	public void run() {
		for (int i = 1; i <= numRequests; i++) {
			Main.TakeTurns(id);
		}
	}
}