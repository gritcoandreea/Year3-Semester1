package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main  extends IOFile{

    public static void main(String[] args) throws InterruptedException,IOException{
	    int n,m,nn,mm,nrThreads;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Give n for matrix 1: ");
        n=Integer.parseInt(br.readLine());
        System.out.println("Give n for matrix1: ");
        m= Integer.parseInt(br.readLine());
        System.out.println("Give n for matrix 2: ");
        nn=Integer.parseInt(br.readLine());
        System.out.println("Give m for matrix 1: ");
        mm = Integer.parseInt(br.readLine());

        System.out.println("Give nr of threads: ");
        nrThreads = Integer.parseInt(br.readLine());

        writeMatrixInFile(n,m,"matriceA.txt");
        writeMatrixInFile(nn,mm,"matriceB.txt");

        int[][] m1 = new int[n][m];
        int[][] m2 = new int[nn][mm];

        readMatrixFromFile(n,m,m1,"matriceA.txt");
        readMatrixFromFile(nn,mm,m2,"matriceB.txt");

        Matrix matrix1 = new Matrix(n,m,m1);
        Matrix matrix2 = new Matrix(nn,mm,m2);
        Matrix resultMatrix = Matrix.addMatricesTreadPool(matrix1,matrix2,nrThreads);
       Matrix resultMatrix2 = Matrix.multiplyMatricesThreadPool(matrix1,matrix2,nrThreads);
        resultMatrix.printMatrix();
        resultMatrix2.printMatrix();
    }
}
