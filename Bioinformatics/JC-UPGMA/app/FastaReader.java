package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class FastaReader {

    protected String[] readFasta(String fileName) {
        int indexCounter = 0;
        int contentCounter = 0;
        int currIndex = 0;

        String[] alignArray = new String[100];
        File sequenceFile = new File("./inputs/" + fileName);
        try (BufferedReader fileInput = new BufferedReader(new FileReader(sequenceFile))) {
            String currLine = fileInput.readLine();
            alignArray[indexCounter] = currLine;

            if (!currLine.isEmpty() && currLine != null) {
                contentCounter++;
            }

            indexCounter++;
            while (currLine != null) {
                currLine = fileInput.readLine();
                if (currLine == null) {
                    break;
                }
                if (!currLine.isEmpty() && currLine != null) {
                    contentCounter++;
                }
                alignArray[indexCounter] = currLine;
                indexCounter++;
            }

        } catch (Exception e) {
            System.out.println("Invalid File");
            e.printStackTrace();
        }

        String[] alignTrimmed = new String[contentCounter];

        for (int x = 0; x < indexCounter - 1; x++) {
            if (!alignArray[x].isEmpty() && alignArray[x] != null) {
                alignTrimmed[currIndex] = alignArray[x];
                currIndex++;
            }
        }
        return alignTrimmed;
    }
}