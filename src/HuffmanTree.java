//Classes for building huffman tree

//General node
interface HuffBaseNode {
	boolean isLeaf(); 
	int weight();
}


//Huffman tree leaf node
class HuffLeafNode implements HuffBaseNode {
	
	private char element;      // Element for this node
	private int weight;        // Weight for this node

	//Constructor
	HuffLeafNode(char el, int wt){
		element = el; weight = wt;
	}

	char value(){
		return element;
	}

	public int weight() {
		return weight;
	}

	public boolean isLeaf() {
		return true;
	}
}


//Huffman tree internal node
class HuffInternalNode implements HuffBaseNode {
	private int weight;            
	private HuffBaseNode left;  	
	private HuffBaseNode right; 

	//Constructor
	HuffInternalNode(HuffBaseNode l, HuffBaseNode r, int wt){
		left = l; right = r; weight = wt; 
	}

	HuffBaseNode left(){
		return left;
	}

	HuffBaseNode right(){
		return right; 
	}

	public int weight(){
		return weight; 
	}

	public boolean isLeaf(){
		return false; 
	}
	
}


//Tree Structure
class HuffmanTree{
	
	private HuffBaseNode root;  
	
	//Constructors
	HuffmanTree(char el, int wt){
		root = new HuffLeafNode(el, wt);
	}
	HuffmanTree(HuffBaseNode l, HuffBaseNode r, int wt){
		root = new HuffInternalNode(l, r, wt);
	}

	HuffBaseNode root(){
		return root;
	}
	
	int weight(){
		return root.weight();
	}
	
	int compareTo(Object t){
		HuffmanTree that = (HuffmanTree)t;
		if (root.weight() < that.weight()) return -1;
	    else if (root.weight() == that.weight()) return 0;
	    else return 1;
	}
}