package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends IOFile {

    public static void main(String[] args) throws InterruptedException, IOException {
        int n, m, n2, m2, n3, m3, nrThreads;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Give n for matrix 1: ");
        n = Integer.parseInt(br.readLine());
        System.out.println("Give m for matrix1: ");
        m = Integer.parseInt(br.readLine());
        System.out.println("Give n for matrix 2: ");
        n2 = Integer.parseInt(br.readLine());
        System.out.println("Give m for matrix 2: ");
        m2 = Integer.parseInt(br.readLine());
        System.out.println("Give n for matrix 3: ");
        n3 = Integer.parseInt(br.readLine());
        System.out.println("Give m for matrix 3: ");
        m3 = Integer.parseInt(br.readLine());

        System.out.println("Give nr of threads: ");
        nrThreads = Integer.parseInt(br.readLine());

        writeMatrixInFile(n, m, "matriceA.txt");
        writeMatrixInFile(n2, m2, "matriceB.txt");
        writeMatrixInFile(n2, m2, "matriceC.txt");

        int[][] matrix1 = new int[n][m];
        int[][] matrix2 = new int[n2][m2];
        int[][] matrix3 = new int[n3][m3];

        readMatrixFromFile(n, m, matrix1, "matriceA.txt");
        readMatrixFromFile(n2, m2, matrix2, "matriceB.txt");
        readMatrixFromFile(n3, m3, matrix3, "matriceC.txt");

        Matrix first_matrix = new Matrix(n, m, matrix1);
        Matrix second_matrix = new Matrix(n2, m2, matrix2);
        Matrix third_matrix = new Matrix(n3, m3, matrix3);
        Matrix resultMatrix2 = Matrix.multiplyMatricesThreadPool(first_matrix, second_matrix, third_matrix, nrThreads);

        //resultMatrix2.printMatrix();
    }
}
