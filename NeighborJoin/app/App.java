package app;

import java.io.*;

public class App {
    public static void main(String[] args) throws Exception {
        int numInputs = 0;

        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            do {
                System.out.print("Input File Name: ");
                String userIn = userInput.readLine();

                if (userIn.equals("quit")) {
                    return;
                }

                System.out.println();

                File file = new File("./inputs/" + userIn);

                System.out.print("File Path: ");
                System.out.println(file.getAbsolutePath());

                try (BufferedReader fileIn = new BufferedReader(new FileReader(file))) {
                    System.out.println("File Found");
                    String fileInput = fileIn.readLine();

                    if (fileInput == null) {
                        System.err.println("File Empty... Now Exiting");
                        return;
                    }

                    numInputs = Integer.parseInt(fileInput);

                    System.out.println("Number of clusters: " + fileInput);

                    fileInput = fileIn.readLine();

                    String[] names = fileInput.split("\\s+");

                    double[][] origMatrix = new double[numInputs][numInputs];

                    for (int i = 0; i < numInputs; i++) {
                        fileInput = fileIn.readLine();

                        String[] rowN = fileInput.split("\\s+");

                        for (int x = 0; x < numInputs; x++) {
                            origMatrix[i][x] = Double.parseDouble(rowN[x]);
                        }
                    }

                    NeighborJoin nj = new NeighborJoin();

                    nj.neighborJoin(origMatrix, numInputs, names);
                    break;
                } catch (Exception e) {
                    System.err.println("Invalid File Name, Please Try Again...(type 'quit' to exit)");
                }
            } while (true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}