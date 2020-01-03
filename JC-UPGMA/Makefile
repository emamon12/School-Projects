 .sJFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		@$(JC) $(JFLAGS) app/*.java

CLASSES = \
		app/*.java
		
default: classes

classes: $(CLASSES:.java=.class)

run:	
		@java app.App

clean:
		@$(RM) app/*.class