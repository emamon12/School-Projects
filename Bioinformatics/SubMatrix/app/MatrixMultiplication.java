package app;

public class MatrixMultiplication {
    protected double[][] multiply(double[][] matrix1, double[][] matrix2) {
        double[][] tempMatrix = new double[matrix1.length][matrix1.length];
        double sum;

        for (int i = 0; i < matrix1.length; i++) {
            tempMatrix[i] = new double[matrix1.length];
            for (int j = 0; j < matrix1.length; j++) {
                sum = 0;
                for (int k = 0; k < matrix1.length; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                tempMatrix[i][j] = sum;
            }
        }
        return tempMatrix;
    }
}