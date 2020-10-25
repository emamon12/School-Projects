package app;

public class AffineAlignment {

    protected void getAffineAlignment(String sequence1, String sequence2, int matchScore, int mismatchPen,
            int gapStartPen, int gapExtendPen, boolean graph) {
        // Initializing two strings

        String m = sequence1;
        String n = sequence2;

        boolean showGraph = graph;

        m = " " + m;
        n = " " + n;

        int seq1Length = m.length();
        int seq2Length = n.length();

        int s;

        int dMatrix[][] = new int[seq1Length][seq2Length];
        int hMatrix[][] = new int[seq1Length][seq2Length];
        int vMatrix[][] = new int[seq1Length][seq2Length];

        int match = matchScore;
        int mismatch = mismatchPen;
        int gapExtend = gapExtendPen;
        int gapStart = gapStartPen;

        int hval1;
        int hval2;

        int vval1;
        int vval2;

        int val1;
        int val2;
        int val3;

        int maxPenalty = (seq1Length + seq2Length) * gapStart;

        for (int i = 0; i < m.length(); i++) {
            for (int j = 0; j < n.length(); j++) {
                dMatrix[i][j] = maxPenalty;
                hMatrix[i][j] = maxPenalty;
                vMatrix[i][j] = maxPenalty;
            }
        }

        dMatrix[0][0] = 0;
        hMatrix[0][1] = gapStart;
        vMatrix[1][0] = gapStart;

        for (int i = 2; i < seq1Length; i++) {
            vMatrix[i][0] = gapStart + (i - 1) * gapExtend;
        }

        for (int i = 2; i < seq2Length; i++) {
            hMatrix[0][i] = gapStart + (i - 1) * gapExtend;
        }

        for (int x = 1; x < seq1Length; x++) {
            for (int y = 1; y < seq2Length; y++) {
                if (m.charAt(x) == n.charAt(y)) {
                    s = match;
                } else {
                    s = mismatch;
                }

                if (hMatrix[x][y - 1] == maxPenalty) {
                    hval1 = maxPenalty;
                } else {
                    hval1 = hMatrix[x][y - 1] + gapExtend;
                }

                if (dMatrix[x][y - 1] == maxPenalty) {
                    hval2 = maxPenalty;
                } else {
                    hval2 = dMatrix[x][y - 1] + gapStart;
                }

                if (vMatrix[x - 1][y] == maxPenalty) {
                    vval1 = maxPenalty;
                } else {
                    vval1 = vMatrix[x - 1][y] + gapExtend;
                }

                if (dMatrix[x - 1][y] == maxPenalty) {
                    vval2 = maxPenalty;
                } else {
                    vval2 = dMatrix[x - 1][y] + gapStart;
                }

                if (hMatrix[x - 1][y - 1] == maxPenalty) {
                    val1 = maxPenalty;
                } else {
                    val1 = hMatrix[x - 1][y - 1] + s;
                }

                if (vMatrix[x - 1][y - 1] == maxPenalty) {
                    val2 = maxPenalty;
                } else {
                    val2 = vMatrix[x - 1][y - 1] + s;
                }

                if (dMatrix[x - 1][y - 1] == maxPenalty) {
                    val3 = maxPenalty;
                } else {
                    val3 = dMatrix[x - 1][y - 1] + s;
                }

                if ((val1 >= val2) && (val1 >= val3)) {
                    dMatrix[x][y] = val1;
                } else if ((val2 >= val1) && (val2 >= val3)) {
                    dMatrix[x][y] = val2;
                } else {
                    dMatrix[x][y] = val3;
                }

                if (hval1 >= hval2) {
                    hMatrix[x][y] = hval1;
                } else {
                    hMatrix[x][y] = hval2;
                }

                if (vval1 >= vval2) {
                    vMatrix[x][y] = vval1;
                } else {
                    vMatrix[x][y] = vval2;
                }

            }
        }

        if (showGraph == true) {
            System.out.println("Middle Matrix: ");
            for (int x = 0; x < seq1Length; x++) {
                for (int y = 0; y < seq2Length; y++) {
                    System.out.printf("%4d", dMatrix[x][y]);
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("Vertical Matrix: ");
            for (int x = 0; x < seq1Length; x++) {
                for (int y = 0; y < seq2Length; y++) {
                    System.out.printf("%4d", vMatrix[x][y]);
                }
                System.out.println();
            }

            System.out.println();

            System.out.println("Horizontal Matrix");
            for (int x = 0; x < seq1Length; x++) {
                for (int y = 0; y < seq2Length; y++) {
                    System.out.printf("%4d", hMatrix[x][y]);
                }
                System.out.println();
            }

            System.out.println();
        }

        int lastV = vMatrix[seq1Length - 1][seq2Length - 1];
        int lastD = dMatrix[seq1Length - 1][seq2Length - 1];
        int lastH = hMatrix[seq1Length - 1][seq2Length - 1];

        String layer = "";

        if ((lastV >= lastH) && (lastV >= lastD)) {
            layer = "V";
        }

        if ((lastH >= lastH) && (lastH >= lastD)) {
            layer = "H";
        }

        if ((lastD >= lastV) && (lastD >= lastH)) {
            layer = "D";
        }

        System.out.print("Initial Backtrack Layer: ");
        System.out.println(layer);

        int pathRow = seq1Length - 1;
        int pathCol = seq2Length - 1;
        String path = "";

        while ((pathRow != 0) || (pathCol != 0)) {
            if (m.charAt(pathRow) == n.charAt(pathCol)) {
                s = match;
            } else {
                s = mismatch;
            }
            if (pathRow == 0) {
                path = path + 'H';
                pathCol = pathCol - 1;
                layer = "H";
            } else if (pathCol == 0) {
                path = path + 'V';
                pathRow = pathRow - 1;
                layer = "V";
            } else if (layer.equals("D")) {
                if (dMatrix[pathRow][pathCol] == hMatrix[pathRow - 1][pathCol - 1] + s) {
                    layer = "H";
                } else if (dMatrix[pathRow][pathCol] == vMatrix[pathRow - 1][pathCol - 1] + s) {
                    layer = "V";
                }
                path = path + 'D';
                pathRow = pathRow - 1;
                pathCol = pathCol - 1;
            } else if (layer.equals("H")) {
                if (hMatrix[pathRow][pathCol] == dMatrix[pathRow][pathCol - 1] + gapStart) {
                    layer = "D";
                }
                path = path + 'H';
                pathCol = pathCol - 1;
            } else if (layer.equals("V")) {
                if (vMatrix[pathRow][pathCol] == dMatrix[pathRow - 1][pathCol] + gapStart) {
                    layer = "D";
                }
                path = path + 'V';
                pathRow = pathRow - 1;
            }
        }

        StringBuilder pr = new StringBuilder(path);

        String forwardPath = pr.reverse().toString();

        if (showGraph == true) {
            System.out.println();
            System.out.println("Reverse Path is: " + path);
            System.out.println("Forward Path is: " + forwardPath);
            System.out.println();
        }

        int seq1Location = seq1Length - 1;
        int seq2Location = seq2Length - 1;
        int pathLocation = 0;

        String alignSeq1 = "";
        String alignSeq2 = "";

        while (pathLocation < path.length()) {
            if (path.charAt(pathLocation) == 'D') {
                alignSeq1 = m.charAt(seq1Location) + alignSeq1;
                alignSeq2 = n.charAt(seq2Location) + alignSeq2;
                seq1Location--;
                seq2Location--;
            } else if (path.charAt(pathLocation) == 'V') {
                alignSeq1 = m.charAt(seq1Location) + alignSeq1;
                alignSeq2 = '-' + alignSeq2;
                seq1Location--;
            } else if (path.charAt(pathLocation) == 'H') {
                alignSeq1 = '-' + alignSeq1;
                alignSeq2 = n.charAt(seq2Location) + alignSeq2;
                seq2Location--;
            }
            pathLocation++;
        }

        System.out.println("Affine Sequence Alignment: ");

        System.out.println(alignSeq1);
        System.out.println(alignSeq2);

        int count = 0;
        for (int i = 0; i < alignSeq1.length(); i++) {
            if (alignSeq1.charAt(i) == (alignSeq2.charAt(i))) {
                count++;
            }
        }

        System.out.println();
        System.out.println("Total number of aligned characters: " + count);
        System.out.println("Percentage match: " + (double) count / (double) alignSeq1.length());

    }
}
