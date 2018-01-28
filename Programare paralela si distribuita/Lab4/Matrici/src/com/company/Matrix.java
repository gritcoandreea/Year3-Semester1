package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Matrix {

    public int rows;
    public int columns;
    private int[][] matrix;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix = new int[rows][columns];
    }

    public Matrix() {
    }

    public Matrix(int rows, int columns, int[][] matrix) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
    }


    public static Matrix multiplyMatricesThreadPool(Matrix matrix1, Matrix matrix2, Matrix matrix3, int threadNo) throws InterruptedException {
        int tasksPerThread = matrix1.getRows() / threadNo;
        int remainder = matrix1.getRows() % threadNo;
        Thread[] threads = new Thread[threadNo];
        Thread[] finalThreads = new Thread[matrix3.getRows()];
        Matrix resultMatrix = new Matrix(matrix1.getRows(), matrix2.getColumns());
        Matrix intermediaryMatrix = new Matrix(matrix1.getRows(), matrix3.getColumns());

        ExecutorService intermediaryService = Executors.newFixedThreadPool(threadNo);
        ExecutorService finalService = Executors.newFixedThreadPool(matrix3.getRows());

        final Lock lock = new ReentrantLock();

        final Condition condition = lock.newCondition();

        int[] completedRows = new int[matrix3.getRows()];

        for (int i = 0; i < matrix3.getRows(); i++) {
            completedRows[i] = 0;
        }

        int iStart = 0, iStop;
        for (int i = 0; i < threadNo; i++) {
            iStop = iStart + tasksPerThread;
            if (remainder > 0) {
                iStop += 1;
                remainder--;
            }
            threads[i] = new MatrixMultiplication(matrix1, matrix2, intermediaryMatrix, lock, condition, completedRows, iStart, iStop);
            iStart = iStop;
        }

        iStart = 0;
        iStop = 1;
        for (int i = 0; i < matrix3.getRows(); i++) {
            finalThreads[i] = new MatrixMultiplication(intermediaryMatrix, matrix3, resultMatrix, null, null, null, iStart, iStop);
            iStart = iStop;
            iStop++;
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNo; i++) {
            intermediaryService.execute(threads[i]);
        }
        for (int j = 0; j < matrix3.getRows(); j++) {
            lock.lock();
            while (completedRows[j] != matrix3.getRows()) {
                condition.await();
            }
            finalService.execute(finalThreads[j]);
            lock.unlock();
        }

        intermediaryService.shutdown();
        intermediaryService.awaitTermination(1, TimeUnit.SECONDS);
        finalService.shutdown();
        finalService.awaitTermination(1, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + (endTime - startTime));

     //   System.out.println(resultMatrix.toString());
        return resultMatrix;
    }

    public void printMatrix() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int i, int j, int sum) {
        this.matrix[i][j] = sum;
    }
}
