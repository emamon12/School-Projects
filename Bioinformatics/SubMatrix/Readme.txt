Developed on Visual Studio Code 1.37.1
Java version 8 update 211 (build 1.8.0_211-b12)

For Windows: To compile, type "make" in the SubMatrix directory. To run, type "run" in the directory. To delete class files, type "clean".

For Linux/Mac: To compile, type "make" in the SubMatrix directory. To run, type "make run" in the directory. To delete class files, type "make clean".

Put input files inside the input folder within SubMatrix directory.
The frequences used to calculate the substitution matrix is hard coded, so there is no need for the frequency text file.

For inputs, all white spaces are removed and all lines, after the the first line, are concatenated.

NOTE: The "STOP" amino acid placeholder is called "STOP". So if a Stop codon is read, it will mess up the Global Alignment, to remedy this,
just modify the placeholder at the beginning of the Translate Class file. 

For convenience, all of the major algorithms are separated into class functions. The algorithm should be much easier to find and read.

For code issues please email me at emamon@siue.edu.


