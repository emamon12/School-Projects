package app;

public class App {
    public static void main(String[] args) throws Exception {
        int numSpecies = 0;
        String fileName = "alignment.fasta";

        FastaReader fr = new FastaReader();

        String[] trimmedAlign = fr.readFasta(fileName);

        for (String x : trimmedAlign) {
            if (x.charAt(0) == '>') {
                numSpecies++;
            }
        }

        String[] alignTitles = new String[numSpecies];
        String[] speciesSeq = new String[numSpecies];
        int currentSpecies = 0;

        for (int x = 0; x < trimmedAlign.length; x++) {
            if (trimmedAlign[x].charAt(0) == '>') {
                alignTitles[currentSpecies] = trimmedAlign[x];
                currentSpecies++;
                speciesSeq[currentSpecies - 1] = "";
            } else {
                speciesSeq[currentSpecies - 1] += trimmedAlign[x];
            }
        }

        WriteSequenceToFile wf = new WriteSequenceToFile();

        wf.write(alignTitles, speciesSeq, numSpecies);

        FindDistValues fdv = new FindDistValues();

        double[][] distValues = fdv.findValue(numSpecies, speciesSeq);

        System.out.println("Distance Values: ");
        for(int x = 0; x < numSpecies; x++){
            for(int y = 0; y < numSpecies; y++){
                System.out.printf("%8.3f", distValues[x][y]);
            }
            System.out.println();
        }

        System.out.println();

        FindKValue fkv = new FindKValue();
        double[][] kValues = fkv.findValue(distValues);

        System.out.println("K Values: ");
        for(int x = 0; x < numSpecies; x++){
            for(int y = 0; y < numSpecies; y++){
                System.out.printf("%8.3f", kValues[x][y]);
            }
            System.out.println();
        }

        WriteOutPutToFile wo = new WriteOutPutToFile();
        wo.writeO(alignTitles, numSpecies, kValues);
        
    }
}