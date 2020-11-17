import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class HuffmanEncoder {

	private String heapType = "binary"; // binary, 4way, or pairing
	private Hashtable<Character, String> encodeTable = new Hashtable<Character, String>();

	// constructor
	// defaults heap type to binary
	HuffmanEncoder(){}
	HuffmanEncoder(String heap){
		heapType = heap;
	}

	// search through huffman tree
	private Hashtable<Character, String> huffEncodeTable(HuffmanTree huff){
		
		for(int i=0; i<127; i++){
			if(characterCode(huff.root(), (char)i, "") != ""){
				encodeTable.put((char)i, characterCode(huff.root(), (char)i, ""));
			}
		}
		
		return encodeTable;
		
	}


	// finds each char's code
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

	// create hashtable with ASCII key and frequency value
	private Hashtable<Character, Integer> fileToHash(File inputFile){
		
		Hashtable<Character, Integer> charHash = new Hashtable<Character, Integer>(128, 1);

		int c;
		int newVal;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			while((c=br.read())!=-1)
			{
				if(!charHash.containsKey((char)c)){
					charHash.put((char)c, 1); 
				}else{
					newVal = charHash.get((char)c) + 1;
					charHash.replace((char)c, newVal);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return charHash;
		
	}


	// take a file as input and create a table with characters and frequencies
	// print the characters and frequencies
	public String getFrequencies(File inputFile){

		Hashtable<Character, Integer> charHash = fileToHash(inputFile);

		// display characters and frequencies
		String display = "";
		for(int i=0; i<127; i++){
			if(charHash.get((char)i) != null)
				display = display + Character.toString((char) i ) + " " 
						+ Integer.toString(charHash.get((char)i)) + "\n";
		}
		return display;
		
	}


	// take a file as input and create a Huffman Tree
	public HuffmanTree buildTree(File inputFile){

		Hashtable<Character, Integer> charHash = fileToHash(inputFile);

		// create min heap
		MinHeap HHeap;
		switch(heapType) {
		case "4way":
			HHeap = new FourWayHeap(128);
			break;
		case "pairing":
			HHeap = new PairingHeap();
			break;
		default:
			HHeap = new BinaryHeap(128);
		}

		for(int i=0; i<127; i++){
			if(charHash.get((char)i) != null){
				HHeap.insert(new HuffmanTree((char)i, charHash.get((char)i)));
			}
		}

		// create huffman tree from heap
		HuffmanTree tmp1 = null;
		HuffmanTree tmp2 = null; 
		HuffmanTree tmp3 = null;

		while (HHeap.heapSize() > 1) { // while two items left
			tmp1 = HHeap.removeMin();
			tmp2 = HHeap.removeMin();
			tmp3 = new HuffmanTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
			HHeap.insert(tmp3);   // return new tree to heap
		}
		return tmp3; 

	}	

	
	// take a file and a HuffTree and encode the file.
	// output a string of 1's and 0's representing the file
	public void generateEncodedFile(File inputFile, HuffmanTree huffTree){

		// create table of chars and corresponding code
		Hashtable<Character, String> encodeTable = huffEncodeTable(huffTree);

		int c;
		String code;

		BufferedReader br;
		BitOutputStream s;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			s = new BitOutputStream("./encoded.bin");
			while((c=br.read())!=-1)
			{
				code = encodeTable.get((char)c);
				s.writeBits(code.length(), Integer.parseInt(code, 2));
			}
			br.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	
	

	// generates a file with characters and their codes
	public void generateCodeTable(HuffmanTree huffTree){

		// create table of chars and corresponding code
		Hashtable<Character, String> codeTable = huffEncodeTable(huffTree);

		try {
			FileWriter w = new FileWriter("./code_table.txt");
			for(int i=0; i<127; i++){
				if(codeTable.get((char)i) != null)
					w.append(((char)i) + " " + codeTable.get((char)i) + "\n");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

