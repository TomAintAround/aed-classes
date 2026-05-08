import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class Node {
	private PriorityQueue<Transaction> transactions = new PriorityQueue<>(
			Comparator.comparingInt(Transaction::getRisk));
	private Node left;
	private Node right;

	public Node(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		transactions.add(transaction);
		left = null;
		right = null;
	}

	public ArrayList<Transaction> getTransactions() {
		return new ArrayList<>(transactions);
	}

	public ArrayList<Transaction> getTransactions(int minRisk) {
		ArrayList<Transaction> result = new ArrayList<>();
		for (Transaction transaction : transactions) {
			if (transaction.getRisk() >= minRisk)
				result.add(transaction);
		}
		return result;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		Objects.requireNonNull(transactions, "Transactions cannot be null");
		if (transactions.isEmpty())
			throw new IllegalStateException("There must be at least 1 transaction");

		this.transactions.clear();
		for (Transaction transaction : transactions) {
			this.transactions.add(transaction);
		}
	}

	public void insertTransaction(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		transactions.add(transaction);
	}

	public double getValue() {
		if (transactions.isEmpty())
			throw new IllegalStateException("Node has no transactions");
		return transactions.element().getValue();
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public void setLeft(Node node) {
		left = node;
	}

	public void setRight(Node node) {
		right = node;
	}
}
