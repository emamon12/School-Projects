package app;

import java.io.BufferedWriter;
import java.io.FileWriter;

class WriteSequenceToFile {

    protected void write(String[] titles, String[] content, int numSpecies) {

        for (int x = 0; x < numSpecies; x++) {
            String fileName = "./inputs/" + titles[x].substring(1,titles[x].length()) + ".txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(content[x]);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}