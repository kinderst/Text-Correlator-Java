package shake_n_bacon;

import providedCode.*;

/**
 * @author Scott Kinder
 * @UWNetID kinders
 * @studentID 1235149
 * @email kinders@uw.edu
 * 
 *        This is a Hash table that uses the idea of separate chaining. It is
 *        meant to be used with text files, to make hash values for words, and
 *        store them in a bucket. Each hash entry would contain both the name of the word,
 *        and how many times the word was used in the file.
 */
public class HashTable_SC extends DataCounter {
	private MyLinkedList[] hashArray;
	private int arraySize;
	private Hasher hasher;
	private Comparator<String> comparitor;
	private int uniques;
	private static final int[] PRIMES = {7, 17, 37, 67, 137, 281, 571, 1151, 2309, 4621, 9257, 18517, 37039,
		74093, 148193, 296437};

	/*
	 * Param c is a comparator used to compare strings
	 * Param h is a hash value creator to make hash values
	 * Constructor for a separate chaining hash table. Initializes needed
	 * fields.
	 */
	public HashTable_SC(Comparator<String> c, Hasher h) {
		arraySize = 0;
		hashArray = new MyLinkedList[PRIMES[arraySize]];
		uniques = 0;
		hasher = h;
		comparitor = c;
		for (int i = 0; i < PRIMES[arraySize]; i++) {
			hashArray[i] = new MyLinkedList();
		}
	}
   
	/*
	 * Param data is the word being passed in
	 * This is used to either make a new bucket entry with the given word,
	 * or if the hash value is already in the hash table, it will add it to the bucket.
	 * Each entry contains the name of the word, and how many times in appears.
	 */
	public void incCount(String data) {
		MyLinkedList whichList = hashArray[hasher.hash(data) % PRIMES[arraySize]];
		if (whichList.find(data) != null) {
			whichList.find(data).count++;
		} else {
	    	Node newNode = new Node(data, 1);
	        whichList.insert(newNode);
	        if (++uniques > PRIMES[arraySize]) {
	        	rehash();
	        }
		}
	}

	/*
	 * This is used to get the total number of unique words in the hash table
	 */
	public int getSize() {
		return uniques;
	}

	/*
	 * Param data is the word being checked
	 * This is used to get the count of how many times the word was used.
	 */
	public int getCount(String data) {
		MyLinkedList whichList = hashArray[hasher.hash(data) % PRIMES[arraySize]];
		if (whichList.find(data) != null) {
			return whichList.find(data).count;
		} else {
			return 0;
		}
	}
	
	/*
	 * This is used to rehash words to their new respective index, making a new hash
	 * table with the old entries, and approximately double the size.
	 */
	public void rehash() {
		MyLinkedList[] oldLists = hashArray;
		hashArray = new MyLinkedList[PRIMES[++arraySize]];
		for (int i = 0; i < PRIMES[arraySize]; i++) {
			hashArray[i] = new MyLinkedList();
		}
		for (int i = 0; i < PRIMES[arraySize - 1]; i++) {
			Node tempNode = oldLists[i].head;
			while (tempNode != null) {
				MyLinkedList whichList = hashArray[hasher.hash(tempNode.data) % PRIMES[arraySize]];
				Node newNode = new Node(tempNode.data, tempNode.count);
				whichList.insert(newNode);
				tempNode = tempNode.next;
			}
		}
	}
   
	/*
	 * Creates a simple iterator which can be used to get all of the data
	 * from the hash table.
	 */
	public SimpleIterator getIterator() {
   
		SimpleIterator itr = new SimpleIterator() {
         
			private int currentIndex = 0;
			private int currentUniques = 0;
	        private Node current = hashArray[currentIndex].head;
	         
	        /*
			 * Checks to see if there is another element in hash table.
			 */
	        public boolean hasNext() {
	        	return currentUniques < uniques;
	        }
	         
	        /*
			 * Gets the next element in the hash table.
			 */
	        public DataCount next() {
	        	if (!hasNext()) {
	        		throw new IllegalArgumentException();
	        	}
	        	while (current == null) {
	        		current = hashArray[currentIndex++].head;
	        	}
	        	currentUniques++;
	        	DataCount out = new Node(current.data, current.count);
	        	current = current.next;
	        	return out; 	
	        }
		};
		return itr;
	}
   
	/*
	 * This is a node that is used to link items in the linked list,
	 * using the structure of a DataCount object
	 */
	public class Node extends DataCount {
		public Node next;
   	
		/*
		 * Param str is the word being passed in
		 * Param count is the number of times word was in text file
		 * Constructor call to make a node and initializes needed fields
		 */
		public Node(String str, int count) {
			super(str, count);
		}
	}
   
	/*
	 * This resembles a link list that takes in Nodes, and is able to
	 * remember what the last node was.
	 */
	public class MyLinkedList {
		private Node head;
		private Node last;
      
		/*
		 * Constructor call for the linked list
		 */
		public MyLinkedList() {
			head = null;
		}
      
		/*
		 * inserts a node into the linked list
		 */
		public void insert(Node node) {
			if (head == null) {
				head = node;
				last = head;
			} else {
				last.next = node;
				last = last.next;
			}
		}
     
		/*
		 * finds the word stored in a node in the linked list.
		 */
		public Node find(String data) {
			Node temp = head;
			while (temp != null) {
	            if (comparitor.compare(data, temp.data) == 0) {
	            	return temp;
	            } else {
	            	temp = temp.next;
	            }
			}
			return null;
		}
	}
}