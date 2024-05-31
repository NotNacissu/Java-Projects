/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

        /**
         * Perform KMP substring search on the given text with the given pattern.
         * 
         * This should return the starting index of the first substring match if it
         * exists, or -1 if it doesn't.
         */
        public static int search(String pattern, String text) {
        char[] P = pattern.toCharArray(); // pattern to char array
        char[] T = text.toCharArray(); // text to char array
        
        int[] M = computeMatchTable(pattern); // match table for pattern
        int k = 0; // index for text
        int i = 0; // index for pattern
    
        while (k + i < text.length()) { // iterate through text
            if (P[i] == T[k + i]) { // if pattern matches text
                i++; // increment to next char
                if (i == pattern.length()) { // pattern length matched
                    return k; // return start index
                }
            } 
            // mismatch first char of pattern
            else if (M[i] == -1) {
                k += i + 1; 
                i = 0;
            } 
            // mismatch non-first char pattern
            else {
                k += i - M[i];
                i = M[i];
            }
        } 
        return -1; // no match found 
    }
    
    // Match table for pattern
    private static int[] computeMatchTable(String s) {
        char[] S = s.toCharArray(); // string to char array
        int[] M = new int[s.length()]; // array for match table
        M[0] = -1;
        M[1] = 0; // initial table entries
        
        int j = 0; // position in prefix
        int pos = 2; // position in table
    
        while (pos < s.length()) { // iterate through pattern
            if (S[pos - 1] == S[j]) { // if substrings ...pos-1 and 0..j match
                M[pos] = j + 1; // update table
                pos++;
                j++;
            } else if (j > 0) { // mismatch, restart the prefix
                j = M[j];
            } else { // j = 0, we have run out of candidate prefixes
                M[pos] = 0;
                pos++;
            }
        }
        return M; 
    }

}
