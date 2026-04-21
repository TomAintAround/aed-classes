import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class RedBlackNode {
	private PriorityQueue<Transaction> transactions = new PriorityQueue<>(
			Comparator.comparingInt(Transaction::getRisk));
	private boolean red;
	private RedBlackNode left;
	private RedBlackNode right;
	private RedBlackNode parent;

	public RedBlackNode(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		transactions.add(transaction);
		red = true;
		left = null;
		right = null;
		parent = null;
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

	public RedBlackNode getLeft() {
		return left;
	}

	public RedBlackNode getRight() {
		return right;
	}

	public RedBlackNode getParent() {
		return parent;
	}

	public boolean isRed() {
		return red;
	}

	public void setLeft(RedBlackNode node) {
		left = node;
		if (node != null)
			node.parent = this;
	}

	public void setRight(RedBlackNode node) {
		right = node;
		if (node != null)
			node.parent = this;
	}

	public void setParent(RedBlackNode node) {
		parent = node;
	}

	public void setRed() {
		red = true;
	}

	public void setBlack() {
		red = false;
	}
}
