package shake_n_bacon;

import java.io.IOException;

import providedCode.*;

/**
 * An executable that counts the words in a files and prints out the counts in
 * descending order. You will need to modify this file.
 */
public class RunTimeTest {

	/*
	 * Param counter is the given data counter to be used
	 * This is used to get all of the words from the counter, and how many 
	 * times each word was used (count), storing them in an orderly manner. 
	 */
	private static DataCount[] getCountsArray(DataCounter counter) {
		if (counter.getSize() <= 0) {
			return new DataCount[0];
		}
		DataCount[] countArray = new DataCount[counter.getSize()];
		SimpleIterator itr = counter.getIterator();
		int i = 0;
		while (itr.hasNext()) {
			DataCount next = itr.next();
			countArray[i] = new DataCount(next.data, next.count);
			i++;
		}
		return countArray;
	}

	// ////////////////////////////////////////////////////////////////////////
	// /////////////// DO NOT MODIFY ALL THE METHODS BELOW ///////////////////
	// ////////////////////////////////////////////////////////////////////////

	private static void countWords(String file, DataCounter counter) {
		try {
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
			}
		} catch (IOException e) {
			System.err.println("Error processing " + file + " " + e);
			System.exit(1);
		}
	}

	// IMPORTANT: Used for grading. Do not modify the printing *format*!
	private static void printDataCount(DataCount[] counts) {
		for (DataCount c : counts) {
			System.out.println(c.count + "\t" + c.data);
		}
	}

	/*
	 * Sort the count array in descending order of count. If two elements have
	 * the same count, they should be ordered according to the comparator. This
	 * code uses insertion sort. The code is generic, but in this project we use
	 * it with DataCount and DataCountStringComparator.
	 * 
	 * @param counts array to be sorted.
	 * 
	 * @param comparator for comparing elements.
	 */
	private static <E> void insertionSort(E[] array, Comparator<E> comparator) {
		for (int i = 1; i < array.length; i++) {
			E x = array[i];
			int j;
			for (j = i - 1; j >= 0; j--) {
				if (comparator.compare(x, array[j]) >= 0) {
					break;
				}
				array[j + 1] = array[j];
			}
			array[j + 1] = x;
		}
	}

	/*
	 * Print error message and exit
	 */
	private static void usage() {
		System.err
				.println("Usage: [-s | -o] <filename of document to analyze>");
		System.err.println("-s for hashtable with separate chaining");
		System.err.println("-o for hashtable with open addressing");
		System.exit(1);
	}

	/**
	 * Entry of the program
	 * 
	 * @param args
	 *            the input arguments of this program
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
		}

		String firstArg = args[0].toLowerCase();
		DataCounter counter = null;
		if (firstArg.equals("-s")) {
			counter = new HashTable_SC(new StringComparator(),
					new StringHasher());
		} else if (firstArg.equals("-o")) {
			counter = new HashTable_OA(new StringComparator(),
					new StringHasher());
		} else {
			usage();
		}
		
		for (int timing = 0; timing < 10; ++timing) {
			long startTime = System.nanoTime();
			countWords(args[1], counter);
			DataCount[] counts = getCountsArray(counter);
			//insertionSort(counts, new DataCountStringComparator());
			//printDataCount(counts);
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime;
			System.out.println(elapsedTime + " nanoseconds or " 
			         + elapsedTime/(1000000000.0) + " seconds elapsed of insert time");
		}
	}
}
