public class Transaction {
	private int id;
	private double value;
	private int risk;

	public Transaction(int id, double value, int risk) {
		if (risk < 0 || risk > 10)
			throw new IllegalArgumentException("Risk must be between 0 and 10");
		if (value <= 0)
			throw new IllegalArgumentException("Value must be positive");
		this.id = id;
		this.value = value;
		this.risk = risk;
	}

	public int getId() {
		return id;
	}

	public double getValue() {
		return value;
	}

	public int getRisk() {
		return risk;
	}

	public String toString() {
		return "(" + id + ", " + value + ", " + risk + ")";
	}
}
