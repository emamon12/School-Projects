package app;

public class MatrixPowering {
    protected double[][] power(double[][] matrix1, int evolDistance) {
        double[][] result = matrix1;
        MatrixMultiplication aa = new MatrixMultiplication();

        for (int x = 1; x < evolDistance; x++) {
            result = aa.multiply(result, matrix1);
        }
        
        return result;
    }
}