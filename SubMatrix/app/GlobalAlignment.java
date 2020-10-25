package app;

import java.util.Hashtable;

public class GlobalAlignment {
    protected int[][] getGlobalAlignment(String sequence1, String sequence2, int gapScore, boolean graph,
            int[][] subMatrix, Hashtable<String, Integer> hash) {
        String seq1 = sequence1;
        String seq2 = sequence2;

        boolean showGraph = graph;

        seq1 = " " + seq1;
        seq2 = " " + seq2;

        int seq1Length = seq1.length();
        int seq2Length = seq2.length();

        int val1;
        int val2;
        int val3;

        int gap = gapScore;

        int optMatrix[][] = new int[seq1Length][seq2Length];
        for (int i = 0; i < seq1Length; i++) {
            for (int j = 0; j < seq2Length; j++) {
                optMatrix[i][j] = ' ';
            }
        }
        optMatrix[0][0] = 0;

        for (int i = 1; i < seq1Length; i++) {
            optMatrix[i][0] = optMatrix[i - 1][0] + gap;
        }
        for (int j = 1; j < seq2Length; j++) {
            optMatrix[0][j] = optMatrix[0][j - 1] + gap;
        }

        for (int x = 1; x < seq1Length; x++) {
            for (int y = 1; y < seq2Length; y++) {
                val1 = optMatrix[x][y - 1] + gap;
                val2 = optMatrix[x - 1][y] + gap;
                val3 = optMatrix[x - 1][y - 1] + subMatrix[hash.get(Character.toString(seq1.charAt(x)))][hash
                        .get(Character.toString(seq2.charAt(x)))];
                if ((val1 >= val2) && (val1 >= val3)) {
                    optMatrix[x][y] = val1;
                } else if ((val2 >= val1) && (val2 >= val3)) {
                    optMatrix[x][y] = val2;
                } else {
                    optMatrix[x][y] = val3;
                }
            }
        }

        if (showGraph == true) {
            System.out.println("OPT Matrix: ");
            for (int x = 0; x < seq1Length; x++) {
                for (int y = 0; y < seq2Length; y++) {
                    System.out.printf("%4d", optMatrix[x][y]);
                }
                System.out.println();
            }
        }

        int pathRow = seq1Length - 1;
        int pathCol = seq2Length - 1;
        String path = "";

        while ((pathRow != 0) || (pathCol != 0)) {
            if (pathRow == 0) {
                path = path + 'H';
                pathCol -= 1;
            } else if (pathCol == 0) {
                path = path + 'V';
                pathRow -= 1;
            } else if ((optMatrix[pathRow][pathCol - 1] + gap) == (optMatrix[pathRow][pathCol])) {
                path = path + 'H';
                pathCol -= 1;
            } else if ((optMatrix[pathRow - 1][pathCol] + gap) == (optMatrix[pathRow][pathCol])) {
                path = path + 'V';
                pathRow -= 1;
            } else {
                path = path + 'D';
                pathRow -= 1;
                pathCol -= 1;
            }
        }

        StringBuilder rp = new StringBuilder(path);

        String reversePath = rp.reverse().toString();

        if (showGraph == true) {
            System.out.println();
            System.out.println("Forward Path: " + path);
            System.out.println("Reverse Path: " + reversePath);
            System.out.println();
        }

        String seq1Alignment = "";
        String seq2Alignment = "";

        int seq1Location = seq1Length - 1;
        int seq2Location = seq2Length - 1;

        int pathLocation = 0;

        while (pathLocation < path.length()) {
            if (path.charAt(pathLocation) == 'D') {
                seq1Alignment = seq1.charAt(seq1Location) + seq1Alignment;
                seq2Alignment = seq2.charAt(seq2Location) + seq2Alignment;
                seq1Location--;
                seq2Location--;
            } else if (path.charAt(pathLocation) == 'V') {
                seq1Alignment = seq1.charAt(seq1Location) + seq1Alignment;
                seq2Alignment = '-' + seq2Alignment;
                seq1Location--;
            } else if (path.charAt(pathLocation) == 'H') {
                seq1Alignment = '-' + seq1Alignment;
                seq2Alignment = seq2.charAt(seq2Location) + seq2Alignment;
                seq2Location--;
            }
            pathLocation++;
        }

        System.out.println("Global Sequence Alignment: ");

        System.out.println(seq1Alignment);
        System.out.println(seq2Alignment);

        int count = 0;
        for (int i = 0; i < seq1Alignment.length(); i++) {
            if (seq1Alignment.charAt(i) == (seq2Alignment.charAt(i))) {
                count++;
            }
        }
        System.out.println();
        System.out.println("Total number of aligned characters: " + count);
        System.out.println("Percentage match: " + (double) count / (double) seq1Alignment.length());

        return optMatrix;
    }
}