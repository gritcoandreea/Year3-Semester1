public class Decoder2 {

    private Block[][] quantiziedY;
    private Block[][] quantiziedU;
    private Block[][] quantiziedV;
    private int[][] deQuantizationMatrix;
    private int width;
    private int height;

    private Block[][] dctY;
    private Block[][] dctU;
    private Block[][] dctV;

    private Block[][] blocksY;
    private Block[][] blocksU;
    private Block[][] blocksV;


    public Decoder2(Block[][] quantiziedY, Block[][] quantiziedU, Block[][] quantiziedV, int[][] deQuantizationMatrix, int width, int height) {
        this.quantiziedY = quantiziedY;
        this.quantiziedU = quantiziedU;
        this.quantiziedV = quantiziedV;
        this.deQuantizationMatrix = deQuantizationMatrix;
        this.width = width;
        this.height = height;
        this.dctY = new Block[this.height / 8][this.width / 8];
        this.dctU = new Block[this.height / 8][this.width / 8];
        this.dctV = new Block[this.height / 8][this.width / 8];
        this.blocksY = new Block[this.height / 8][this.width / 8];
        this.blocksU = new Block[this.height / 8][this.width / 8];
        this.blocksV = new Block[this.height / 8][this.width / 8];


    }

    public void run() {
        deQuantization();
        inversetDCT();
        add128();
    }

    private void add128() {

        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                //Block block = blocksY[i][j];
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        blocksY[i][j].getValues()[k][l] = 128 + blocksY[i][j].getValues()[k][l];
                        blocksU[i][j].getValues()[k][l] = 128 + blocksU[i][j].getValues()[k][l];
                        blocksV[i][j].getValues()[k][l] = 128 + blocksV[i][j].getValues()[k][l];
                    }
                }
            }
        }
    }

    private void inversetDCT() {

        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {

                //for each block
                Block dctBlockY = this.dctY[i][j];
                Block dctBlockU = this.dctU[i][j];
                Block dctBlockV = this.dctV[i][j];

                Block blockY = new Block("Y", dctBlockY.getPositionInImage1(), dctBlockY.getPositionInImage2(), 8);
                Block blockU = new Block("U", dctBlockU.getPositionInImage1(), dctBlockU.getPositionInImage2(), 8);
                Block blockV = new Block("V", dctBlockV.getPositionInImage1(), dctBlockV.getPositionInImage2(), 8);

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {

                        // for each element of a block
                        double finalResultY = 1.0 / 4.0;
                        double finalResultU = 1.0 / 4.0;
                        double finalResultV = 1.0 / 4.0;

                        double tempSumY = 0;
                        double tempSumU = 0;
                        double tempSumV = 0;

                        for (int u = 0; u < 8; u++) {
                            for (int v = 0; v < 8; v++) {

                                double tempProductY = 1;
                                double tempProductU = 1;
                                double tempProductV = 1;

                                if(u==0){
                                    tempProductY = tempProductY * (1.0/Math.sqrt(2));
                                    tempProductU = tempProductU * (1.0/Math.sqrt(2));
                                    tempProductV = tempProductV * (1.0/Math.sqrt(2));
                                }

                                if(v==0){
                                    tempProductY = tempProductY * (1.0/Math.sqrt(2));
                                    tempProductU = tempProductU * (1.0/Math.sqrt(2));
                                    tempProductV = tempProductV * (1.0/Math.sqrt(2));
                                }

                                tempProductY = tempProductY* dctBlockY.getValues()[u][v];
                                tempProductU = tempProductU* dctBlockU.getValues()[u][v];
                                tempProductV = tempProductV* dctBlockV.getValues()[u][v];

                                double tempCosY =0;
                                double tempCos2Y = 0;
                                double tempCosU =0;
                                double tempCos2U = 0;
                                double tempCosV =0;
                                double tempCos2V = 0;

                                tempCosY = ((2*x +1) * u * Math.PI)/16.0;

                                tempCos2Y = ((2*y +1) * v *Math.PI)/16.0;

                                tempCosU = ((2*x +1) * u * Math.PI)/16.0;

                                tempCos2U = ((2*y +1) * v *Math.PI)/16.0;

                                tempCosV = ((2*x +1) * u * Math.PI)/16.0;

                                tempCos2V = ((2*y +1) * v *Math.PI)/16.0;

                                tempProductY = tempProductY * Math.cos(tempCosY);
                                tempProductY = tempProductY * Math.cos(tempCos2Y);

                                tempProductU = tempProductU * Math.cos(tempCosU);
                                tempProductU = tempProductU * Math.cos(tempCos2U);

                                tempProductV = tempProductV * Math.cos(tempCosV);
                                tempProductV = tempProductV * Math.cos(tempCos2V);

                                tempSumY+= tempProductY;
                                tempSumU+= tempProductU;
                                tempSumV+= tempProductV;

                            }
                        }

                        finalResultY = finalResultY * tempSumY;
                        finalResultU = finalResultU * tempSumU;
                        finalResultV = finalResultV * tempSumV;
                        blockY.addValue(x,y,finalResultY);
                        blockU.addValue(x,y,finalResultU);
                        blockV.addValue(x,y,finalResultV);

                    }
                }

                this.blocksY[i][j] = blockY;
                this.blocksU[i][j] = blockU;
                this.blocksV[i][j] = blockV;

            }
        }
    }

    private void deQuantization() {
        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                //for each block
                Block blockY = this.quantiziedY[i][j];
                Block blockU = this.quantiziedU[i][j];
                Block blockV = this.quantiziedV[i][j];

                Block dctBlockY = new Block("Y", blockY.getPositionInImage1(), blockY.getPositionInImage2(), 8);
                Block dctBlockU = new Block("U", blockU.getPositionInImage1(), blockU.getPositionInImage2(), 8);
                Block dctBlockV = new Block("V", blockV.getPositionInImage1(), blockV.getPositionInImage2(), 8);


                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        double valueY =  blockY.getValues()[k][l] * this.deQuantizationMatrix[k][l];
                        double valueU = blockU.getValues()[k][l] * this.deQuantizationMatrix[k][l];
                        double valueV = blockV.getValues()[k][l] * this.deQuantizationMatrix[k][l];
                        dctBlockY.addValue(k, l, valueY);
                        dctBlockU.addValue(k, l, valueU);
                        dctBlockV.addValue(k, l, valueV);
                    }
                }

                this.dctY[i][j] = dctBlockY;
                this.dctU[i][j] = dctBlockU;
                this.dctV[i][j] = dctBlockV;
            }
        }
    }

    public Block[][] getBlocksY() {
        return blocksY;
    }

    public Block[][] getBlocksU() {
        return blocksU;
    }

    public Block[][] getBlocksV() {
        return blocksV;
    }

    public Block[][] getQuantiziedY() {
        return quantiziedY;
    }

    public Block[][] getQuantiziedU() {
        return quantiziedU;
    }

    public Block[][] getQuantiziedV() {
        return quantiziedV;
    }

    public Block[][] getDctY() {
        return dctY;
    }

    public Block[][] getDctU() {
        return dctU;
    }

    public Block[][] getDctV() {
        return dctV;
    }
}
