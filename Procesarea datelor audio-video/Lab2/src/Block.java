import java.util.Arrays;

public class Block {

    /** Y,U,V*/
    private String blockType;
    private int positionInImage1;
    private int positionInImage2;
    /** 8 - Y , 4 - U and V*/
    private int nrValues;
    private double values[][];

    public Block(String blockType, int positionInImage, int positionInImage2, int nrValues) {
        this.blockType = blockType;
        this.positionInImage1 = positionInImage;
        this.positionInImage2 = positionInImage2;
        this.nrValues = nrValues;
        this.values = new double[nrValues][nrValues];
    }

    public Block(String blockType, int positionInImage, int positionInImage2,int nrValues, double[][] values) {
        this.blockType = blockType;
        this.positionInImage1 = positionInImage;
        this.positionInImage2 = positionInImage2;
        this.nrValues = nrValues;
        this.values = values;
    }

    public void addValue(int i, int j, double value){
        this.values[i][j] = value;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public int getPositionInImage1() {
        return positionInImage1;
    }

    public int getPositionInImage2() {
        return positionInImage2;
    }

    public void setPositionInImage2(int positionInImage2) {
        this.positionInImage2 = positionInImage2;
    }

    public void setPositionInImage1(int positionInImage) {
        this.positionInImage1 = positionInImage;
    }

    public int getNrValues() {
        return nrValues;
    }

    public void setNrValues(int nrValues) {
        this.nrValues = nrValues;
    }

    public double[][] getValues() {
        return values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockType='" + blockType + '\'' +
                ", positionInImage1=" + positionInImage1 +
                ", positionInImage2=" + positionInImage2 +
                ", nrValues=" + nrValues +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public void printValues(){
        for(int i=0;i<nrValues;i++){
            for(int j=0;j<nrValues;j++){
                System.out.print(values[i][j]);
                System.out.print("  ");
            }
            System.out.println();
        }
    }
}
