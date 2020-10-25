package app;

import java.io.*;
import java.util.Hashtable;

public class App {

    private final static String FILE_ERR_MESSAGE = "Unable to find file, please try again";
    private final static String FILE_ACPT = "File Found!";
    private final static String FIRST_PROMPT = "Please input the file name of the first sequence: ";
    private final static String SECOND_PROMPT = "Please input the file name of the second sequence: ";
    private final static String MATRIX_PROMPT = "Enter the file name of the substitution scores matrix or probability PAM matrix: ";
    private final static String MUT_PROMPT = "Is the file a mutation probability PAM matrix?(Y/N): ";
    private final static String GAP_PROMPT = "Enter the gap score: ";
    private final static String INVALID_TYPE = "Invalid Input, please try again...";
    private final static String DIV_PROMPT = "Enter the divergence: ";
    private final static String FIRST_SEQ = "Is the first sequence a nucleotide?(Y/N): ";
    private final static String SECOND_SEQ = "Is the first sequence a nucleotide?(Y/N): ";
    private final static String FSEQ_TEMP = "Is the first sequence a template?(Template must be 5`-3`)(Y/N): ";
    private final static String SSEQ_TEMP = "Is the second sequence a template?(Template must be 5`-3`)(Y/N): ";

    private final static double[] FREQ_ARRAY = { 0.08768333990119, 0.0405129182960021, 0.0408784631518651,
            0.0477160345974603, 0.0324709539656211, 0.0378461268859548, 0.0504933695605074, 0.0898249006830963,
            0.0328588505954496, 0.0357514442352249, 0.0852464099207531, 0.0791031344407513, 0.0148824394639692,
            0.0148824394639692, 0.0515802694709073, 0.0697549720598532, 0.0583275704247605, 0.00931264523877659,
            0.0317154088087089, 0.0630397292098708 };

    public static void main(String[] args) throws Exception {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println();

            boolean seq1IsNucleotide = false;
            boolean seq2IsNucleotide = false;
            boolean seq1IsTemplate = false;
            boolean seq2IsTemplate = false;
            boolean isMutProb = false;

            double[][] permMatrix;
            String[] seqOfAA;
            String[] sPlaceHolder;
            String uInput;
            String[] seqArray;
            String[] aminoArray;
            String matrixFileName;

            int[][] subMatrix = new int[20][20];

            String[] sequences = new String[2];

            int gapScore = 0;
            int divergence = 250;
            /*------------------Start Of User Prompt-------------------- */
            do {
                System.out.print(FIRST_PROMPT);
                uInput = userInput.readLine();
                File fileCheck = new File("./inputs/" + uInput);
                try (BufferedReader fileIn = new BufferedReader(new FileReader(fileCheck))) {
                    sequences[0] = uInput;
                    System.out.println(FILE_ACPT);
                    break;
                } catch (Exception e) {
                    System.out.println(FILE_ERR_MESSAGE);
                }
            } while (true);

            System.out.print(FIRST_SEQ);
            uInput = userInput.readLine();

            if (uInput.toUpperCase().equals("Y")) {
                seq1IsNucleotide = true;
            }

            System.out.print(FSEQ_TEMP);
            uInput = userInput.readLine();

            if (uInput.toUpperCase().equals("Y")) {
                seq2IsTemplate = true;
            }

            do {
                System.out.print(SECOND_PROMPT);
                uInput = userInput.readLine();
                File fileCheck = new File("./inputs/" + uInput);
                try (BufferedReader fileIn = new BufferedReader(new FileReader(fileCheck))) {
                    sequences[1] = uInput;
                    System.out.println(FILE_ACPT);
                    break;
                } catch (Exception e) {
                    System.out.println(FILE_ERR_MESSAGE);
                }
            } while (true);

            System.out.print(SECOND_SEQ);
            uInput = userInput.readLine();

            if (uInput.toUpperCase().equals("Y")) {
                seq2IsNucleotide = true;
            }

            System.out.print(SSEQ_TEMP);
            uInput = userInput.readLine();

            if (uInput.toUpperCase().equals("Y")) {
                seq2IsTemplate = true;
            }

            do {
                System.out.print(MATRIX_PROMPT);
                uInput = userInput.readLine();
                File fileCheck = new File("./inputs/" + uInput);
                try (BufferedReader fileIn = new BufferedReader(new FileReader(fileCheck))) {
                    matrixFileName = uInput;
                    System.out.println(FILE_ACPT);
                    break;
                } catch (Exception e) {
                    System.out.println(FILE_ERR_MESSAGE);
                }
            } while (true);

            System.out.print(MUT_PROMPT);
            uInput = userInput.readLine();

            if (uInput.toUpperCase().equals("Y")) {
                isMutProb = true;
            }

            do {
                System.out.print(GAP_PROMPT);
                uInput = userInput.readLine();
                try {
                    gapScore = Integer.parseInt(uInput);
                    break;
                } catch (Exception e) {
                    System.out.println(INVALID_TYPE);
                }
            } while (true);

            /*-----------------END OF USER PROMPT--------------*/


            //populate the two sequence arrays
            PopulateSequence ps = new PopulateSequence();
            seqArray = ps.populate(sequences, 2);
            aminoArray = seqArray;

            System.out.println();
            //translates the sequences arrays if nucleotides
            if (seq1IsNucleotide) {
                Translate t = new Translate();
                aminoArray[0] = t.translate(seqArray[0], seq1IsTemplate);
            } else {
                aminoArray[0] = seqArray[0];
            }

            if (seq2IsNucleotide) {
                Translate t = new Translate();
                aminoArray[1] = t.translate(seqArray[1], seq2IsTemplate);
            } else {
                aminoArray[1] = seqArray[1];
            }

            //take when using a mutation probability
            if (isMutProb) {
                do {
                    System.out.print(DIV_PROMPT);
                    uInput = userInput.readLine();
                    try {
                        divergence = Integer.parseInt(uInput);
                        break;
                    } catch (Exception e) {
                        System.out.println(INVALID_TYPE);
                    }
                } while (true);
            }
            /*----------------START OF FILE READING-----------------*/
            File firstFile = new File("./inputs/" + matrixFileName);
            try (BufferedReader fileIn = new BufferedReader(new FileReader(firstFile))) {
                String currLine = "";
                System.out.println("File Found");
                currLine = fileIn.readLine();
                System.out.println("FATSA info: " + currLine);
                currLine = fileIn.readLine();

                seqOfAA = currLine.split(",");

                double[][] pamMatrix = new double[seqOfAA.length][seqOfAA.length];
                int[][] pamMatrixInt = new int[seqOfAA.length][seqOfAA.length];

                Hashtable<String, Integer> hash = new Hashtable<String, Integer>();

                for (int i = 0; i < 20; i++) {
                    hash.put(seqOfAA[i], i);
                }

                if (isMutProb) {
                    //start reading of the given matrix
                    int rowCounter = 0;
                    while (currLine != null) {
                        currLine = fileIn.readLine();
                        if (currLine != null) {
                            sPlaceHolder = currLine.split(",");
                            for (int x = 0; x < sPlaceHolder.length; x++) {
                                pamMatrix[rowCounter][x] = (Double.parseDouble(sPlaceHolder[x]));
                            }
                            rowCounter++;
                        }
                    }
                    System.out.println();

                    double[] colSum = new double[seqOfAA.length];

                    for (int x = 0; x < seqOfAA.length; x++) {
                        for (int y = 0; y < seqOfAA.length; y++) {
                            colSum[x] += pamMatrix[y][x];
                        }
                    }
                    //dividing each column element by the column sum to increase accuracy
                    for (int x = 0; x < seqOfAA.length; x++) {
                        for (int y = 0; y < seqOfAA.length; y++) {
                            pamMatrix[y][x] = (pamMatrix[y][x] / colSum[x]);
                        }
                    }

                    MatrixPowering mm = new MatrixPowering();
                    permMatrix = mm.power(pamMatrix, 5000);

                    double[] aminoFreq = new double[20];
                    System.out.println("Amino acid frequences taken at 5000 divergence");

                    for (int i = 0; i < 20; i++) {
                        aminoFreq[i] = permMatrix[i][i];
                        System.out.println(seqOfAA[i] + ": " + aminoFreq[i]);
                    }
                    System.out.println();

                    double[][] probMatrix = mm.power(pamMatrix, divergence);

                    FindSubMatrix fsm = new FindSubMatrix();
                    subMatrix = fsm.findSubMatrix(probMatrix, FREQ_ARRAY);

                    System.out.println();
                    System.out.println("Probability Matrix");

                    for (String x : seqOfAA) {
                        System.out.printf("%7s", x);
                    }
                    System.out.println();

                    for (int x = 0; x < 20; x++) {
                        for (int y = 0; y < 20; y++) {
                            System.out.printf("%7.3f", probMatrix[x][y]);
                        }
                        System.out.println();
                    }
                //else the usage of given subsitution matrix
                } else {
                    //start reading of the current matrix and then having set the sub matrix to the read input matrix
                    int rowCounter = 0;
                    while (currLine != null) {
                        currLine = fileIn.readLine();
                        if (currLine != null) {
                            sPlaceHolder = currLine.split(",");
                            for (int x = 0; x < sPlaceHolder.length; x++) {
                                pamMatrixInt[rowCounter][x] = (Integer.parseInt(sPlaceHolder[x]));
                            }
                            rowCounter++;
                        }

                        subMatrix = pamMatrixInt;
                    }
                }

                System.out.println();
                System.out.println();
                System.out.println("Substitution Matrix (Standardized Amino Acid Frequencies Are Used)");

                for (String x : seqOfAA) {
                    System.out.printf("%7s", x);
                }
                System.out.println();
                for (int x = 0; x < 20; x++) {
                    for (int y = 0; y < 20; y++) {
                        System.out.printf("%7d", subMatrix[x][y]);
                    }
                    System.out.println();
                }

                System.out.println();

                GlobalAlignment ga = new GlobalAlignment();
                ga.getGlobalAlignment(aminoArray[0], aminoArray[1], gapScore, true, subMatrix, hash);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}