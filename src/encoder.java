import java.io.File;

public class encoder {

	public static void main(String[] args) {

		if(args.length != 1){
			System.err.println("this program requires one argument: input path");
			System.err.println("ex: java encoder ./my_input_file.txt");
			System.exit(1);
		}
		
		File textFile = new File(args[0]);
		if(!textFile.exists()){
			System.err.println("the file name you provided could not be found");
			System.err.println("please verify the file path is correct and try again");
			System.exit(1);
		}
		
		System.out.println("running huffman encoding.\n");
		HuffmanEncoder encoder = new HuffmanEncoder();
		
		System.out.println("finding character frequencies...");
		encoder.getFrequencies(textFile);
		
		System.out.println("building huffman tree...");
		HuffmanTree huffTree = encoder.buildTree(textFile);
		
		System.out.println("generating code table file (code_table.txt)...");
		encoder.generateCodeTable(huffTree);
		
		System.out.println("generating encoded file (encoded.bin)...");
		encoder.generateEncodedFile(textFile, huffTree);
		
		System.out.println("\nhuffman encoding complete.");

	}

}
