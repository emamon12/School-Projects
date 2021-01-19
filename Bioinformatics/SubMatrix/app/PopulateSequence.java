package app;

import java.io.BufferedReader;
import java.io.FileReader;

class PopulateSequence {

    protected String[] populate(String[] sequences, int numSequences) {
        String[] sequence = new String[numSequences];
        String fileLine = "";
        String uInput;

        for (int i = 0; i < numSequences; i++) {
            sequence[i] = "";
            uInput = sequences[i];
            System.out.println();
            try (BufferedReader fileIn = new BufferedReader(new FileReader("./inputs/" + uInput))) {
                fileLine = fileIn.readLine();
                while (fileLine != null) {
                    System.out.println(fileLine);
                    fileLine = fileIn.readLine();
                    if (fileLine != null) {
                        sequence[i] = sequence[i] + fileLine;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sequence;
    }
}