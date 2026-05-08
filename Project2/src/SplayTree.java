import java.util.ArrayList;
import java.util.Objects;

public class SplayTree {
    private SplayNode root;
    private int numRotations;

    public SplayTree(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        root = new SplayNode(transaction);
    }

    public int getNumRotations() {
        return numRotations;
    }

    private void splay(SplayNode node) {
        while (node.getParent() != null) {
            SplayNode parent = node.getParent();
            SplayNode grandparent = parent.getParent();

            if (grandparent == null) {
                if (node == parent.getLeft())
                    rightRotate(parent);
                else
                    leftRotate(parent);
            } else if (node == parent.getLeft() && parent == grandparent.getLeft()) {
                rightRotate(grandparent);
                rightRotate(parent);
            } else if (node == parent.getRight() && parent == grandparent.getRight()) {
                leftRotate(grandparent);
                leftRotate(parent);
            } else if (node == parent.getRight() && parent == grandparent.getLeft()) {
                leftRotate(parent);
                rightRotate(grandparent);
            } else {
                rightRotate(parent);
                leftRotate(grandparent);
            }
        }
    }

    public void insert(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");

        if (root == null) {
            root = new SplayNode(transaction);
            return;
        }

        SplayNode currNode = root;
        SplayNode parent = null;

        while (currNode != null) {
            parent = currNode;
            if (transaction.getValue() < currNode.getValue()) {
                currNode = currNode.getLeft();
            } else if (transaction.getValue() > currNode.getValue()) {
                currNode = currNode.getRight();
            } else {
                currNode.insertTransaction(transaction);
                splay(currNode);
                return;
            }
        }

        SplayNode newNode = new SplayNode(transaction);
        newNode.setParent(parent);

        if (transaction.getValue() < parent.getValue())
            parent.setLeft(newNode);
        else
            parent.setRight(newNode);

        splay(newNode);
    }

    public ArrayList<Integer> intervalSearch(double minValue, double maxValue, int minRisk) {
        ArrayList<Integer> list = new ArrayList<>();
        SplayNode curr = root;

        while (curr != null) {
            double value = curr.getValue();

            if (value >= minValue && value <= maxValue) {
                for (Transaction t : curr.getTransactions(minRisk))
                    list.add(t.getId());

                SplayNode left = curr.getLeft();
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

        SplayNode nodeToDelete = root;

        while (nodeToDelete != null && nodeToDelete.getValue() != value) {
            if (value < nodeToDelete.getValue())
                nodeToDelete = nodeToDelete.getLeft();
            else
                nodeToDelete = nodeToDelete.getRight();
        }

        if (nodeToDelete == null)
            return;

        splay(nodeToDelete);

        if (nodeToDelete.getLeft() == null) {
            root = nodeToDelete.getRight();
            if (root != null)
                root.setParent(null);
        } else {
            SplayNode maxLeft = findMax(nodeToDelete.getLeft());
            splay(maxLeft);
            maxLeft.setRight(nodeToDelete.getRight());
            if (nodeToDelete.getRight() != null)
                nodeToDelete.getRight().setParent(maxLeft);
            root = maxLeft;
            root.setParent(null);
        }
    }

    private void leftRotate(SplayNode node) {
        SplayNode prevRight = node.getRight();
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

    private void rightRotate(SplayNode node) {
        SplayNode prevLeft = node.getLeft();
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

    private void preOrderTraverse(SplayNode node, ArrayList<Transaction> list) {
        if (node == null)
            return;
        list.addAll(node.getTransactions());
        preOrderTraverse(node.getLeft(), list);
        preOrderTraverse(node.getRight(), list);
    }

    private void inOrderTraverse(SplayNode node, ArrayList<Transaction> list) {
        if (node == null)
            return;
        inOrderTraverse(node.getLeft(), list);
        list.addAll(node.getTransactions());
        inOrderTraverse(node.getRight(), list);
    }

    private void postOrderTraverse(SplayNode node, ArrayList<Transaction> list) {
        if (node == null)
            return;
        postOrderTraverse(node.getLeft(), list);
        postOrderTraverse(node.getRight(), list);
        list.addAll(node.getTransactions());
    }

    public ArrayList<Transaction> traverse(TraverseType type) {
        ArrayList<Transaction> list = new ArrayList<>();
        if (type == TraverseType.PreOrder)
            preOrderTraverse(root, list);
        else if (type == TraverseType.InOrder)
            inOrderTraverse(root, list);
        else
            postOrderTraverse(root, list);
        return list;
    }

    public SplayNode findMin(SplayNode node) {
        SplayNode curr = node;
        if (curr == null)
            return null;

        while (curr.getLeft() != null)
            curr = curr.getLeft();

        return curr;
    }

    public SplayNode findMin() {
        return findMin(root);
    }

    public SplayNode findMax(SplayNode node) {
        SplayNode curr = node;
        if (curr == null)
            return null;

        while (curr.getRight() != null)
            curr = curr.getRight();

        return curr;
    }

    public SplayNode findMax() {
        return findMax(root);
    }
}
