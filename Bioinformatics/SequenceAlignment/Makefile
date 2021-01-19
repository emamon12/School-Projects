.sJFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		@$(JC) $(JFLAGS) app/*.java

CLASSES = \
		app/AffineAlignment.java\
		app/GlobalAlignment.java\
		app/LocalAlignment.java\
		app/App.java
		
default: classes

classes: $(CLASSES:.java=.class)

run:	
		@java app.App

clean:
		@$(RM) app/*.class