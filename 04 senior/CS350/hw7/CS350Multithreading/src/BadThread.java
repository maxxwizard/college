
public class BadThread extends MyThread {

	public BadThread(int i) {
		super(i);
	}

	@Override
	void EnterCS() {
		// do nothing
	}

	@Override
	void ExitCS() {
		// do nothing
	}

}
