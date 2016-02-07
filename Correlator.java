package shake_n_bacon;

import java.io.IOException;

import providedCode.*;

/**
 * @author Scott Kinder
 * @UWNetID kinders
 * @studentID 1235149
 * @email kinders@uw.edu
 * 
 *        This is a correlator to test the correlation between two different
 *        text files. It is intended to show how similar (or different) the two
 *        text files are, by computing a variance. This variance is like the
 *        Euclidean distance between two vectors, as it squares the resulting
 *        difference, and adds that to the variance. It should be noted that
 *        it doesn't take into account all the words, just relevant words that
 *        are shared between the two text files.
 *        
 */
public class Correlator {

	public static void main(String[] args) {
		if (args.length != 3) {
			return;
		}
		double variance = 0.0;
		String firstArg = args[0].toLowerCase();
		DataCounter counter1 = null;
		DataCounter counter2 = null;
		if (firstArg.equals("-s")) {
			counter1 = new HashTable_SC(new StringComparator(), new StringHasher());
			counter2 = new HashTable_SC(new StringComparator(), new StringHasher());
		} else if (firstArg.equals("-o")) {
			counter1 = new HashTable_OA(new StringComparator(), new StringHasher());
			counter2 = new HashTable_OA(new StringComparator(), new StringHasher());
		} else {
			return;
		}
		countWords(args[1], counter1);
		countWords(args[2], counter2);
		variance = calculateFreq(counter1, counter2);

		System.out.println(variance);
	}
	
	/* Param counter1 is the first data counter storing the data of words, and their count.
	 * Param counter2 is the second data counter storing the data of words, and their count.
	 * This is used to calculate individual word frequencies of the two counters.
	 * It then compares the frequencies of each word that is in both counters, and
	 * finds the difference, and then gets the Euclidean sum of it. The final result
	 * of all the differences of frequencies is returned.
	* 
	*/
	private static double calculateFreq(DataCounter counter1, DataCounter counter2) {
		double var = 0.0;
		SimpleIterator itr = counter1.getIterator();
		while (itr.hasNext()) {
			DataCount next = itr.next();
			if (checkWorthy(counter1.getCount(next.data), counter2.getCount(next.data), 
					counter1.getSize(), counter2.getSize())) {
				double freq1 = (counter1.getCount(next.data) * 10.0) / (counter1.getSize() * 10.0);
				double freq2 = (counter2.getCount(next.data) * 10.0) / (counter2.getSize() * 10.0);
				double squared = (freq1 - freq2) * (freq1 - freq2);
				var += squared;
			}
		}
		return var;
	}
	
	/*
	 * Param first is the first count of a word being checked
	 * Param second is the second count of a word being checked
	 * Param firstSize is the total size of the first text file
	 * Param secondSize is the total size of the second text file
	 * This is used to check if a word should be considered or not. This is so that
	 * words that are rarely used (>0.01% of the time) and words that are used a lot
	 * (<1% of the time) are not checked, which would skew data.
	 */
	private static boolean checkWorthy(double first, double second, double firstSize, 
			double secondSize) {
		return (first / firstSize >= 0.0001 && first / firstSize <= 0.01) &&
				(second / secondSize >= 0.0001 && second / secondSize <= 0.01);
	}
	
	/*
	 * Param file is the name of the file which will be checked
	 * Param counter is the data counter which is used to store data from text file
	 * This is used to get a count of how many times each word is a text file is used.
	 */
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
	
}
