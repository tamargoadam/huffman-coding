import java.io.File;

public interface HuffmanCoding{
	
	//take a file as input and create a table with characters and frequencies
	//print the characters and frequencies
	public String getFrequencies(File inputFile);
	
	
	//take a file as input and create a Huffman Tree
	public HuffTree buildTree(File inputFile);
	
	//take a file and a HuffTree and encode the file.
	//output a string of 1's and 0's representing the file
	public String encodeFile(File inputFile, HuffTree huffTree);
	
	
	//take a String and HuffTree and output the decoded words
	public String decodeFile(String code, HuffTree huffTree);
	
	
	//print the characters and their codes
	public String traverseHuffmanTree(HuffTree huffTree);

}