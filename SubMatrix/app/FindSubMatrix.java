package app;

public class FindSubMatrix {

    protected int[][] findSubMatrix(double[][] probMatrix, double[] aminoFreq) {
        int[][] subMatrix = new int[20][20];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                subMatrix[j][i] = subMatrix[i][j] = (int) (0.5
                        + (10 * (Math.log(probMatrix[j][i] / aminoFreq[j])) / Math.log(10)
                                + 10 * (Math.log(probMatrix[i][j] / aminoFreq[i])) / Math.log(10)) / 2);
            }
        }
        return subMatrix;
    }
}