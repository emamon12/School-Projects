package app;

import java.io.*;

public class App {
    public static void main(String[] args) {
        String executionType;
        String sequence1 = "";
        String sequence2 = "";
        int matchScore = 0;
        int gapScore = 0;
        int mismatchScore = 0;
        int gapExtensionScore = 0;
        boolean showGraph = false;

        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            do {
                System.out.print("Affine(A), Global(G), or Local(L)? ");
                executionType = userInput.readLine();
                executionType = executionType.toUpperCase();

                if (!(executionType.equals("A") || executionType.equals("G") || executionType.equals("L"))) {
                    System.err.println("Invalid Input");
                } else {
                    break;
                }
            } while (true);

            System.out.println();

            do {
                System.out.print("Input First Sequence File Name(type quit to exit): ");
                String userIn = userInput.readLine();

                if (userIn.equals("quit")) {
                    return;
                }

                System.out.println();
                File firstFile = new File("./inputs/" + userIn);
                System.out.print("File Path: ");
                System.out.println(firstFile.getAbsolutePath());

                try (BufferedReader fileIn = new BufferedReader(new FileReader(firstFile))) {
                    System.out.println("File Found");
                    String fileInput = fileIn.readLine();

                    if (fileInput == null) {
                        System.err.println("File Empty... now exiting");
                    }

                    System.out.println("File Preference: " + fileInput);

                    fileInput = fileIn.readLine();

                    while (fileInput != null) {
                        sequence1 = sequence1 + fileInput;
                        fileInput = fileIn.readLine();
                    }
                    fileIn.close();
                    break;

                } catch (Exception e) {
                    System.err.println("File Not Found. Please Try Again.");
                }

            } while (true);

            do {
                System.out.println();
                System.out.print("Input Second Sequence File Name(type quit to exit): ");
                String userIn = userInput.readLine();

                if (userIn.equals("quit")) {
                    return;
                }

                System.out.println();
                File secondFile = new File("./inputs/" + userIn);
                System.out.println("File Path: " + secondFile.getAbsolutePath());

                try (BufferedReader fileIn = new BufferedReader(new FileReader(secondFile))) {
                    System.out.println("File Found");
                    String fileInput = fileIn.readLine();

                    if (fileInput == null) {
                        System.err.println("File Empty... now exiting");
                    }

                    System.out.println("File Preference: " + fileInput);

                    fileInput = fileIn.readLine();
                    while (fileInput != null) {
                        sequence2 = sequence2 + fileInput;
                        fileInput = fileIn.readLine();
                    }
                    fileIn.close();
                    break;

                } catch (Exception e) {
                    System.err.println("File Not Found. Please Try Again.");
                }
            } while (true);

            do {
                System.out.println();
                System.out.print("Provide a Match Score: ");
                matchScore = Integer.parseInt(userInput.readLine());

                System.out.println();

                System.out.print("Provide a Mismatch Score: ");
                mismatchScore = Integer.parseInt(userInput.readLine());

                System.out.println();

                if (executionType.equals("A")) {
                    System.out.print("Provide a Gap Start Score: ");
                    gapScore = Integer.parseInt(userInput.readLine());

                    System.out.println();

                    System.out.print("Provide a Gap Extension Score: ");
                    gapExtensionScore = Integer.parseInt(userInput.readLine());
                    System.out.println();
                } else {
                    System.out.print("Provide a Gap Score: ");
                    gapScore = Integer.parseInt(userInput.readLine());
                    System.out.println();
                }

                do {
                    System.out.print("Show graph and path?(y/n) ");
                    String userIn = userInput.readLine();
                    if (userIn.toUpperCase().equals("Y")) {
                        showGraph = true;
                        break;
                    } else if (userIn.toUpperCase().equals("N")) {
                        break;
                    }
                    System.out.println("Unrecognized Command");
                } while (true);
                break;
            } while (true);

            userInput.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println();

        System.out.println("First Sequence: " + sequence1);
        System.out.println("Second Sequence: " + sequence2);

        String seq1 = sequence1;
        String seq2 = sequence2;
        seq1 = seq1.replaceAll("\\s+", "");
        seq2 = seq2.replaceAll("\\s+", "");
        int match = matchScore;
        int mismatch = mismatchScore;
        int gap = gapScore;
        int gapE = gapExtensionScore;

        System.err.println();

        if (executionType.equals("A")) {
            AffineAlignment aff = new AffineAlignment();
            aff.getAffineAlignment(seq1, seq2, match, mismatch, gap, gapE,showGraph);
        } else if (executionType.equals("G")) {
            GlobalAlignment gla = new GlobalAlignment();
            gla.getGlobalAlignment(seq1, seq2, match, mismatch, gap, showGraph);
        } else if (executionType.equals("L")){
            LocalAlignment lal = new LocalAlignment();
            lal.getLocalAlignment(seq1, seq2, match, mismatch, gap, showGraph);
        }

    }
}