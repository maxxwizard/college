public class PetersonThread extends MyThread {

	public PetersonThread(int i) {
		super(i);
	}

	@Override
	void EnterCS() {
		int i = super.id;
		int j = not(i);

		Main.turn.set(j); 
	    Main.flag.set(i, 1);
	    while (Main.flag.get(j) == 1 && Main.turn.get() == j) {
	    	Main.busyCounts.incrementAndGet(i);
	    }
	}

	@Override
	void ExitCS() {
		int i = super.id;

		Main.flag.set(i, 0);
	}

	private int not(int i) {
		if (i == 0)
			return 1;
		else
			return 0;
	}

}