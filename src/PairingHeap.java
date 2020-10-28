import java.util.Vector;

// min pairing heap for creating Huffman tree
public class PairingHeap implements MinHeap {
	
	private PairingNode root;
	private int heapSize = 0;
	private Vector<PairingNode> meldingQueue = new Vector<PairingNode>();
	
	// heap nodes
	private class PairingNode {		
		public PairingNode child, left, right;
		public HuffmanTree data;
	
		// constructor
		public PairingNode(HuffmanTree data){
			child = left = right = null;
			this.data = data;
		}
		
		public int weight(){
			return data.weight();
		}
	}
	
	// constructor
	public PairingHeap(){}
	
	public int heapSize() {
		return heapSize;
	}
	
	// combine two pairing heaps into one
	public PairingNode meld(PairingNode node1, PairingNode node2){
		if(node1.weight() < node2.weight()){
			node2.left = node1;
			node2.right = node1.child;
			node1.child = node2;
			return node1;
		}
		else{
			node1.left = node2;
			node1.right = node2.child;
			node2.child = node1;
			return node2;
		}
	}
	
	public void insert(HuffmanTree node){
		PairingNode newNode = new PairingNode(node);
		if(heapSize == 0 || root == null){
			root = newNode;
		}
		else {
			root = meld(root, newNode);
		}
		heapSize++;
	}
	
	public HuffmanTree removeMin(){
		if(heapSize == 0) return null;
		
		HuffmanTree min = root.data;		
		if(heapSize == 1){
			root = null;
			heapSize--;
			return min;
		}
		
		// add nodes to melding queue
		PairingNode currentNode = root.child;
		currentNode.left = null;
		meldingQueue.add(currentNode);
		while(currentNode.right != null){
			currentNode = currentNode.right;
			// break link with last node
			currentNode.left.right = null;
			currentNode.left = null;
			meldingQueue.add(currentNode);
		}
		
		// meld first in pairs of pairingNodes and add back to queue until only one node
		PairingNode newMeld;
		while(meldingQueue.size() > 1){
			newMeld = meld(meldingQueue.remove(meldingQueue.size()-1), meldingQueue.remove(meldingQueue.size()-1));
			meldingQueue.add(newMeld);
		}
		root = meldingQueue.remove(0);
		
		heapSize--;
		
		return min;
	}
}