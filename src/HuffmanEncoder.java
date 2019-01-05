import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class HuffmanEncoder implements HuffmanCoding {
	
	private Hashtable<Character, String> encodeTable = new Hashtable<Character, String>(128);
	
	//Search through huffman tree
	private Hashtable<Character, String> huffEncodeTable(HuffTree huff){
		for(int i=0; i<127; i++){
				if(characterCode(huff.root(), (char)i, "") != ""){
					encodeTable.put((char)i, characterCode(huff.root(), (char)i, ""));
				}
		}
		return encodeTable;
	}
	
	
	//Finds each char's code
	public static String characterCode(HuffBaseNode bNode, char c, String code){
        HuffInternalNode node = (HuffInternalNode) bNode;
		if(node!=null){
			if (node.left().isLeaf() && ((HuffLeafNode) node.left()).value()==c){
            	return code+"0";
            }else if (node.right().isLeaf() && ((HuffLeafNode) node.right()).value()==c){
                return code+"1";
            }else if(!node.left().isLeaf() && characterCode((HuffInternalNode) node.left(), c, code) != ""){
            	return code+ "0"+characterCode((HuffInternalNode) node.left(), c, code);
            }else if(!node.right().isLeaf() && characterCode((HuffInternalNode) node.right(), c, code) != ""){
            	return code+"1"+characterCode((HuffInternalNode) node.right(), c, code);
            }
        }
        return "";
    }
	
	//Create Hashtable with ASCII key and frequency value
	private Hashtable<Character, Integer> fileToHash(File inputFile){
		Hashtable<Character, Integer> charHash = new Hashtable<Character, Integer>(128, 1);
		FileInputStream file;
		Scanner s;
		try {
			file = new FileInputStream(inputFile);
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	    String line;
	    char readChar;
	    int newVal;
		
	    //Calculates frequencies from scanner
	    while(s.hasNext()){
	    	line = s.nextLine();
	    	for(int i=0; i<line.length(); i++){
	    		readChar = line.charAt(i);
	    		//increments value
	    		if(!charHash.containsKey(readChar)){
	    			charHash.put(readChar, 1); 
	    		}else{
	    			newVal = charHash.get(readChar) + 1;
	    			charHash.replace(readChar, newVal);
	    		}
	    	}
	    }
	    s.close();
	    return charHash;
	}
	
	
	//Take a file as input and create a table with characters and frequencies
	//Print the characters and frequencies
	public String getFrequencies(File inputFile){
		
		Hashtable<Character, Integer> charHash = fileToHash(inputFile);
	    
		//Display characters and frequencies
	    String display = "";
	    for(int i=0; i<127; i++){
	    	if(charHash.get((char)i) != null)
	    		display = display + Character.toString((char) i ) + " " 
	    		+ Integer.toString(charHash.get((char)i)) + "\n";
	    }
		return display;
	}
	
	
	//Take a file as input and create a Huffman Tree
	public HuffTree buildTree(File inputFile){
		
		Hashtable<Character, Integer> charHash = fileToHash(inputFile);
	    
		//Create max heap
		HuffHeap HHeap = new HuffHeap(128);
		for(int i=0; i<127; i++){
		    if(charHash.get((char)i) != null){
		    	HHeap.insert(new HuffTree((char)i, charHash.get((char)i)));
		    }
		}
	    
		//Create huff tree from heap
		HuffTree tmp1 = null;
		HuffTree tmp2 = null; 
		HuffTree tmp3 = null;

		while (HHeap.heapsize() > 1) { // While two items left
			tmp1 = HHeap.removeMin();
	    	tmp2 = HHeap.removeMin();
	    	tmp3 = new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
			HHeap.insert(tmp3);   // Return new tree to heap
		}
		return tmp3; 
	    
	}	
		
	//Take a file and a HuffTree and encode the file.
	//Output a string of 1's and 0's representing the file
	public String encodeFile(File inputFile, HuffTree huffTree){
        
		//Create table of chars and corresponding code
		Hashtable<Character, String> encodeTable = huffEncodeTable(huffTree);
		
		StringBuilder encode = new StringBuilder();
		StringBuilder fileString = new StringBuilder();
		String line = "";
		char readChar;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			while((line=br.readLine())!=null)
			{
			    fileString.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    for(int i=0; i<fileString.length(); i++){
    		encode.append(encodeTable.get(fileString.charAt(i)));
    	}
	    
        return encode.toString();
	}	
		
	//take a String and HuffTree and output the decoded words
	public String decodeFile(String code, HuffTree huffTree){
		String decoded = "";
		boolean found = false;
		HuffInternalNode node = (HuffInternalNode) huffTree.root();
		for(int i=0; i<code.length();i++){
			if(found){
				node = (HuffInternalNode) huffTree.root();
				found = false;
			}
			if(code.charAt(i) == '0'){
				if(node.left().isLeaf()){
					decoded = decoded + ((HuffLeafNode) node.left()).value();
					found = true;
				}else{
					node = (HuffInternalNode)node.left();
				}
			}else if(code.charAt(i) == '1'){
				if(node.right().isLeaf()){
					decoded = decoded + ((HuffLeafNode) node.right()).value();
					found = true;
				}else{
					node = (HuffInternalNode)node.right();
				}
			}
		}
		return decoded;
	}	
		
	//print the characters and their codes
	public String traverseHuffmanTree(HuffTree huffTree){
		
		String tablePrint = "";
		//Create table of chars and corresponding code
		Hashtable<Character, String> codeTable = huffEncodeTable(huffTree);
		
		//Create string from table
		for(int i=0; i<127; i++){
			if(codeTable.get((char)i) != null)
				tablePrint = tablePrint + ((char)i) + " " + codeTable.get((char)i) + "\n";
		}
		return tablePrint;
	}

	
}


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
class HuffTree{
	
	private HuffBaseNode root;  
	
	//Constructors
	HuffTree(char el, int wt){
		root = new HuffLeafNode(el, wt);
	}
	HuffTree(HuffBaseNode l, HuffBaseNode r, int wt){
		root = new HuffInternalNode(l, r, wt);
	}

	HuffBaseNode root(){
		return root;
	}
	
	int weight(){
		return root.weight();
	}
	
	int compareTo(Object t){
		HuffTree that = (HuffTree)t;
		if (root.weight() < that.weight()) return -1;
	    else if (root.weight() == that.weight()) return 0;
	    else return 1;
	}
}

//Max heap
class HuffHeap{
	
	HuffTree[] arrayHeap;
	int heapSize = 0;
	
	//Constructor
	HuffHeap(int maxSize){
		arrayHeap = new HuffTree[maxSize + 1];
	}
	
	public int heapsize() {
		return heapSize;
	}

	void insert(HuffTree node){
		arrayHeap[heapSize + 1] = node;
		
		int index = heapSize + 1;
		HuffTree tempNode;
		
		//Adjust heap
		while(index/2 != 0 && arrayHeap[index/2].weight() > node.weight()){
			tempNode = arrayHeap[index/2];
			arrayHeap[index/2] = arrayHeap[index];
			arrayHeap[index] = tempNode;
			index = index/2;
		}
		heapSize++;
	}
	
	HuffTree removeMin(){
		if(heapSize == 0){
			System.err.println("Cannot remove from empty heap");
			return null;
		}
		
		HuffTree minNode = arrayHeap[1];
		arrayHeap[1] = arrayHeap[heapSize];
		heapSize--;
		int index = 1;
		HuffTree tempNode;
		
		//Adjust heap
		if(heapSize > 1){
			while(index*2+1 <= heapSize && 
					(arrayHeap[index].weight() > arrayHeap[index*2].weight() 
					|| arrayHeap[index].weight() > arrayHeap[index*2+1].weight())){
				
				if(arrayHeap[index*2].weight() < arrayHeap[index*2+1].weight()){
					tempNode = arrayHeap[index*2];
					arrayHeap[index*2] = arrayHeap[index];
					arrayHeap[index] = tempNode;
					index = index*2;
				}else{
					tempNode = arrayHeap[index*2+1];
					arrayHeap[index*2+1] = arrayHeap[index];
					arrayHeap[index] = tempNode;
					index = index*2+1;
				}
				
			}
			if(index*2 <= heapSize && arrayHeap[index*2].weight() < arrayHeap[index].weight()){
				tempNode = arrayHeap[index*2];
				arrayHeap[index*2] = arrayHeap[index];
				arrayHeap[index] = tempNode;
				index = index*2;
			}
		}
		
		return minNode;
	}
}

