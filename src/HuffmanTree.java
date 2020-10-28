
// classes for building huffman tree

// general node
interface HuffBaseNode {
	boolean isLeaf(); 
	int weight();
}


// huffman tree leaf node
class HuffLeafNode implements HuffBaseNode {
	
	private char element;      // Element for this node
	private int weight;        // Weight for this node

	// constructor
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


// huffman tree internal node
class HuffInternalNode implements HuffBaseNode {
	private int weight;            
	private HuffBaseNode left;  	
	private HuffBaseNode right; 

	// constructor
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


// tree structure
class HuffmanTree{
	
	private HuffBaseNode root;  
	
	// constructors
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
	
}