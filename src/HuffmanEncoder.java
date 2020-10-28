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
	
	//Search through Huffman tree
	private Hashtable<Character, String> huffEncodeTable(HuffmanTree huff){
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
	public HuffmanTree buildTree(File inputFile){
		
		Hashtable<Character, Integer> charHash = fileToHash(inputFile);
	    
		//Create max heap
//		BinaryHeap HHeap = new BinaryHeap(128);
		PairingHeap HHeap = new PairingHeap();
//		FourWayHeap HHeap = new FourWayHeap(128);
		for(int i=0; i<127; i++){
		    if(charHash.get((char)i) != null){
		    	HHeap.insert(new HuffmanTree((char)i, charHash.get((char)i)));
		    }
		}
	    
		//Create Huffman tree from heap
		HuffmanTree tmp1 = null;
		HuffmanTree tmp2 = null; 
		HuffmanTree tmp3 = null;

		while (HHeap.heapSize() > 1) { // While two items left
			tmp1 = HHeap.removeMin();
	    	tmp2 = HHeap.removeMin();
	    	tmp3 = new HuffmanTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
			HHeap.insert(tmp3);   // Return new tree to heap
		}
		return tmp3; 
	    
	}	
		
	//Take a file and a HuffTree and encode the file.
	//Output a string of 1's and 0's representing the file
	public String encodeFile(File inputFile, HuffmanTree huffTree){
        
		//Create table of chars and corresponding code
		Hashtable<Character, String> encodeTable = huffEncodeTable(huffTree);
		
		StringBuilder encode = new StringBuilder();
		StringBuilder fileString = new StringBuilder();
		String line = "";
		
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
		
	//print the characters and their codes
	public String traverseHuffmanTree(HuffmanTree huffTree){
		
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

