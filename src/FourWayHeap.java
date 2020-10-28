
//4-way cache optimized min heap for creating Huffman tree
class FourWayHeap implements MinHeap{
	
	private HuffmanTree[] fourWayHeap;
	private int heapSize = 0;
	
	// constructor
	FourWayHeap(int maxSize){
		fourWayHeap = new HuffmanTree[maxSize + 1];
	}
	
	public int heapSize() {
		return heapSize;
	}
	
	// returns the specified child index for a parent and index 'index'
	private int getChild(int index, int child){
		switch(child) {
		  case 0:
		    return 4 * (index-3) + 4;
		  case 1:
			  return 4 * (index-3) + 5;
		  case 2:
			  return 4 * (index-3) + 6;
		  default:
			  return 4 * (index-3) + 7;
		}
	}
	
	// returns the index of the current node's parent
	private int getParent(int currentIndex){
		return (int) (Math.ceil((currentIndex-3)/4.0) + 2);
	}
	
	// returns true any child of the parent at index 'index' has a lesser weight
	private boolean isChildLess(int index){
		int numChild = Math.min(heapSize+3-getChild(index, 0)+1, 4);
		for(int i=0; i<numChild; i++){
			if (fourWayHeap[index].weight() > fourWayHeap[getChild(index, i)].weight())
				return true;
		}
		return false;
	}
	
	// returns the index of the child with the lowest weight
	private int getLeastChild(int index){
		int minIndex = getChild(index, 0);
		int numChild = Math.min(heapSize+3-getChild(index, 0)+1, 4);
		for(int i=1; i<numChild; i++){
			if (fourWayHeap[getChild(index, i)].weight() < fourWayHeap[minIndex].weight())
				minIndex = getChild(index, i);
		}
		return minIndex;
	}
	
	// swaps two nodes in the heap
	private void swap(int index1, int index2){
		HuffmanTree tempNode;
		tempNode = fourWayHeap[index2];
		fourWayHeap[index2] = fourWayHeap[index1];
		fourWayHeap[index1] = tempNode;
	}

	public void insert(HuffmanTree node){
		int index = heapSize + 3;
		fourWayHeap[index] = node;
		
		// adjust heap
		while(fourWayHeap[getParent(index)] != null && fourWayHeap[getParent(index)].weight() > node.weight()){
			swap(index, getParent(index));
			index = getParent(index);
		}
		heapSize++;
	}
	
	public HuffmanTree removeMin(){
		if(heapSize == 0){
			System.err.println("Cannot remove from empty heap");
			return null;
		}
		
		int index = 3;
		HuffmanTree minNode = fourWayHeap[index];
		fourWayHeap[index] = fourWayHeap[heapSize-1+index];
		heapSize--;
		int leastChild;
		
		// adjust heap
		if(heapSize > 1){
			while(getChild(index, 0) <= heapSize+3 && isChildLess(index)){

				leastChild = getLeastChild(index);
				swap(index, leastChild);
				index = leastChild;
			
			}
		}
		
		return minNode;
	}
}