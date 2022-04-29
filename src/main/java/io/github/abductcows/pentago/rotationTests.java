package io.github.abductcows.pentago;

import java.util.Arrays;

public class rotationTests {

    public static void main(String[] args) {

        Integer[][] a = new Integer[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        printArr(a);
        rotateLeft90(a);
        printArr(a);
    }


    public static <T> void rotateLeft90(T[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                T temp = matrix[i][j];
                matrix[i][j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = temp;
            }
        }
    }

    public static <T> void rotateRight90(T[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                T temp = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[i][j];
                matrix[i][j] = temp;
            }
        }
    }

    static <T> void printArr(T[][] a) {
        for (var row : a) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
