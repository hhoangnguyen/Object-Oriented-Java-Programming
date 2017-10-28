/**
 *
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team
 */
public class NearbyWords implements SpellingSuggest {
    // THRESHOLD to determine how many words to look through when looking
    // for spelling suggestions (stops prohibitively long searching)
    // For use in the Optional Optimization in Part 2.
    private static final int THRESHOLD = 100000;

    Dictionary dict;

    public NearbyWords(Dictionary dict) {
        this.dict = dict;
    }

    /**
     * Return the list of Strings that are one modification away
     * from the input string.
     *
     * @param s         The original String
     * @param wordsOnly controls whether to return only words or any String
     * @return list of Strings which are nearby the original string
     */
    public List<String> distanceOne(String s, boolean wordsOnly) {
        List<String> retList = new ArrayList<String>();
        insertions(s, retList, wordsOnly);
        substitution(s, retList, wordsOnly);
        deletions(s, retList, wordsOnly);
        return retList;
    }


    /**
     * Add to the currentList Strings that are one character mutation away
     * from the input string.
     *
     * @param s           The original String
     * @param currentList is the list of words to append modified words
     * @param wordsOnly   controls whether to return only words or any String
     * @return
     */
    public void substitution(String s, List<String> currentList, boolean wordsOnly) {
        // for each letter in the s and for all possible replacement characters
        for (int index = 0; index < s.length(); index++) {
            for (int charCode = (int) 'a'; charCode <= (int) 'z'; charCode++) {
                // use StringBuffer for an easy interface to permuting the
                // letters in the String
                StringBuffer sb = new StringBuffer(s);
                sb.setCharAt(index, (char) charCode);

                this.addStringToList(s, currentList, sb.toString(), wordsOnly);
            }
        }
    }

    /**
     * if the item isn't in the list, isn't the original string, and
     * (if wordsOnly is true) is a real word, add to the list
     *
     * @param originalString The original string
     * @param list           list of words
     * @param string         string to compare
     * @param wordsOnly      controls whether to return only words or any String
     */
    private void addStringToList(String originalString, List<String> list, String string, boolean wordsOnly) {
        if (!list.contains(string) &&
                (!wordsOnly || dict.isWord(string)) &&
                !originalString.equals(string)) {
            list.add(string);
        }
    }

    /**
     * Add to the currentList Strings that are one character insertion away
     * from the input string.
     *
     * @param s           The original String
     * @param currentList is the list of words to append modified words
     * @param wordsOnly   controls whether to return only words or any String
     * @return
     */
    public void insertions(String s, List<String> currentList, boolean wordsOnly) {
        // for each letter in the s and for all possible characters
        for (int index = 0; index < s.length(); index++) {
            for (int charCode = (int) 'a'; charCode <= (int) 'z'; charCode++) {
                // use StringBuffer for an easy interface to permuting the
                // letters in the String
                StringBuffer sb = new StringBuffer(s);

                // index 0, insert to start
                if (index == 0) {
                    sb.insert(0, (char) charCode);
                    this.addStringToList(s, currentList, sb.toString(), wordsOnly);

                    // delete char at index 0 as we just inserted a char above
                    sb.deleteCharAt(0);
                }

                sb.insert(index + 1, (char) charCode);
                this.addStringToList(s, currentList, sb.toString(), wordsOnly);
            }
        }
    }

    /**
     * Add to the currentList Strings that are one character deletion away
     * from the input string.
     *
     * @param s           The original String
     * @param currentList is the list of words to append modified words
     * @param wordsOnly   controls whether to return only words or any String
     * @return
     */
    public void deletions(String s, List<String> currentList, boolean wordsOnly) {
        // for each letter in the s and for all possible characters
        for (int index = 0; index < s.length(); index++) {
            // use StringBuffer for an easy interface to permuting the
            // letters in the String
            StringBuffer sb = new StringBuffer(s);

            sb.deleteCharAt(index);
            this.addStringToList(s, currentList, sb.toString(), wordsOnly);
        }
    }

    /**
     * Add to the currentList Strings that are one character deletion away
     * from the input string.
     *
     * @param word           The misspelled word
     * @param numSuggestions is the maximum number of suggestions to return
     * @return the list of spelling suggestions
     */
    @Override
    public List<String> suggestions(String word, int numSuggestions) {
        // Input:  word for which to provide number of spelling suggestions
        // Input:  number of maximum suggestions to provide
        // Output: list of spelling suggestions

        // initial variables

        // Create a queue to hold words to explore
        List<String> queue = new LinkedList<String>();

        // Create a visited set to avoid looking at the same String repeatedly
        HashSet<String> visited = new HashSet<String>();

        // Create list of real words to return when finished
        List<String> retList = new LinkedList<String>();

        // Number of string searched so we can limit using THRESHOLD
        int numStringSearch = 0;

        // Add the initial word to the queue and visited
        queue.add(word);
        visited.add(word);

        // while the queue has elements and we need more suggestions
        while (!queue.isEmpty() && numStringSearch <= THRESHOLD) {
            // remove the word from the start of the queue and assign to curr
            String curr = queue.remove(0);
            // get a list of neighbors (strings one mutation away from curr)
            List<String> neighbors = this.distanceOne(curr, false);
            // for each n in the list of neighbors
            for (String n : neighbors) {
                // if n is not visited
                if (!visited.contains(n)) {
                    // add n to the visited set
                    visited.add(n);
                    // add n to the back of the queue
                    queue.add(n);
                    // if n is a word in the dictionary
                    if (this.dict.isWord(n) && retList.size() < numSuggestions) {
                        // add n to the list of words to return
                        retList.add(n);
                    }
                }
            }
            numStringSearch += neighbors.size();
        }

        return retList;

    }

    public static void main(String[] args) {
        // basic testing code to get started
        String word = "fare";
        // Pass NearbyWords any Dictionary implementation you prefer
        Dictionary d = new DictionaryHashSet();
        DictionaryLoader.loadDictionary(d, "test_cases/dict2.txt");
        NearbyWords w = new NearbyWords(d);
        List<String> l = w.distanceOne(word, false);
        System.out.println("One away word Strings for for \"" + word + "\" are:");
        System.out.println(l + "\n");

        word = "fare";
        List<String> suggest = w.suggestions(word, 3);
        System.out.println("Spelling Suggestions for \"" + word + "\" are:");
        System.out.println(suggest);
    }
}
