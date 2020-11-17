JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	BinaryHeap.java \
	BitOutputStream.java \
	decoder.java \
	encoder.java \
	FourWayHeap.java \
	HuffmanDecoder.java \
	HuffmanEncoder.java \
	HuffmanTree.java \
	MinHeap.java \
	PairingHeap.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
