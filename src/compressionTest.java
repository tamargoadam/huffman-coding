import java.io.File;

public class compressionTest {

	public static void main(String[] args) {

		HuffmanEncoder huff = new HuffmanEncoder();
		File textFile = new File("./myTest.txt");
		
		System.out.println("Character Frequencies:\n-----------------------");
		System.out.println(huff.getFrequencies(textFile));
		
		System.out.println("Character Codes:\n-----------------------");
		HuffmanTree huffTree = huff.buildTree(textFile);
		System.out.println(huff.traverseHuffmanTree(huffTree));
		
		System.out.println("Encoded File:\n-----------------------");
		String encoded = huff.encodeFile(textFile, huffTree);
		System.out.println(encoded + "\n");
		System.out.println("length:" + encoded.length() + "\n");
		
		System.out.println("Decoded File:\n-----------------------");
		System.out.println(huff.decodeFile(encoded, huffTree) + "\n");
		
	}

}
