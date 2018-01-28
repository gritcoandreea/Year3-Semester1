package com.company;

public class MatrixAddition extends Thread  implements  Runnable{

    protected Matrix m1,m2,m3;
    protected  int iStart,iStop, jStart, jStop,record,rowColumnGroup;

    public MatrixAddition(Matrix m1, Matrix m2, Matrix m3, int iStart, int iStop,int jStart,int jStop,int record, int rowColumnGroup) {
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.iStart = iStart;
        this.iStop = iStop;
        this.jStart = jStart;
        this.jStop = jStop;
        this.record = record;
        this.rowColumnGroup = rowColumnGroup;
    }

    @Override
    public void run() {
       if(rowColumnGroup == 0){
           for (int i = iStart; i < iStop; i++){
               for (int j = 0; j < m1.getColumns(); j++){
                   m3.setMatrix(i, j, m1.getMatrix()[i][j]+ m2.getMatrix()[i][j]);
               }
           }
       }
       else if(rowColumnGroup ==1){
           for(int i=0;i<m1.getRows();i++){
               for(int j=iStart;j< iStop;j++){
                   m3.setMatrix(i,j,m1.getMatrix()[i][j] + m2.getMatrix()[i][j]);
               }
           }
       }else{
           int nr=0;
           for(int i=iStart;i<m1.getRows();i++){
               for(int j=jStart;j<m1.getColumns();j++){
                   m3.setMatrix(i,j,m1.getMatrix()[i][j] + m2.getMatrix()[i][j]);
                   nr=nr+1;
                   if(nr==record){
                       break;

                   }
               }
           }
       }
    }
}
