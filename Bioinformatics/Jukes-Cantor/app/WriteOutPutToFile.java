package app;

import java.io.BufferedWriter;
import java.io.FileWriter;

class WriteOutPutToFile {

    protected void writeO(String[] titles, int numSpecies, double[][] KValues) {

        String fileName = "./outputs/KValues.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int x = 0; x < numSpecies; x++) {
                for(int y = x; y < numSpecies; y++){
                    if(x != y){
                        writer.write("K(" + titles[x].substring(1,titles[x].length()) + "," + titles[y].substring(1,titles[y].length()) + "): " + KValues[x][y]);
                        writer.newLine();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}