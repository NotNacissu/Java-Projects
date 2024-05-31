import java.util.*;



// I got help with this part using this website : https://www.geeksforgeeks.org/boyer-moore-algorithm-for-pattern-searching/
public class BoyerMoore{
    private static final int NO_OF_CHARS = 256;


    public static int search(String pattern, String text) {
        int m = pattern.length();
        int n = text.length();

        int[] badChar = new int[NO_OF_CHARS];

        // Preprocess the bad character heuristic
        badCharHeuristic(pattern, m, badChar);

        int shift = 0;

        while (shift <= (n - m)) {
            int j = m - 1;

            // Keep reducing index j of pattern while characters of pattern and text are matching at this shift s
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j))
                j--;

            // If the pattern is present at current shift, return the shift
            if (j < 0) {
                return shift;
            }

            // Shift the pattern according to the bad character heuristic
            shift += Math.max(1, j - badChar[text.charAt(shift + j)]);
        }

        // Pattern not found
        return -1;
    }
    
    private static void badCharHeuristic(String str, int size, int[] badChar) {
        Arrays.fill(badChar, -1);

        for (int i = 0; i < size; i++) {
            badChar[str.charAt(i)] = i;
        }
    }


}
