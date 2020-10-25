.sJFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		@$(JC) $(JFLAGS) app/*.java

CLASSES = \
		app/FindSubMatrix.java\
		app/GlobalAlignment.java\
		app/MatrixMultiplication.java\
		app/MatrixPowering.java\
		app/PopulateSequence.java\
		app/Translate.java\
		app/App.java
		
default: classes

classes: $(CLASSES:.java=.class)

run:	
		@java app.App

clean:
		@$(RM) app/*.class