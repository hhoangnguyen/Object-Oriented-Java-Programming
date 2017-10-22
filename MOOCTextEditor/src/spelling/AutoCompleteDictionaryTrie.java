package spelling;

import java.util.*;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Huy Hoang-Nguyen
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}


	public TrieNode getRoot() {
		return this.root;
	}
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word)
	{
		TrieNode next = null;
		TrieNode curr = this.root;
		char[] wordArray = word.toLowerCase().toCharArray();

		// loop through each char of word, advancing to new node
	    for (char chr : wordArray) {
			next = curr.insert(chr);

			// if next is null, chr already existed in children of curr
			if (next == null) {
				// get next TrieNode using chr as key
				curr = curr.getChild(chr);
			}
			else { // otherwise, we just created a new TrieNode
				curr = next;
			}
		}

		// flag to return whether or not we successfully adding a new word
		// if a word already in the dictionary, return false
		boolean isNewWord = false;

		// if this is an end word
		// don't increase size as this already in the dictionary
		if (!curr.endsWord()) {
			this.size = this.size + 1;
			isNewWord = true;
		}

		curr.setEndsWord(true);

	    return isNewWord;
	}

	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
		return this.size;
	}
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s) 
	{
		// edge case: empty string is not a word
		if (s.length() == 0) return false;

		// iterate through the trie starting from root
		TrieNode curr = this.findStemFromString(s);

		// can't find the stem, not a word
		if (curr == null) return false;

		return curr.endsWord();
	}

	/**
	 * Find TrieNode for stem, if doesn't exist, return null
	 * @param s
	 * @return TrieNode|null
	 */
	private TrieNode findStemFromString(String s) {
		TrieNode curr = this.root;

		if (s.equals("")) return curr;

		char[] wordArray = s.toLowerCase().toCharArray();
		for (char chr : wordArray) {
			if (curr != null) {
				if (curr.getChild(chr) == null) {
					if (curr.getText().equals(s)) {
						return curr;
					}
				}
				curr = curr.getChild(chr);
			}
		}

		return curr;
	}

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions

		 // list of completions to return (initially empty)
		 ArrayList<String> completions = new ArrayList<>();

		 if (prefix.equals("") && numCompletions == 0) {
			return completions;
		 }

		 // find stem from string
		 TrieNode stemNode = this.findStemFromString(prefix);
		 if (stemNode == null) {
			 return completions;
		 }

		 // BFS, level traversal

		 // create a queue (LinkedList)
		 LinkedList<TrieNode> queue = new LinkedList<>();
		 queue.addLast(stemNode);

		 // while the queue is not empty and you don't have enough completions
		 while (!queue.isEmpty() && completions.size() < numCompletions) {
			 // remove the first Node from the queue
			 TrieNode node = queue.remove();

			 // if it is a word and not already in list, add it to the completions list
			 if (node.endsWord() && !completions.contains(node.getText())) {
				 completions.add(node.getText());
			 }

			 // add all of its child nodes to the back of the queue
			 for (Character c : node.getValidNextCharacters()) {
				 TrieNode next = node.getChild(c);
				 queue.addLast(next);
			 }
		 }

		 return completions;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;

 		if (curr.endsWord()) {
			System.out.println(curr.getText());
		}
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}