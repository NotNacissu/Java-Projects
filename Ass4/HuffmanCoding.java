/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */

import java.util.*;
record Node(double wght, int codedT, String syb, Node lft, Node rgt) implements Comparable<Node>{ //node record for huffman
		
    public int compareTo(Node n){ //compare weights of nodes
        if (this.wght == n.wght){ 
            if (this.syb != null && n.syb != null){ //compare symbols
                return this.syb.compareTo(n.syb);
            } 
            else {return 0;} //if null -> then equal
        }
        return Double.compare(this.wght, n.wght); //if not equal -> compare weights
    }
}

public class HuffmanCoding {
 
	/**
	 * This would be a good place to compute and store the tree.
	 */

	private Node t = null; //store root of HuffmanTree 	
	private List<String> T = new ArrayList<>(); //stores individual chars of text
	private Set<String> S = new HashSet<>(); //stores ,. chars of text	
	private PriorityQueue<Node> N = new PriorityQueue<>(); //priorityQueue to store nodes
	
	public HuffmanCoding(String text) {
		
		for (int i = 0; i < text.length(); i++){ //iterate through text
			S.add(text.substring(i, i + 1)); //add unique chars -> ,.
			T.add(text.substring(i, i + 1)); //add chars -> T
		}

		for (String character : S){ //iterate through symbolS
			double freq = T.stream().filter((s) -> (s.equals(character))).count(); //count frequency of each char
			N.add(new Node(freq, -1, character, null, null)); //add nodes with respective frequency
		}
 
		while (N.size() != 1){ //build HuffmanTree until one node left in queue
			Node n1 = N.poll(); //lowest frequency node
			Node n2 = N.poll(); //next lowest frequency node
 
			Node leftC = new Node(n1.wght(), 0, n1.syb(), n1.lft(), n1.rgt()); //establish left
			Node rightC = new Node(n2.wght(), 1, n2.syb(), n2.lft(), n2.rgt()); //establish right
 
			double leftRightNode = leftC.wght() + rightC.wght(); //calculate weight of combined nodes
			N.add(new Node(leftRightNode, -1, null, leftC, rightC)); //add to priorityQueue
		}
 
		t = N.poll(); //remaining node -> root of tree
	}
 
	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		String encoded = ""; // Store encoded string
		
		for (int i = 0; i < text.length(); i++) { // Traverse through the text
			String code = encoding("" + text.charAt(i), t, ""); // Encode the character
			encoded += code; //encoded code to the result
		}
		
		return encoded; // Return the encoded string
	}
 
	public String encoding(String l, Node n, String c) {
		
		if (n == null) {return null;} //node is null -> return null
 
		if (n.syb() != null && n.syb().equals(l)){ //node equals char to encode  
			c = c + n.codedT(); //add code of node to current code
			return c; //retrun encoded string
		}
 
		if (n.codedT() == -1){ //if node is internal
			String left = encoding(l, n.lft(), c); //traverse left
			String right = encoding(l, n.rgt(), c); //traverse right
			
			if (left != null) {return left;} //return encoded string -> found in left subtree
			if (right != null) {return right;} //return encoded string -> found in right subtree
		}
		
		String lRecur = encoding(l, n.lft(), c); //traverse left
		String rRecur = encoding(l, n.rgt(), c); //traverse right
		
		if (lRecur != null) {return n.codedT() + lRecur;} //return encoded string -> found in left subtree
		if (rRecur != null) {return n.codedT() + rRecur;} //return encoded string -> found in right subtree

		return null; //return null if not found in subtree
	}
 
	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		String text = ""; // Stores the decoded text
		String decodedText; // Stores decoded char to add to text
		Node p = t; // Pointer to traverse the tree
	
		for (int i = 0; i < encoded.length(); i++) { // Decode the string character by character by traversing the tree
			decodedText = encoded.substring(i, i + 1);
	
			if (decodedText.equals("0")) {p = p.lft();} 
			else if (decodedText.equals("1")) {p = p.rgt();} 
			else {return null;}
	
			if (p.syb() != null) { // If a leaf node is reached
				text += p.syb(); // Add the character to the text
				p = t; // Reset the pointer to the root of the tree
			}
		}
		return text;
	}
	
	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}
}
