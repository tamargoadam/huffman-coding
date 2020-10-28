import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class HuffmanEncoder implements HuffmanCoding {
	
	private String heapType = "binary"; // binary, 4way, or pairing
	private Hashtable<Character, String> encodeTable = new Hashtable<Character, String>(128);
	
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
//			  System.out.println("Using four-way heap...");
			  break;
		  case "pairing":
			  HHeap = new PairingHeap();
//			  System.out.println("Using pairing heap...");
			  break;
		  default:
			  HHeap = new BinaryHeap(128);
//			  System.out.println("Using binary heap...");
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
	public String encodeFile(File inputFile, HuffmanTree huffTree){
        
		// create table of chars and corresponding code
		Hashtable<Character, String> encodeTable = huffEncodeTable(huffTree);
		
		BitOutputStream s = new BitOutputStream("./encoded.bin");
		StringBuilder encode = new StringBuilder();
		int c;
		String code;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			while((c=br.read())!=-1)
			{
				code = encodeTable.get((char)c);
				encode.append(code);
				s.writeBits(code.length(), Integer.parseInt(code, 2)); // TODO: add end of file indication
			}
			br.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        return encode.toString();
	}	
		
	// take a string and huffman tree and output the decoded words
	public String decodeFile(String code, HuffmanTree huffTree){
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
		
	// print the characters and their codes
	public String traverseHuffmanTree(HuffmanTree huffTree){
		
		String tablePrint = "";
		// create table of chars and corresponding code
		Hashtable<Character, String> codeTable = huffEncodeTable(huffTree);
		
		// create string from table
		for(int i=0; i<127; i++){
			if(codeTable.get((char)i) != null)
				tablePrint = tablePrint + ((char)i) + " " + codeTable.get((char)i) + "\n";
		}
		return tablePrint;
	}
	
}

