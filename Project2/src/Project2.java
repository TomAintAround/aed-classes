import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Project2 {
	public static void main(String[] args) {
		String result = "";

		ArrayList<Transaction[]> linearTransactions = new ArrayList<>();
		linearTransactions.add(generateOrderedTransactions(100_000));
		linearTransactions.add(generateOrderedTransactions(100_000));
		linearTransactions.add(generateOrderedTransactions(100_000));
		linearTransactions.add(generateOrderedTransactions(250_000));
		linearTransactions.add(generateOrderedTransactions(250_000));
		linearTransactions.add(generateOrderedTransactions(250_000));
		linearTransactions.add(generateOrderedTransactions(500_000));
		linearTransactions.add(generateOrderedTransactions(500_000));
		linearTransactions.add(generateOrderedTransactions(500_000));
		linearTransactions.add(generateOrderedTransactions(750_000));
		linearTransactions.add(generateOrderedTransactions(750_000));
		linearTransactions.add(generateOrderedTransactions(750_000));
		linearTransactions.add(generateOrderedTransactions(1000_000));
		linearTransactions.add(generateOrderedTransactions(1000_000));
		linearTransactions.add(generateOrderedTransactions(1000_000));

		ArrayList<Transaction[]> randomTransactions = new ArrayList<>();
		randomTransactions.add(generateRandomTransactions(100_000));
		randomTransactions.add(generateRandomTransactions(100_000));
		randomTransactions.add(generateRandomTransactions(100_000));
		randomTransactions.add(generateRandomTransactions(250_000));
		randomTransactions.add(generateRandomTransactions(250_000));
		randomTransactions.add(generateRandomTransactions(250_000));
		randomTransactions.add(generateRandomTransactions(500_000));
		randomTransactions.add(generateRandomTransactions(500_000));
		randomTransactions.add(generateRandomTransactions(500_000));
		randomTransactions.add(generateRandomTransactions(750_000));
		randomTransactions.add(generateRandomTransactions(750_000));
		randomTransactions.add(generateRandomTransactions(750_000));
		randomTransactions.add(generateRandomTransactions(1000_000));
		randomTransactions.add(generateRandomTransactions(1000_000));
		randomTransactions.add(generateRandomTransactions(1000_000));

		result += log("Árvore binária (linear)\n");
		for (Transaction[] transactions : linearTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			BinarySearchTree binarySearchTree = new BinarySearchTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				binarySearchTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				binarySearchTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				binarySearchTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");
		}
		result += log("\n");

		result += log("Árvore AVL (linear)\n");
		for (Transaction[] transactions : linearTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			AVLTree avlTree = new AVLTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				avlTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = avlTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				avlTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) +
						1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				avlTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = avlTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		result += log("Árvore Vermelha e Preta (linear)\n");
		for (Transaction[] transactions : linearTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			RedBlackTree redBlackTree = new RedBlackTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				redBlackTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = redBlackTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				redBlackTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				redBlackTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = redBlackTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		result += log("Árvore Splay (linear)\n");
		for (Transaction[] transactions : linearTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			SplayTree splayTree = new SplayTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				splayTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = splayTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				splayTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				splayTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = splayTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		result += log("Árvore binária (aleatório)\n");
		for (Transaction[] transactions : randomTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			BinarySearchTree binarySearchTree = new BinarySearchTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				binarySearchTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				binarySearchTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				binarySearchTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");
		}
		result += log("\n");

		result += log("Árvore AVL (aleatório)\n");
		for (Transaction[] transactions : randomTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			AVLTree avlTree = new AVLTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				avlTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = avlTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				avlTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				avlTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = avlTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		result += log("Árvore Vermelha e Preta (aleatório)\n");
		for (Transaction[] transactions : randomTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			RedBlackTree redBlackTree = new RedBlackTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				redBlackTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = redBlackTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				redBlackTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				redBlackTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = redBlackTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		result += log("Árvore Splay (aleatório)\n");
		for (Transaction[] transactions : randomTransactions) {
			result += log("Inserção de " + transactions.length + " elementos: ");
			long start = System.nanoTime();
			SplayTree splayTree = new SplayTree(transactions[0]);
			for (int i = 1; i < transactions.length; i++)
				splayTree.insert(transactions[i]);
			long end = System.nanoTime();
			double elapsedSeconds = (end - start) / 1_000_000_000.0;
			int numRotations = splayTree.getNumRotations();
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");

			int numSearches = 10_000;
			double[][] intervals = new double[numSearches][2];
			for (int i = 0; i < numSearches; i++) {
				intervals[i][0] = (i / numSearches) * transactions.length;
				intervals[i][1] = ((i + 1) / numSearches) * transactions.length;
			}
			result += log(numSearches + " consultas por intervalo: ");
			start = System.nanoTime();
			for (double[] interval : intervals)
				splayTree.intervalSearch(interval[0], interval[1], (int) (Math.random() * 10) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			result += log(elapsedSeconds + " s\n");

			int numDeletions = (int) (transactions.length * 0.2);
			result += log(numDeletions + " remoções: ");
			start = System.nanoTime();
			for (int i = 0; i < numDeletions; i++)
				splayTree.remove((int) (Math.random() * transactions.length) + 1);
			end = System.nanoTime();
			elapsedSeconds = (end - start) / 1_000_000_000.0;
			numRotations = splayTree.getNumRotations() - numRotations;
			result += log(elapsedSeconds + " s (" + numRotations + " rotações)\n");
		}
		result += log("\n");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
			writer.write(result);
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Transaction[] generateOrderedTransactions(int size) {
		Transaction[] result = new Transaction[size];
		for (int i = 0; i < size; i++)
			result[i] = new Transaction(i, i + 1, (int) (Math.random() * 11));
		return result;
	}

	public static Transaction[] generateRandomTransactions(int size) {
		Transaction[] result = generateOrderedTransactions(size);

		for (int i = result.length - 1; i > 0; i--) {
			int shuffleIdx = (int) (Math.random() * size);
			Transaction temp = result[i];
			result[i] = result[shuffleIdx];
			result[shuffleIdx] = temp;
		}

		return result;
	}

	public static String log(String string) {
		System.out.print(string);
		return string;
	}
}
