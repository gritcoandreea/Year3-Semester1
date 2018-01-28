package com.company;

public class MatrixMultiplication  extends Thread implements Runnable{

    protected Matrix matrix1, matrix2, matrix3;
    protected int iStart, iStop;

    public MatrixMultiplication(Matrix matrix1, Matrix matrix2, Matrix matrix3, int iStart, int iStop) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrix3 = matrix3;
        this.iStart = iStart;
        this.iStop = iStop;
    }

    @Override
    public void run() {
        for (int i = iStart; i<iStop; i++){
            for (int j = 0; j<matrix2.getRows(); j++)
                for(int k = 0; k<matrix1.getColumns(); k++){
                try {
                    matrix3.setMatrix(i, j,

                            matrix3.getMatrix()[i][j] + matrix1.getMatrix()[i][k] * matrix2.getMatrix()[k][j]);
                }catch(ArrayIndexOutOfBoundsException e){

                }
                }
        }
    }
}
