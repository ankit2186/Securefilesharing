JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
          Server.java \
          Peer.java \
          connectToPeer.java \
	
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class


