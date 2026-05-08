import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class SplayNode {
    private PriorityQueue<Transaction> transactions = new PriorityQueue<>(
            Comparator.comparingInt(Transaction::getRisk));
    private SplayNode left;
    private SplayNode right;
    private SplayNode parent;

    public SplayNode(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");
        transactions.add(transaction);
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public ArrayList<Transaction> getTransactions(int minRisk) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions)
            if (transaction.getRisk() >= minRisk)
                result.add(transaction);
        return result;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        Objects.requireNonNull(transactions, "Transactions cannot be null");
        if (transactions.isEmpty())
            throw new IllegalStateException("There must be at least 1 transaction");

        this.transactions.clear();
        for (Transaction t : transactions)
            this.transactions.add(t);
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

    public SplayNode getLeft() {
        return left;
    }

    public SplayNode getRight() {
        return right;
    }

    public SplayNode getParent() {
        return parent;
    }

    public void setLeft(SplayNode node) {
        left = node;
        if (node != null)
            node.parent = this;
    }

    public void setRight(SplayNode node) {
        right = node;
        if (node != null)
            node.parent = this;
    }

    public void setParent(SplayNode node) {
        parent = node;
    }
}
