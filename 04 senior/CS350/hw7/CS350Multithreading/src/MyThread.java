import java.util.*;

public abstract class MyThread extends Thread {
	public int id;
	Random rand;

	public MyThread(int i) {
		id = i;
		rand = new Random();
	}

	public void run() {
		for (int i = 1; i <= 5; i++) {
			EnterCS();
			Main.output += "Thread " + this.id + " is starting iteration " + i + "#";
			doWork();
			Main.output += "Thread " + this.id + " is done with iteration " + i + "#";
			ExitCS();
		}
	}
	
	abstract void EnterCS();
	abstract void ExitCS();
	
	void doWork()
	{
		this.randomSleep();
		Main.output += "We hold these truths to be self-evident, that all men are created equal," + "#";
		this.randomSleep();
		Main.output += "that they are endowed by their Creator with certain unalienable Rights," + "#";
		this.randomSleep();
		Main.output += "that among these are Life, Liberty and the pursuit of Happiness." + "#";
		this.randomSleep();
	}
	
	private void randomSleep()
	{
		try {
			Thread.sleep(rand.nextInt(20)+1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}