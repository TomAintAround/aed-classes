import java.util.Objects;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class AVLTree {
	private AVLNode root;
	private int numRotations;

	public AVLTree(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		root = new AVLNode(transaction);
		numRotations = 0;
	}

	public int getNumRotations() {
		return numRotations;
	}

	public void insert(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");

		if (root == null) {
			root = new AVLNode(transaction);
			return;
		}

		Deque<AVLNode> path = new ArrayDeque<>();
		AVLNode curr = root;

		while (true) {
			path.push(curr);

			if (transaction.getValue() == curr.getValue()) {
				curr.insertTransaction(transaction);
				return;
			} else if (transaction.getValue() < curr.getValue()) {
				if (curr.getLeft() == null) {
					curr.setLeft(new AVLNode(transaction));
					break;
				}
				curr = curr.getLeft();
			} else {
				if (curr.getRight() == null) {
					curr.setRight(new AVLNode(transaction));
					break;
				}
				curr = curr.getRight();
			}
		}

		rebalancePath(path);
	}

	public ArrayList<Integer> intervalSearch(double minValue, double maxValue, int minRisk) {
		ArrayList<Integer> list = new ArrayList<>();
		AVLNode curr = root;

		while (curr != null) {
			double value = curr.getValue();

			if (value >= minValue && value <= maxValue) {
				for (Transaction t : curr.getTransactions(minRisk))
					list.add(t.getId());

				AVLNode left = curr.getLeft();
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
		if (root == null)
			return;

		Deque<AVLNode> path = new ArrayDeque<>();
		AVLNode curr = root;
		AVLNode target = null;

		while (curr != null) {
			if (value == curr.getValue()) {
				target = curr;
				break;
			}
			path.push(curr);
			curr = value < curr.getValue() ? curr.getLeft() : curr.getRight();
		}
		if (target == null)
			return;

		if (target.getLeft() != null && target.getRight() != null) {
			path.push(target);
			AVLNode successor = target.getRight();
			while (successor.getLeft() != null) {
				path.push(successor);
				successor = successor.getLeft();
			}
			target.setTransactions(successor.getTransactions());
			target = successor;
		}

		AVLNode replacement = (target.getLeft() != null) ? target.getLeft() : target.getRight();

		if (path.isEmpty()) {
			root = replacement;
		} else {
			AVLNode parent = path.peek();
			if (parent.getLeft() == target)
				parent.setLeft(replacement);
			else
				parent.setRight(replacement);
		}

		rebalancePath(path);
	}

	private void rebalancePath(Deque<AVLNode> path) {
		AVLNode child = null;

		while (!path.isEmpty()) {
			AVLNode node = path.pop();
			if (child != null) {
				if (child.getValue() < node.getValue())
					node.setLeft(child);
				else
					node.setRight(child);
			}

			child = rotate(node);
		}

		if (child != null)
			root = child;
	}

	private AVLNode rotate(AVLNode node) {
		node.updateHeight();
		int balanceFactor = node.getBalanceFactor();

		if (balanceFactor > 1) {
			if (node.getLeft().getBalanceFactor() < 0)
				node.setLeft(leftRotate(node.getLeft()));
			return rightRotate(node);
		} else if (balanceFactor < -1) {
			if (node.getRight().getBalanceFactor() > 0)
				node.setRight(rightRotate(node.getRight()));
			return leftRotate(node);
		}

		return node;
	}

	private AVLNode leftRotate(AVLNode node) {
		AVLNode prevRight = node.getRight();
		AVLNode prevRightLeft = prevRight.getLeft();

		prevRight.setLeft(node);
		node.setRight(prevRightLeft);

		node.updateHeight();
		prevRight.updateHeight();

		numRotations++;
		return prevRight;
	}

	private AVLNode rightRotate(AVLNode node) {
		AVLNode prevLeft = node.getLeft();
		AVLNode prevLeftRight = prevLeft.getRight();

		prevLeft.setRight(node);
		node.setLeft(prevLeftRight);

		node.updateHeight();
		prevLeft.updateHeight();

		numRotations++;
		return prevLeft;
	}

	private void preOrderTraverse(AVLNode currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}

		preOrderTraverse(currNode.getLeft(), list);
		preOrderTraverse(currNode.getRight(), list);
	}

	private void inOrderTraverse(AVLNode currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		inOrderTraverse(currNode.getLeft(), list);
		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}
		inOrderTraverse(currNode.getRight(), list);
	}

	private void postOrderTraverse(AVLNode currNode, ArrayList<Transaction> list) {
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

	public AVLNode findMax(AVLNode node) {
		AVLNode curr = node;
		if (curr == null)
			return null;

		while (curr.getRight() != null)
			curr = curr.getRight();

		return curr;
	}

	public AVLNode findMax() {
		return findMax(root);
	}

	public AVLNode findMin(AVLNode node) {
		AVLNode curr = node;
		if (curr == null)
			return null;

		while (curr.getLeft() != null)
			curr = curr.getLeft();

		return curr;
	}

	public AVLNode findMin() {
		return findMin(root);
	}
}
