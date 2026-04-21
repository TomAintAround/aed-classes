import java.util.Objects;
import java.util.ArrayList;

public class RedBlackTree {
	private RedBlackNode root;
	private int numRotations;

	public RedBlackTree(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		root = new RedBlackNode(transaction);
		root.setBlack();
		numRotations = 0;
	}

	public int getNumRotations() {
		return numRotations;
	}

	public void insert(Transaction transaction) {
		Objects.requireNonNull(transaction, "Transaction cannot be null");
		RedBlackNode newNode;

		RedBlackNode parent = null;
		RedBlackNode currNode = root;

		while (currNode != null && transaction.getValue() != currNode.getValue()) {
			parent = currNode;
			if (transaction.getValue() < currNode.getValue())
				currNode = currNode.getLeft();
			else
				currNode = currNode.getRight();
		}

		if (currNode != null) {
			currNode.insertTransaction(transaction);
			return;
		}

		newNode = new RedBlackNode(transaction);
		newNode.setParent(parent);

		if (parent == null) {
			root = newNode;
			newNode.setBlack();
			return;
		} else if (transaction.getValue() < parent.getValue())
			parent.setLeft(newNode);
		else
			parent.setRight(newNode);

		if (newNode.getParent().getParent() == null)
			return;

		currNode = newNode;
		while (currNode != root && currNode.getParent().isRed()) {
			if (currNode.getParent() == currNode.getParent().getParent().getLeft()) {
				RedBlackNode uncleNode = currNode.getParent().getParent().getRight();

				if (isRed(uncleNode)) {
					currNode.getParent().setBlack();
					uncleNode.setBlack();
					currNode.getParent().getParent().setRed();
					currNode = currNode.getParent().getParent();
				} else {
					if (currNode == currNode.getParent().getRight()) {
						currNode = currNode.getParent();
						leftRotate(currNode);
					}

					currNode.getParent().setBlack();
					currNode.getParent().getParent().setRed();
					rightRotate(currNode.getParent().getParent());
				}
			} else {
				RedBlackNode uncleNode = currNode.getParent().getParent().getLeft();

				if (isRed(uncleNode)) {
					currNode.getParent().setBlack();
					uncleNode.setBlack();
					currNode.getParent().getParent().setRed();
					currNode = currNode.getParent().getParent();
				} else {
					if (currNode == currNode.getParent().getLeft()) {
						currNode = currNode.getParent();
						rightRotate(currNode);
					}

					currNode.getParent().setBlack();
					currNode.getParent().getParent().setRed();
					leftRotate(currNode.getParent().getParent());
				}
			}
		}
		root.setBlack();
	}

	public ArrayList<Integer> intervalSearch(double minValue, double maxValue, int minRisk) {
		ArrayList<Integer> list = new ArrayList<>();
		RedBlackNode curr = root;

		while (curr != null) {
			double value = curr.getValue();

			if (value >= minValue && value <= maxValue) {
				for (Transaction transaction : curr.getTransactions(minRisk))
					list.add(transaction.getId());

				RedBlackNode left = curr.getLeft();
				while (left != null) {
					double leftValue = left.getValue();

					if (leftValue >= minValue && leftValue <= maxValue) {
						for (Transaction transaction : left.getTransactions(minRisk))
							list.add(transaction.getId());
					}

					if (leftValue > minValue)
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

		RedBlackNode nodeToDelete = root;

		while (nodeToDelete != null && nodeToDelete.getValue() != value) {
			if (value < nodeToDelete.getValue())
				nodeToDelete = nodeToDelete.getLeft();
			else
				nodeToDelete = nodeToDelete.getRight();
		}

		if (nodeToDelete == null)
			return;

		RedBlackNode replacementNode = nodeToDelete;
		boolean nodeWasBlack = !nodeToDelete.isRed();
		RedBlackNode replacementChildNode;

		if (nodeToDelete.getLeft() == null) {
			replacementChildNode = nodeToDelete.getRight();
			transplant(nodeToDelete, replacementChildNode);
		} else if (nodeToDelete.getRight() == null) {
			replacementChildNode = nodeToDelete.getLeft();
			transplant(nodeToDelete, replacementChildNode);
		} else {
			replacementNode = findMax(nodeToDelete.getLeft());
			nodeWasBlack = !replacementNode.isRed();
			replacementChildNode = replacementNode.getLeft();

			if (replacementNode.getParent() != nodeToDelete) {
				transplant(replacementNode, replacementChildNode);
				replacementNode.setLeft(nodeToDelete.getLeft());
			}

			transplant(nodeToDelete, replacementNode);
			replacementNode.setRight(nodeToDelete.getRight());
			if (nodeToDelete.isRed())
				replacementNode.setRed();
			else
				replacementNode.setBlack();
		}

		if (nodeWasBlack)
			deleteFixup(replacementChildNode);
	}

	private void transplant(RedBlackNode nodeOut, RedBlackNode nodeIn) {
		if (nodeOut.getParent() == null)
			root = nodeIn;
		else if (nodeOut == nodeOut.getParent().getLeft())
			nodeOut.getParent().setLeft(nodeIn);
		else
			nodeOut.getParent().setRight(nodeIn);
	}

	private void deleteFixup(RedBlackNode currNode) {
		while (currNode != root && isBlack(currNode)) {
			RedBlackNode parent = currNode != null ? currNode.getParent() : null;
			if (parent == null)
				break;

			if (currNode == parent.getLeft()) {
				RedBlackNode siblingNode = parent.getRight();

				if (isRed(siblingNode)) {
					siblingNode.setBlack();
					parent.setRed();
					leftRotate(parent);
					siblingNode = parent.getRight();
				}

				if (siblingNode == null) {
					currNode = parent;
					continue;
				}

				if (isBlack(siblingNode.getLeft()) && isBlack(siblingNode.getRight())) {
					siblingNode.setRed();
					currNode = parent;
				} else {
					if (isBlack(siblingNode.getRight())) {
						if (siblingNode.getLeft() != null)
							siblingNode.getLeft().setBlack();
						siblingNode.setRed();
						rightRotate(siblingNode);
						siblingNode = parent.getRight();
					}

					siblingNode.setRed();
					parent.setBlack();
					if (siblingNode.getRight() != null)
						siblingNode.getRight().setBlack();
					leftRotate(parent);
					currNode = root;
				}
			} else if (currNode != null) {
				RedBlackNode siblingNode = parent.getLeft();

				if (isRed(siblingNode)) {
					siblingNode.setBlack();
					parent.setRed();
					rightRotate(parent);
					siblingNode = parent.getLeft();
				}

				if (siblingNode == null) {
					currNode = parent;
					continue;
				}

				if (isBlack(siblingNode.getLeft()) && isBlack(siblingNode.getRight())) {
					siblingNode.setRed();
					currNode = parent;
				} else {
					if (isBlack(siblingNode.getLeft())) {
						if (siblingNode.getRight() != null)
							siblingNode.getRight().setBlack();
						siblingNode.setRed();
						leftRotate(siblingNode);
						siblingNode = parent.getLeft();
					}

					siblingNode.setRed();
					parent.setBlack();
					if (siblingNode.getLeft() != null)
						siblingNode.getLeft().setBlack();
					rightRotate(parent);
					currNode = root;
				}
			}
		}

		if (currNode != null)
			currNode.setBlack();
	}

	private void leftRotate(RedBlackNode node) {
		RedBlackNode prevRight = node.getRight();
		if (prevRight == null)
			return;

		node.setRight(prevRight.getLeft());
		if (node.getParent() == null) {
			root = prevRight;
			prevRight.setParent(null);
		} else if (node == node.getParent().getLeft()) {
			node.getParent().setLeft(prevRight);
		} else {
			node.getParent().setRight(prevRight);
		}
		prevRight.setLeft(node);
		numRotations++;
	}

	private void rightRotate(RedBlackNode node) {
		RedBlackNode prevLeft = node.getLeft();
		node.setLeft(prevLeft.getRight());
		if (node.getParent() == null) {
			root = prevLeft;
			prevLeft.setParent(null);
		} else if (node == node.getParent().getLeft()) {
			node.getParent().setLeft(prevLeft);
		} else {
			node.getParent().setRight(prevLeft);
		}
		prevLeft.setRight(node);
		numRotations++;
	}

	private boolean isBlack(RedBlackNode node) {
		return node == null || !node.isRed();
	}

	private boolean isRed(RedBlackNode node) {
		return node != null && node.isRed();
	}

	private void preOrderTraverse(RedBlackNode currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}

		preOrderTraverse(currNode.getLeft(), list);
		preOrderTraverse(currNode.getRight(), list);
	}

	private void inOrderTraverse(RedBlackNode currNode, ArrayList<Transaction> list) {
		if (currNode == null)
			return;

		inOrderTraverse(currNode.getLeft(), list);
		for (Transaction transaction : currNode.getTransactions()) {
			list.add(transaction);
		}
		inOrderTraverse(currNode.getRight(), list);
	}

	private void postOrderTraverse(RedBlackNode currNode, ArrayList<Transaction> list) {
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

	public RedBlackNode findMax(RedBlackNode node) {
		RedBlackNode curr = node;
		if (curr == null)
			return null;

		while (curr.getRight() != null)
			curr = curr.getRight();

		return curr;
	}

	public RedBlackNode findMax() {
		return findMax(root);
	}

	public RedBlackNode findMin(RedBlackNode node) {
		RedBlackNode curr = node;
		if (curr == null)
			return null;

		while (curr.getLeft() != null)
			curr = curr.getLeft();

		return curr;
	}

	public RedBlackNode findMin() {
		return findMin(root);
	}
}
