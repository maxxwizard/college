public class BakeryThread extends MyThread {

	public BakeryThread(int i) {
		super(i);
	}

	@Override
	void EnterCS() {
		int i = super.id;

		Main.ticket.set(i, 1);
		Main.ticket.set(i, getMaxTicket());
		for (int j = 0; j < Main.ticket.length(); j++) {
			while (Main.ticket.get(j) != 0
					&& ((Main.ticket.get(j) < Main.ticket.get(i)) ||
							(Main.ticket.get(j) == Main.ticket.get(i) && j < i))) {
			}
		}
	}

	@Override
	void ExitCS() {
		int i = super.id;

		Main.ticket.set(i, 0);
	}

	private int getMaxTicket() {
		int max = 1;
		for (int i = 0; i < Main.ticket.length(); i++) {
			if (Main.ticket.get(i) > max) {
				max = Main.ticket.get(i);
			}
		}
		return max + 1;
	}

}