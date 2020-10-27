
//Binary min heap for creating Huffman tree
class BinaryHeap implements MinHeap{
	
	private HuffmanTree[] binHeap;
	private int heapSize = 0;
	
	//Constructor
	BinaryHeap(int maxSize){
		binHeap = new HuffmanTree[maxSize + 1];
	}
	
	public int heapsize() {
		return heapSize;
	}
	
	// returns true if the specified child of the parent at index 'index' has a lesser weight
	private boolean isChildLess(int index, int child){
		if(child == 0) return binHeap[index].weight() > binHeap[index*2].weight();
		else return binHeap[index].weight() > binHeap[index*2+1].weight();
	}
	
	// returns the index of the child with the lowest weight
	private int getLeastChild(int parentIndex){
		if(binHeap[parentIndex*2].weight() < binHeap[parentIndex*2+1].weight()) return parentIndex*2;
		else return parentIndex*2+1;
	}
	
	// swaps two nodes in the heap
	private void swap(int index1, int index2){
		HuffmanTree tempNode;
		tempNode = binHeap[index2];
		binHeap[index2] = binHeap[index1];
		binHeap[index1] = tempNode;
	}

	public void insert(HuffmanTree node){
		int index = heapSize + 1;
		binHeap[heapSize + 1] = node;
		
		//Adjust heap
		while(index/2 != 0 && binHeap[index/2].weight() > node.weight()){
			swap(index, index/2);
			index = index/2;
		}
		heapSize++;
	}
	
	public HuffmanTree removeMin(){
		if(heapSize == 0){
			System.err.println("Cannot remove from empty heap");
			return null;
		}
		
		int index = 1;
		HuffmanTree minNode = binHeap[index];
		binHeap[index] = binHeap[heapSize-1+index];
		heapSize--;
		HuffmanTree tempNode;
		int leastChild;
		
		//Adjust heap
		if(heapSize > 1){
			while(index*2+1 <= heapSize && (isChildLess(index, 0) || isChildLess(index, 1))){
				
				leastChild = getLeastChild(index);
				swap(index, leastChild);
				index = leastChild;
			
			}
			if(index*2 <= heapSize && isChildLess(index, 0)){
				swap(index, index*2);
				index = index*2;
			}
		}
		
		return minNode;
	}
}