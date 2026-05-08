import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class AVLNode {
	private PriorityQueue<Transaction> transactions = new PriorityQueue<>(
			Comparator.comparingInt(Transaction::getRisk));
	private int height;
	private AVLNode left;
	private AVLNode right;

	public AVLNode(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		transactions.add(transaction);
		height = 1;
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

	public AVLNode getLeft() {
		return left;
	}

	public AVLNode getRight() {
		return right;
	}

	public void setLeft(AVLNode node) {
		left = node;
	}

	public void setRight(AVLNode node) {
		right = node;
	}

	public int getHeight() {
		return height;
	}

	public void updateHeight() {
		int leftHeight = 0;
		int rightHeight = 0;
		if (getLeft() != null)
			leftHeight = getLeft().getHeight();
		if (getRight() != null)
			rightHeight = getRight().getHeight();

		height = 1 + Math.max(leftHeight, rightHeight);
	}

	public int getBalanceFactor() {
		int leftHeight = 0;
		int rightHeight = 0;
		if (getLeft() != null)
			leftHeight = getLeft().getHeight();
		if (getRight() != null)
			rightHeight = getRight().getHeight();

		return leftHeight - rightHeight;
	}
}
