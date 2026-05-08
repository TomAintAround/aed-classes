import java.util.ArrayList;
import java.util.Objects;

public class BinarySearchTree {
	private Node root;

	public BinarySearchTree(Transaction transaction) {
		Objects.requireNonNull(transaction, "Root cannot be null");
		root = new Node(transaction);
	}

	public void insert(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");

		Node newNode = new Node(transaction);

		if (root == null) {
			root = newNode;
			return;
		}

		Node curr = root;
		Node parent = null;

		while (curr != null) {
			parent = curr;

			if (transaction.getValue() < curr.getValue()) {
				curr = curr.getLeft();
			} else if (transaction.getValue() > curr.getValue()) {
				curr = curr.getRight();
			} else {
				curr.insertTransaction(transaction);
				return;
			}
		}

		if (transaction.getValue() < parent.getValue())
			parent.setLeft(newNode);
		else
			parent.setRight(newNode);
	}

	public ArrayList<Integer> intervalSearch(double minValue, double maxValue, int minRisk) {
		ArrayList<Integer> list = new ArrayList<>();
		Node curr = root;

		while (curr != null) {
			double value = curr.getValue();

			if (value >= minValue && value <= maxValue) {
				for (Transaction t : curr.getTransactions(minRisk))
					list.add(t.getId());

				Node left = curr.getLeft();
				while (left != null) {
					double lv = left.getValue();

					if (lv >= minValue && lv <= maxValue) {
						for (Transaction t : left.getTransactions(minRisk))
							list.add(t.getId());
					}

					if (lv > minValue)
						left = left.getLeft();
					else
						left = left.getRight();
				}

				curr = curr.getRight();
			} else if (value < minValue) {
				curr = curr.getRight();
			} else {
				curr = curr.getLeft();
			}
		}

		return list;
	}

	public void remove(double value) {
		Node curr = root;
		Node parent = null;

		while (curr != null && curr.getValue() != value) {
			parent = curr;
			if (value < curr.getValue())
				curr = curr.getLeft();
			else
				curr = curr.getRight();
		}

		if (curr == null)
			return;

		if (curr.getLeft() == null || curr.getRight() == null) {
			Node child = (curr.getLeft() != null) ? curr.getLeft() : curr.getRight();

			if (parent == null)
				root = child;
			else if (parent.getLeft() == curr)
				parent.setLeft(child);
			else
				parent.setRight(child);
		} else {
			Node succParent = curr;
			Node succ = findMin(curr.getRight());

			curr.setTransactions(succ.getTransactions());

			if (succParent.getLeft() == succ)
				succParent.setLeft(succ.getRight());
			else
				succParent.setRight(succ.getRight());
		}
	}

	private void preOrderTraverse(Node currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}

		preOrderTraverse(currNode.getLeft(), list);
		preOrderTraverse(currNode.getRight(), list);
	}

	private void inOrderTraverse(Node currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		inOrderTraverse(currNode.getLeft(), list);
		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}
		inOrderTraverse(currNode.getRight(), list);
	}

	private void postOrderTraverse(Node currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		postOrderTraverse(currNode.getLeft(), list);
		postOrderTraverse(currNode.getRight(), list);
		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}
	}

	public ArrayList<Transaction> traverse(TraverseType traverseType) {
		ArrayList<Transaction> list = new ArrayList<>();
		if (traverseType.equals(TraverseType.PreOrder))
			preOrderTraverse(root, list);
		else if (traverseType.equals(TraverseType.InOrder))
			inOrderTraverse(root, list);
		else
			postOrderTraverse(root, list);
		return list;
	}

	public Node findMax(Node node) {
		Node curr = node;
		if (curr == null)
			return null;

		while (curr.getRight() != null)
			curr = curr.getRight();

		return curr;
	}

	public Node findMax() {
		return findMax(root);
	}

	public Node findMin(Node node) {
		Node curr = node;
		if (curr == null)
			return null;

		while (curr.getLeft() != null)
			curr = curr.getLeft();

		return curr;
	}

	public Node findMin() {
		return findMin(root);
	}
}
