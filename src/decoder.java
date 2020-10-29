import java.io.File;

public class decoder {

	public static void main(String[] args) {

		if(args.length != 2){
			System.err.println("this program requires two argument: encoded file & code table file");
			System.err.println("ex: java decoder ./encoded.bin ./code_table.txt");
			System.exit(1);
		}
		
		File encodedFile = new File(args[0]);
		File codeTableFile = new File(args[1]);
		if(!encodedFile.exists() || !codeTableFile.exists()){
			System.err.println("the file names you provided could not be found");
			System.err.println("please verify the file path is correct and try again");
			System.exit(1);
		}
		
		System.out.println("running huffman decoding.\n");
		HuffmanDecoder decoder = new HuffmanDecoder();
		
		System.out.println("generating decoded file (decoded.txt)...");
		decoder.decodeBinFile(encodedFile, codeTableFile);
		
		System.out.println("\nhuffman decoding complete.");
	}

}
