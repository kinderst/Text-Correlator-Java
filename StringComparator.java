package shake_n_bacon;

import providedCode.*;

/**
 * @author Scott Kinder
 * @UWNetID kinders
 * @studentID 1235149
 * @email kinders@uw.edu
 * 
 *        This is a comparer of strings, which is used to see if two given
 *        strings are the exact same. It does take into consideration the case
 *        of the words, if that is not checked before.
 */
public class StringComparator implements Comparator<String> {

	/**
	 * Param s1 is the first string to be compared
	 * Param s2 is the second string to be compared
	 * This is used to compare the two strings, to see if they are equal. If they
	 * are equal, it will return a value of 0, if they are unequal, it will return
	 * a negative or positive value, depending on which word was greater in ASCII value.
	 */
	public int compare(String s1, String s2) {
      int value = 0;
      int size = 0;
      if (s1 != null) {
    	  size = s1.length();
	      if (s2 == null || s2.length() < s1.length() || s2.length() > s1.length()) {
	    	  return 1;
	      }
	      for (int i = 0; i < size; i++) {
	          value = value + s1.charAt(i) - s2.charAt(i);
	      }
      }
      if (value < 0) {
         return -1;
      } else if (value > 0) {
         return 1;
      } else {
         return 0;
      }
	}
}
