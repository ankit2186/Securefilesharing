JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES =\
	Peer1.java \
	keyBank.java \
        filePeer2Peer.java \
        Peer2Peer.java \
        Serialize.java \
	
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

