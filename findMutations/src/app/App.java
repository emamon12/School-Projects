package app;

import java.util.*;
import java.io.*;

public class App {

    // setting up constructor
    int id;
    String geneticCode;
    String aminoAcid;

    // change these constants for other represenations
    public static final String ALA = "A";
    public static final String ARG = "R";
    public static final String ASN = "N";
    public static final String ASP = "D";
    public static final String CYS = "C";
    public static final String GLN = "Q";
    public static final String GLU = "E";
    public static final String GLY = "G";
    public static final String HIS = "H";
    public static final String ILE = "I";
    public static final String LEU = "L";
    public static final String LYS = "K";
    public static final String MET = "M";
    public static final String PHE = "F";
    public static final String PRO = "P";
    public static final String SER = "S";
    public static final String THR = "T";
    public static final String TRP = "W";
    public static final String TYR = "Y";
    public static final String VAL = "V";
    public static final String ASX = "B";
    public static final String GLX = "Z";
    public static final String Xaa = "X";
    public static final String STOP = "STOP";

    // constructor for amino acid sequences
    public App(int id, String geneticCode, String aminoAcid) {
        this.id = id;
        this.geneticCode = geneticCode;
        this.aminoAcid = aminoAcid;
    }

    public static void main(String[] args) throws Exception {

        int codex1Entries = 0;
        int codex2Entries = 0;
        char[] codex1 = new char[100];
        char[] codex2 = new char[100];

        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            boolean firstFileInvalid = true;
            boolean secondFileInvalid = true;
            boolean firstSequenceIsTemplate = true;
            boolean secondSequenceIsTemplate = true;
            boolean firstSequenceIs5to3 = true;
            boolean secondSequenceIs5to3 = true;

            Integer[] mismatchInstances = new Integer[100];
            String[] mismatchFirstSequence = new String[100];
            String[] mismatchSeconSequence = new String[100];

            do {
                System.out.print("Enter the wildtype sequence file name(type quit to quit the application): ");
                String userIn = userInput.readLine();

                if (userIn.equals("quit")) {
                    return;
                }

                File firstFile = new File("./inputs/" + userIn);
                System.out.println(firstFile.getAbsolutePath());

                try (BufferedReader fileIn = new BufferedReader(new FileReader(firstFile))) {

                    System.out.println("file found");
                    String fileInput = fileIn.readLine();

                    if(fileInput == null){
                        System.err.println("File empty... now exiting");
                        return;
                    }

                    System.out.println("File Preference: " + fileInput);

                    while (fileInput != null) {
                        fileInput = fileIn.readLine();
                        if (fileInput == null) {
                            break;
                        }
                        codex1Entries += fileInput.length();
                        System.out.println(fileInput);
                    }
                    firstFileInvalid = false;

                    fileIn.close();

                    BufferedReader fileIn2 = new BufferedReader(new FileReader(firstFile));
                    codex1 = new char[codex1Entries];
                    int currentIndex = 0;

                    fileInput = fileIn2.readLine();
                    while (fileInput != null) {
                        fileInput = fileIn2.readLine();
                        if (fileInput == null) {
                            break;
                        }
                        fileInput.trim();
                        fileInput.toUpperCase().getChars(0, fileInput.length(), codex1, currentIndex);
                        currentIndex += fileInput.length();
                    }
                    fileIn2.close();
                } catch (Exception e) {
                    System.err.println("First input file not found. Please try again.");
                    e.printStackTrace();
                }
            } while (firstFileInvalid);

            do {
                System.out.print("Enter the allele sequence file name(type quit to quit the application): ");
                String userIn = userInput.readLine();

                if (userIn.equals("quit")) {
                    return;
                }

                File secondFile = new File("./inputs/" + userIn);
                System.out.println(secondFile.getAbsolutePath());

                try (BufferedReader secondFileIn = new BufferedReader(new FileReader(secondFile))) {

                    System.out.println("file found");
                    String fileInput = secondFileIn.readLine();
                    if(fileInput == null){
                        System.err.println("File empty... now exiting");
                    }
                    System.out.println("File Preference: " + fileInput);

                    while (fileInput != null) {
                        fileInput = secondFileIn.readLine();
                        if (fileInput == null) {
                            break;
                        }
                        codex2Entries += fileInput.length();
                        System.out.println(fileInput);
                    }
                    secondFileInvalid = false;

                    secondFileIn.close();

                    BufferedReader secondFileIn2 = new BufferedReader(new FileReader(secondFile));
                    codex2 = new char[codex2Entries];
                    int currentIndex = 0;

                    fileInput = secondFileIn2.readLine();
                    while (fileInput != null) {
                        fileInput = secondFileIn2.readLine();
                        if (fileInput == null) {
                            break;
                        }
                        fileInput.trim();
                        fileInput.toUpperCase().getChars(0, fileInput.length(), codex2, currentIndex);
                        currentIndex += fileInput.length();
                    }
                    secondFileIn2.close();
                } catch (Exception a) {
                    System.err.println("Second input file not found. Please try again.");
                    a.printStackTrace();
                }

                System.out.println();

            } while (secondFileInvalid);
            do {
                System.out.print("First sequence (T)emplate stand or (N)on-Template strand? ");
                String userIn = userInput.readLine();
                if (userIn.toUpperCase().equals("T") || userIn.toUpperCase().equals("N")) {
                    System.out.println();
                    if (userIn.toUpperCase().equals("T")) {
                        firstSequenceIsTemplate = true;
                    } else {
                        firstSequenceIsTemplate = false;
                    }
                    break;
                } else {
                    System.err.println("Unrecognized command");
                }
            } while (true);

            do {
                System.out.print("First sequence from (5)-3 or (3)-5? ");
                String userIn = userInput.readLine();
                if (userIn.toUpperCase().equals("5") || userIn.toUpperCase().equals("3")) {
                    if (userIn.toUpperCase().equals("5")) {
                        firstSequenceIs5to3 = true;
                    } else {
                        firstSequenceIs5to3 = false;
                    }
                    System.out.println();
                    break;
                } else {
                    System.err.println("Unrecognized command");
                }
            } while (true);
            do {
                System.out.print("Second sequence (T)emplate stand or (N)on-Template strand? ");
                String userIn = userInput.readLine();
                if (userIn.toUpperCase().equals("T") || userIn.toUpperCase().equals("N")) {
                    System.out.println();
                    if (userIn.toUpperCase().equals("T")) {
                        secondSequenceIsTemplate = true;
                    } else {
                        secondSequenceIsTemplate = false;
                    }
                    break;
                } else {
                    System.err.println("Unrecognized command");
                }
            } while (true);

            do {
                System.out.print("Second sequence from (5)-3 or (3)-5? ");
                String userIn = userInput.readLine();
                if (userIn.toUpperCase().equals("5") || userIn.toUpperCase().equals("3")) {
                    if (userIn.toUpperCase().equals("5")) {
                        secondSequenceIs5to3 = true;
                    } else {
                        secondSequenceIs5to3 = false;
                    }
                    System.out.println();
                    break;
                } else {
                    System.err.println("Unrecognized command");
                }
            } while (true);

            char[] rnaCodex1 = new char[codex1.length];
            char[] rnaCodex2 = new char[codex2.length];

            if (!firstSequenceIsTemplate && firstSequenceIs5to3) {
                for (int i = 0; i < codex1.length; i++) {
                    if (codex1[i] == 'T') {
                        rnaCodex1[i] = 'U';
                    } else {
                        rnaCodex1[i] = codex1[i];
                    }
                }
            }

            if (!firstSequenceIsTemplate && !firstSequenceIs5to3) {
                for (int i = 0; i < codex1.length; i++) {
                    if (codex1[codex1.length - 1 - i] == 'T') {
                        rnaCodex1[i] = 'U';
                    } else {
                        rnaCodex1[i] = codex1[codex1.length - 1 - i];
                    }
                }
            }

            if (firstSequenceIsTemplate && !firstSequenceIs5to3) {
                for (int i = 0; i < codex1.length; i++) {
                    switch (codex1[i]) {
                    case 'A':
                        rnaCodex1[i] = 'U';
                        break;
                    case 'C':
                        rnaCodex1[i] = 'G';
                        break;
                    case 'T':
                        rnaCodex1[i] = 'A';
                        break;
                    case 'G':
                        rnaCodex1[i] = 'C';
                        break;
                    }
                }
            }

            if (firstSequenceIsTemplate && firstSequenceIs5to3) {
                for (int i = 0; i < codex1.length; i++) {
                    switch (codex1[codex1.length - 1 - i]) {
                    case 'A':
                        rnaCodex1[i] = 'U';
                        break;
                    case 'C':
                        rnaCodex1[i] = 'G';
                        break;
                    case 'T':
                        rnaCodex1[i] = 'A';
                        break;
                    case 'G':
                        rnaCodex1[i] = 'C';
                        break;
                    }
                }
            }
            // second ssequence
            if (!secondSequenceIsTemplate && secondSequenceIs5to3) {
                for (int i = 0; i < codex2.length; i++) {
                    if (codex2[i] == 'T') {
                        rnaCodex2[i] = 'U';
                    } else {
                        rnaCodex2[i] = codex2[i];
                    }
                }
            }

            if (!secondSequenceIsTemplate && !secondSequenceIs5to3) {
                for (int i = 0; i < codex2.length; i++) {
                    if (codex2[codex2.length - 1 - i] == 'T') {
                        rnaCodex2[i] = 'U';
                    } else {
                        rnaCodex2[i] = codex2[codex2.length - 1 - i];
                    }
                }
            }

            if (secondSequenceIsTemplate && !secondSequenceIs5to3) {
                for (int i = 0; i < codex2.length; i++) {
                    switch (codex2[i]) {
                    case 'A':
                        rnaCodex2[i] = 'U';
                        break;
                    case 'C':
                        rnaCodex2[i] = 'G';
                        break;
                    case 'T':
                        rnaCodex2[i] = 'A';
                        break;
                    case 'G':
                        rnaCodex2[i] = 'C';
                        break;
                    }
                }
            }

            if (secondSequenceIsTemplate && secondSequenceIs5to3) {
                for (int i = 0; i < codex2.length; i++) {
                    switch (codex2[codex2.length - 1 - i]) {
                    case 'A':
                        rnaCodex2[i] = 'U';
                        break;
                    case 'C':
                        rnaCodex2[i] = 'G';
                        break;
                    case 'T':
                        rnaCodex2[i] = 'A';
                        break;
                    case 'G':
                        rnaCodex2[i] = 'C';
                        break;
                    }
                }
            }

            System.out.print("RNA sequence 1: ");
            System.out.println(rnaCodex1);
            System.out.println();
            System.out.print("RNA sequence 2: ");
            System.out.println(rnaCodex2);
            System.out.println();

            if (rnaCodex1.length != rnaCodex2.length) {
                System.out
                        .println("The length of the sequences are not equal... therefore evidence of frame shifting... now exiting...");
                return;
            }

            int numEntries = codex1.length / 3;
            List<App>[] aminoAcidArray1 = createAminoAcidArray(numEntries);
            List<App>[] aminoAcidArray2 = createAminoAcidArray(numEntries);
            populateAminoAcid(rnaCodex1, aminoAcidArray1, numEntries);
            populateAminoAcid(rnaCodex2, aminoAcidArray2, numEntries);

            System.out.println();
            System.out.print("Protein Sequence 1: ");
            for (int i = 0; i < aminoAcidArray1.length; i++) {
                System.out.print(aminoAcidArray1[i].get(0).aminoAcid);
            }

            System.out.println();
            System.out.println();
            System.out.print("Protein Sequence 2: ");
            for (int i = 0; i < aminoAcidArray2.length; i++) {
                System.out.print(aminoAcidArray2[i].get(0).aminoAcid);
            }

            String finalUserIn;

            int mismatchCounter = 0;
            boolean validNucleotideCommand = false;

            while (!validNucleotideCommand) {
                System.out.println();
                System.out.print("Compare (N)ucleotide or (A)mino acid sequences? ");

                finalUserIn = userInput.readLine();

                if (finalUserIn.toUpperCase().equals("N")) {
                    validNucleotideCommand = true;
                    for (int i = 0; i < rnaCodex1.length - 1; i++) {
                        if (!(rnaCodex1[i] == rnaCodex2[i])) {
                            mismatchCounter++;
                            if (mismatchCounter == 100) {
                                System.out.println("Too many mutations(mutations greater than 100)... Now exiting.");
                                return;
                            }
                            mismatchInstances[mismatchCounter] = i + 1;
                            mismatchFirstSequence[mismatchCounter] = Character.toString(rnaCodex1[i]);
                            mismatchSeconSequence[mismatchCounter] = Character.toString(rnaCodex2[i]);
                        }
                    }

                    if (mismatchCounter == 0) {
                        System.out.println("the sequences are identical");
                    } else {
                        for (int i = 1; i <= mismatchCounter; i++) {
                            System.out.print("Mismatch Found: ");
                            System.out.print(
                                    "" + mismatchFirstSequence[i] + mismatchInstances[i] + mismatchSeconSequence[i]);
                        }
                    }
                    System.out.println();
                }

                if (finalUserIn.toUpperCase().equals("A")) {
                    validNucleotideCommand = true;
                    for (int i = 0; i < aminoAcidArray1.length - 1; i++) {
                        if (!(aminoAcidArray1[i].get(0).aminoAcid.equals(aminoAcidArray2[i].get(0).aminoAcid))) {
                            mismatchCounter++;
                            if (mismatchCounter == 100) {
                                System.out.println("Too many Mutations(mutations greater than 100)... Now exiting.");
                                return;
                            }
                            mismatchInstances[mismatchCounter] = i + 1;
                            mismatchFirstSequence[mismatchCounter] = aminoAcidArray1[i].get(0).aminoAcid;
                            mismatchSeconSequence[mismatchCounter] = aminoAcidArray2[i].get(0).aminoAcid;
                        }
                    }
                    if (mismatchCounter == 0) {
                        System.out.println("the sequences are identical");
                    } else {
                        for (int i = 1; i <= mismatchCounter; i++) {
                            System.out.print("Mismatch Found: ");
                            System.out.print(
                                    "" + mismatchFirstSequence[i] + mismatchInstances[i] + mismatchSeconSequence[i]);
                        }
                    }
                }

                if (validNucleotideCommand == true) {
                    return;
                }

                System.out.println("Invalid Input Command...");

            }

        }

    }

    public static List<App>[] createAminoAcidArray(int numEntries) {
        List<App>[] aminoAcidArray = new List[numEntries];
        for (int i = 0; i < numEntries; i++) {
            aminoAcidArray[i] = new ArrayList<>();
        }
        return aminoAcidArray;
    }

    public static void addAminoAcid(List<App>[] aminoAcidArray, int currentEntry, int id, String geneticCode,
            String aminoAcid) {
        aminoAcidArray[currentEntry].add(new App(id, geneticCode, aminoAcid));
    }

    public static void populateAminoAcid(char[] rnaCodex, List<App>[] aminoAcidArray, int numEntries) {
        char entry1;
        char entry2;
        char entry3;

        int indexCounter = 0;

        String aminoAcid = "null";
        String nucleotideSeq = "null";

        entry1 = 0;
        entry2 = 0;
        entry3 = 0;

        for (int i = 0; i <= rnaCodex.length - 2; i += 3) {

            entry1 = rnaCodex[i];
            entry2 = rnaCodex[i + 1];
            entry3 = rnaCodex[i + 2];

            nucleotideSeq = "" + entry1 + entry2 + entry3;

            switch (entry1) {
            case 'U':
                switch (entry2) {
                case 'U':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = PHE;
                        break;
                    case 'C':
                        aminoAcid = PHE;
                        break;
                    default:
                        aminoAcid = LEU;
                        break;
                    }
                    break;
                case 'C':
                    aminoAcid = SER;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = TYR;
                        break;
                    case 'C':
                        aminoAcid = TYR;
                        break;
                    default:
                        aminoAcid = STOP;
                        break;
                    }
                    break;
                case 'G':
                    switch (entry3) {
                    case 'A':
                        aminoAcid = STOP;
                        break;
                    case 'G':
                        aminoAcid = TRP;
                        break;
                    default:
                        aminoAcid = CYS;
                        break;
                    }
                    break;
                }
                break;
            case 'C':
                switch (entry2) {
                case 'U':
                    aminoAcid = LEU;
                    break;
                case 'C':
                    aminoAcid = PRO;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = HIS;
                        break;
                    case 'C':
                        aminoAcid = HIS;
                        break;
                    default:
                        aminoAcid = GLN;
                        break;
                    }
                    break;
                case 'G':
                    aminoAcid = ARG;
                    break;
                }
                break;
            case 'A':
                switch (entry2) {
                case 'U':
                    switch (entry3) {
                    case 'G':
                        aminoAcid = MET;
                        break;
                    default:
                        aminoAcid = ILE;
                        break;
                    }
                    break;
                case 'C':
                    aminoAcid = THR;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = ASN;
                        break;
                    case 'C':
                        aminoAcid = ASN;
                        break;
                    default:
                        aminoAcid = LYS;
                        break;
                    }
                    break;
                case 'G':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = SER;
                        break;
                    case 'C':
                        aminoAcid = SER;
                        break;
                    default:
                        aminoAcid = ARG;
                        break;
                    }
                    break;
                }
                break;
            case 'G':
                switch (entry2) {
                case 'U':
                    aminoAcid = VAL;
                    break;
                case 'C':
                    aminoAcid = ALA;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        aminoAcid = ASP;
                        break;
                    case 'C':
                        aminoAcid = ASP;
                        break;
                    default:
                        aminoAcid = GLU;
                        break;
                    }
                    break;
                case 'G':
                    aminoAcid = GLY;
                    break;
                }
                break;
            }

            addAminoAcid(aminoAcidArray, indexCounter, indexCounter, nucleotideSeq, aminoAcid);
            indexCounter++;

        }
    }
}