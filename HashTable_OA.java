package shake_n_bacon;

import providedCode.*;

/**
 * @author Scott Kinder
 * @UWNetID kinders
 * @studentID 1235149
 * @email kinders@uw.edu
 * 
 *        This is a Hash table that uses the idea of quadratic probing. It is
 *        meant to be used with text files, to make hash values for words, and
 *        store them in a index, which would contain both the name of the word,
 *        and how many times the word was used in the file.
 */
public class HashTable_OA extends DataCounter {
	private static final int[] PRIMES = {7, 17, 37, 67, 137, 281, 571, 1151, 2309, 4621, 9257, 18517, 37039,
		74093, 148193, 296437};
	private int arraySize;
	private Hasher hasher;
	private Comparator<String> comparitor;
	private HashEntry[] array;
	private int totalSize;

	/*
	 * Param c is a comparator used to compare strings
	 * Param h is a hash value creator to make hash values
	 * Constructor for a quadratic probing hash table. Initializes needed
	 * fields.
	 */
	public HashTable_OA(Comparator<String> c, Hasher h) {
		arraySize = 0;
		totalSize = -1;
		hasher = h;
		comparitor = c;
		array = new HashEntry[PRIMES[arraySize]];
	}

	/*
	 * Param data is the word being passed in
	 * This is used to either make a new hash table entry with the given word,
	 * or if the word is already in the hash table, it will increase the count
	 * of the word.
	 */
	public void incCount(String data) {
		int currentPos = findPos(data);
		if (isActive(currentPos)) {
			array[currentPos].count++;
		} else {
			array[currentPos] = new HashEntry(data, 1);
			if (++totalSize >= PRIMES[arraySize] / 2) {
				rehash();
			}
		}
	}

	/*
	 * This is used to get the total number of unique words in the hash table
	 */
	public int getSize() {
		return totalSize;
	}

	/*
	 * Param data is the word being checked
	 * This is used to get the count of how many times the word was used.
	 */
	public int getCount(String data) {
		int currentPos = findPos(data);
		if (isActive(currentPos)) {
			return array[currentPos].count;
		} else {
			return 0;
		}
	}
	
	/*
	 * Creates a simple iterator which can be used to get all of the data
	 * from the hash table.
	 */
	public SimpleIterator getIterator() {
		   
		SimpleIterator itr = new SimpleIterator() {
			private int currentIndex = 0;
			private int totalCount = 0;
			private HashEntry current = array[currentIndex];

			/*
			 * Gets the next element in the hash table.
			 */
			public DataCount next() {
				if (!hasNext()) {
					throw new IllegalArgumentException();
				}
				while (current == null) {
					current = array[++currentIndex];
				}
				totalCount++;
				DataCount out = new HashEntry(current.data, current.count);
				current = array[++currentIndex];
				return out;
			}

			/*
			 * Checks to see if there is another element in hash table.
			 */
			public boolean hasNext() {
				return totalCount < totalSize;
			}

		};
		return itr;
	}
	
	/*
	 * This is a hash table entry which is used to store the data at the given
	 * index.
	 */
	private static class HashEntry extends DataCount {

		/*
		 * Param data is the given word being passed in
		 * Param count is the number of times given word was used
		 * Constructor call for a hash table entry, initializes needed
		 * fields
		 */
		public HashEntry(String data, int count) {
			super(data, count);
		}
	}
	
	/*
	 * Param currentPos is the position in the hash table to check
	 * This is used to see if the given index has a entry that is valid
	 * for being examined.
	 */
	private boolean isActive(int currentPos) {
		return array[currentPos] != null;
	}
	
	/*
	 * Param data is the given word being checked
	 * This is used to find where in the table the given word is.
	 */
	public int findPos(String data) {
		int offset = 1;
		int currentPos = hasher.hash(data) % PRIMES[arraySize];
		while (array[currentPos] != null && comparitor.compare(array[currentPos].data, data) != 0) {
			currentPos += offset;
			offset += 2;
			if (currentPos >= PRIMES[arraySize]) {
				currentPos -= PRIMES[arraySize];
			}
		}
		return currentPos;
	}
	
	/*
	 * This is used to rehash words to their new respective index, making a new hash
	 * table with the old entries, and approximately double the size.
	 */
	private void rehash() {
		HashEntry[] oldLists = array;
		array = new HashEntry[PRIMES[++arraySize]];
		for (int i = 0; i < PRIMES[arraySize - 1]; i++) {
			if (oldLists[i] != null) {
				int currentPos = findPos(oldLists[i].data);
				array[currentPos] = new HashEntry(oldLists[i].data, oldLists[i].count);
			}
		}
	}

}