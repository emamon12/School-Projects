package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
//introduced in version 2.0
class WriteUPGMAToFile {

    protected void write(String outPut) {

        String fileName = "./outputs/UPGMAOutput.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(outPut);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}