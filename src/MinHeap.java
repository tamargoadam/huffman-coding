
public interface MinHeap {
	
	// get size of heap
	public int heapSize();
	
	// insert a Huffman tree into the heap
	public void insert(HuffmanTree node);
	
	// remove the Huffman tree with the smallest root node weight
	public HuffmanTree removeMin();
	
}
