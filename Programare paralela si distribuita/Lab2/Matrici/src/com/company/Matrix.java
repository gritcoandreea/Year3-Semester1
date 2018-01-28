package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Matrix {

    public int rows;
    public int columns;
    private int[][] matrix;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix= new int[rows][columns];
    }

    public Matrix() {
    }

    public Matrix(int rows, int columns, int[][] matrix) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
    }



    public static Matrix addMatricesTreadPool(Matrix m1, Matrix m2, int nrThreads) throws InterruptedException{
        int nrElements = m1.getRows()* m1.getColumns();
        int rowColumnGroups = 0; //0 for row , 1 for column, 2 for groups

        Thread [] threads = new Thread[nrThreads];
        ExecutorService service = Executors.newFixedThreadPool(nrThreads);

        Matrix resultMatrix = new Matrix(m1.getRows(),m2.getColumns());

        if(nrThreads <= m1.getRows()){
            int iStart = 0, iStop;
            rowColumnGroups = 0;
            int nrRowsPerThread = m1.getRows() / nrThreads;
            int remainder = m1.getRows() % nrThreads;
            for(int i=0;i<nrThreads;i++){
                iStop = iStart + nrRowsPerThread;
                if (remainder > 0) {
                    iStop += 1;
                    remainder--;
                }
                threads[i] = new MatrixAddition(m1, m2, resultMatrix, iStart, iStop,-1,-1,-1,rowColumnGroups);
                iStart = iStop;
            }
        }else if(nrThreads <=m1.getColumns()){
            int iStart = 0, iStop;
            rowColumnGroups = 1;
            int nrColumnsPerThread = m1.getColumns() / nrThreads;
            int remainder = m1.getColumns() % nrThreads;
            for(int i=0;i<nrThreads;i++){
                iStop = iStart + nrColumnsPerThread;
                if (remainder > 0) {
                    iStop += 1;
                    remainder--;
                }
                threads[i] = new MatrixAddition(m1, m2, resultMatrix, iStart, iStop,-1,-1,-1,rowColumnGroups);
                iStart = iStop;
            }
        }else{
            //mergi pe grupuri de elemente
            rowColumnGroups = 3;
            int nrElementsPerThread = nrElements / nrThreads;
            int remainder = nrElements % nrThreads;

            int iStart=0,iStop=0,jStart=0,jStop=0;
            int record=0;
            for(int i=0;i<nrThreads;i++){
                record = record + nrElementsPerThread;
                int record2 = 0;
                record2 = nrElementsPerThread;
                if(remainder>0){
                    record=record+1;
                    record2++;
                    remainder--;
                }
                List<Integer> l = m1.getIndex(record);
                iStop = l.get(0);
                jStop = l.get(1);

                threads[i] = new MatrixAddition(m1,m2,resultMatrix,iStart,iStop,jStart,jStop,record2,rowColumnGroups);
                iStart = iStop;
                jStart = jStop;

            }
        }


        long startTime = System.currentTimeMillis();
        for (int i = 0; i < nrThreads; i++) {
            service.execute(threads[i]);
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + (endTime - startTime));

        System.out.println(resultMatrix.toString());
        return resultMatrix;
    }

    public static Matrix addMatricesFuture(Matrix m1, Matrix m2, int nrThreads) throws InterruptedException{
        int nrElements = m1.getRows()* m1.getColumns();
        int rowColumnGroups = 0; //0 for row , 1 for column, 2 for groups

        List<MatrixAddition> threads = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(nrThreads);

        Matrix resultMatrix = new Matrix(m1.getRows(),m2.getColumns());

        if(nrThreads <= m1.getRows()){
            int iStart = 0, iStop;
            rowColumnGroups = 0;
            int nrRowsPerThread = m1.getRows() / nrThreads;
            int remainder = m1.getRows() % nrThreads;
            for(int i=0;i<nrThreads;i++){
                iStop = iStart + nrRowsPerThread;
                if (remainder > 0) {
                    iStop += 1;
                    remainder--;
                }
                threads.add(new MatrixAddition(m1, m2, resultMatrix, iStart, iStop,-1,-1,-1,rowColumnGroups));
                iStart = iStop;
            }
        }else if(nrThreads <=m1.getColumns()){
            int iStart = 0, iStop;
            rowColumnGroups = 1;
            int nrColumnsPerThread = m1.getColumns() / nrThreads;
            int remainder = m1.getColumns() % nrThreads;
            for(int i=0;i<nrThreads;i++){
                iStop = iStart + nrColumnsPerThread;
                if (remainder > 0) {
                    iStop += 1;
                    remainder--;
                }
                threads.add(new MatrixAddition(m1, m2, resultMatrix, iStart, iStop,-1,-1,-1,rowColumnGroups));
                iStart = iStop;
            }
        }else{
            //mergi pe grupuri de elemente
            rowColumnGroups = 3;
            int nrElementsPerThread = nrElements / nrThreads;
            int remainder = nrElements % nrThreads;

            int iStart=0,iStop=0,jStart=0,jStop=0;
            int record=0;
            for(int i=0;i<nrThreads;i++){
                record = record + nrElementsPerThread;
                int record2 = 0;
                record2 = nrElementsPerThread;
                if(remainder>0){
                    record=record+1;
                    record2++;
                    remainder--;
                }
                List<Integer> l = m1.getIndex(record);
                iStop = l.get(0);
                jStop = l.get(1);

                threads.add(new MatrixAddition(m1,m2,resultMatrix,iStart,iStop,jStart,jStop,record2,rowColumnGroups));
                iStart = iStop;
                jStart = jStop;

            }
        }


        long startTime = System.currentTimeMillis();
        for (Thread t : threads) {
            Future future = service.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    t.run();
                    return "OK";
                }
            });
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + (endTime - startTime));

        System.out.println(resultMatrix.toString());
        return resultMatrix;
    }


    public static Matrix multiplyMatricesThreadPool(Matrix matrix1, Matrix matrix2, int threadNo) throws InterruptedException {
        int tasksPerThread = matrix1.getRows() / threadNo;
        int remainder = matrix1.getRows() % threadNo;
        Thread[] threads = new Thread[threadNo];
        Matrix resultMatrix = new Matrix(matrix1.getRows(), matrix2.getColumns());

        ExecutorService service = Executors.newFixedThreadPool(threadNo);


        int iStart = 0, iStop;
        for (int i = 0; i < threadNo; i++) {
            iStop = iStart + tasksPerThread;
            if (remainder > 0) {
                iStop += 1;
                remainder--;
            }
            threads[i] = new MatrixMultiplication(matrix1, matrix2, resultMatrix, iStart, iStop);
            iStart = iStop;
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNo; i++) {
            service.execute(threads[i]);
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.currentTimeMillis();

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + (endTime - startTime));

        System.out.println(resultMatrix.toString());
        return resultMatrix;
    }

    public static Matrix multiplyMatricesFuture(Matrix matrix1, Matrix matrix2, int threadNo) throws InterruptedException {
        int tasksPerThread = matrix1.getRows() / threadNo;
        int remainder = matrix1.getRows() % threadNo;
        Matrix resultMatrix = new Matrix(matrix1.getRows(), matrix2.getColumns());
        List<MatrixMultiplication> threads = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(threadNo);
        int iStart = 0, iStop;
        for (int i = 0; i < threadNo; i++) {
            iStop = iStart + tasksPerThread;
            if (remainder > 0) {
                iStop += 1;
                remainder--;
            }
            threads.add(new MatrixMultiplication(matrix1, matrix2, resultMatrix, iStart, iStop));
            iStart = iStop;
        }

        long startTime = System.currentTimeMillis();
        for (final Thread thread : threads) {
           Future future = service.submit(new Callable<String>() {

               @Override
               public String call() throws Exception {
                   thread.run();
                   return "OK";
               }
           });
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + (endTime - startTime));


        System.out.println(resultMatrix.toString());
        return resultMatrix;
    }

    private List<Integer> getIndex(int nrelements){
        int nr=0;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                nr=nr+1;
                if(nr==nrelements){
                    list.add(i);
                    list.add(j);
                }
            }
        }
        return list;
    }



    private static void print(Matrix matrix){
        for(int i=0;i<matrix.getRows();i++) {
            for (int j = 0; j < matrix.getColumns(); j++)
                System.out.print(matrix.getMatrix()[i][j] + " ");
            System.out.println();
        }
    }

    public void printMatrix(){
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
        this.matrix[i][j]= sum;
    }
}
