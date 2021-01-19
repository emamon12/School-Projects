Developed on Visual Studio Code 1.37.1
Java version 8 update 211 (build 1.8.0_211-b12)

For Windows: To compile, type "make" in the JC-UPGMA directory. To run, type "run" in the directory. To delete class files, type "clean".

For Linux/Mac: To compile, type "make" in the JC-UPGMA directory. To run, type "make run" in the directory. To delete class files, type "make clean".

Put input files inside the input folder within the JC-UPGMA directory(only the alignment.fasta is needed for this).

For inputs, all white spaces are removed and each sequences is separated by the '>' character.

All of the outputs are titled dynamically, based on the name given by the '>' identifier.

The titles for the sequences are based on what is given in the input, but is shortened for easier reading.

The output is both given on the console and inside the outputs folder.

Changes:
 -App.java has been updated since the previous project.
 -FindKValue.java and FindDistValue.java has been updated since the previous project to correct errors.
 -FindShortestPairWiseDist.java and WriteUPGMAToFile.java has been added to find the shortest pair wise distance using UPGMA.

For code issues please email me at emamon@siue.edu.


