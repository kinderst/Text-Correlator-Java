package shake_n_bacon;

import providedCode.Hasher;

/**
 * @author Scott Kinder
 * @UWNetID kinders
 * @studentID 1235149
 * @email kinders@uw.edu
 * 
 * 		This is a custom hash value creator for strings. It should be noted that it
 * 		expects to take in a wide range of values, which is why it grows at a "fairly average"
 * 		rate, so it doesn't get wickedly big or small.
 */
public class StringHasher implements Hasher {

	/**
	 * Param str is the string which is to be hashed
	 * This is used to hash a value to a given string.
	 */
	public int hash(String str) {
		if (str == null) {
			return 0;
		}
		int hash = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			hash = Math.abs(hash * 7 + str.charAt(i));
		}
		return hash;
	}
}
