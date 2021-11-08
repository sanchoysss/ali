package by.helpers;

public class MatrixOperations {

    public static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixB[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                double sum = 0d;
                for (int k = 0; k < matrixA[0].length; k++) {
                    double var = matrixA[i][k] * matrixB[k][j];
                    sum += var;
                }
                result[i][j] = sum;
            }
        }
        return result;
    }
}
