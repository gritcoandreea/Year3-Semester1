class Encoder2 {

    private Encoder encoder;
    private int width;
    private int height;
    private Block[][] blocksY;
    private Block[][] blocksU;
    private Block[][] blocksV;
    private Block[][] dctY;
    private Block[][] dctU;
    private Block[][] dctV;
    private Block[][] quantiziedY;
    private Block[][] quantiziedU;
    private Block[][] quantiziedV;
    private int[][] quantizationMatrix = {{6, 4, 4, 6, 10, 16, 20, 24},
                                         {5, 5, 6, 8, 10, 23, 24, 22},
                                         {6, 5, 6, 10, 16, 23, 28, 22},
                                         {6, 7, 9, 12, 20, 35, 32, 25},
                                         {7, 9, 15, 22, 27, 44, 41, 31},
                                         {10, 14, 22, 26, 32, 42, 45, 37},
                                         {20, 26, 31, 35, 41, 48, 48, 40},
                                         {29, 37, 38, 39, 45, 40, 41, 40}};


    public Encoder2(final Encoder encoder) {
        this.encoder = encoder;
        this.width = encoder.getImage().getImageWidth();
        this.height = encoder.getImage().getImageHeight();

        this.blocksY = encoder.getBlocksY();
        this.blocksU = new Block[this.height / 8][this.width / 8];
        this.blocksV = new Block[this.height / 8][this.width / 8];

        this.dctY = new Block[this.height / 8][this.width / 8];
        this.dctU = new Block[this.height / 8][this.width / 8];
        this.dctV = new Block[this.height / 8][this.width / 8];

        this.quantiziedY = new Block[this.height / 8][this.width / 8];
        this.quantiziedU = new Block[this.height / 8][this.width / 8];
        this.quantiziedV = new Block[this.height / 8][this.width / 8];
    }

    public void run() {
        //convert 4x4 blocks back to 8x8

        transformBlocksUV(); //ok
//        blocksU[0][0].printValues();
//        System.out.println("----------------------pula");
        substract128(); //ok
        discreteCosineTransform();
        quantization();
    }

    private void quantization() {
        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                //for each block
                Block blockY = this.dctY[i][j];
                Block blockU = this.dctU[i][j];
                Block blockV = this.dctV[i][j];

                Block quantBlockY = new Block("Y", blockY.getPositionInImage1(), blockY.getPositionInImage2(), 8);
                Block quantBlockU = new Block("U", blockU.getPositionInImage1(), blockU.getPositionInImage2(), 8);
                Block quantBlockV = new Block("V", blockV.getPositionInImage1(),blockV.getPositionInImage2(), 8);

                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        int valueY = (int) blockY.getValues()[k][l] / this.quantizationMatrix[k][l];
                        int valueU = (int) blockU.getValues()[k][l] / this.quantizationMatrix[k][l];
                        int valueV = (int) blockV.getValues()[k][l] / this.quantizationMatrix[k][l];
                        quantBlockY.addValue(k, l, valueY);
                        quantBlockU.addValue(k, l, valueU);
                        quantBlockV.addValue(k, l, valueV);
                    }
                }

                this.quantiziedY[i][j] = quantBlockY;
                this.quantiziedU[i][j] = quantBlockU;
                this.quantiziedV[i][j] = quantBlockV;
            }
        }
    }

    private void discreteCosineTransform() {
        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                Block blockY = this.blocksY[i][j];
                Block blockU = this.blocksU[i][j];
                Block blockV = this.blocksV[i][j];

                Block blockDCTY = new Block("Y", blockY.getPositionInImage1(), blockY.getPositionInImage2(), 8);
                Block blockDCTU = new Block("U", blockU.getPositionInImage1(), blockU.getPositionInImage2(), 8);
                Block blockDCTV = new Block("V", blockV.getPositionInImage1(), blockV.getPositionInImage2(), 8);

                //for each block
                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        double finalResultY = 1.0 / 4.0;
                        double finalResultU = 1.0 / 4.0;
                        double finalResultV = 1.0 / 4.0;

                        if (u == 0) {
                            finalResultY = finalResultY * (1.0 / Math.sqrt(2));
                            finalResultU = finalResultU * (1.0 / Math.sqrt(2));
                            finalResultV = finalResultV * (1.0 / Math.sqrt(2));
                        }
                        if (v == 0) {
                            finalResultY = finalResultY * (1.0 / Math.sqrt(2));
                            finalResultU = finalResultU * (1.0 / Math.sqrt(2));
                            finalResultV = finalResultV * (1.0 / Math.sqrt(2));
                        }


                        double tempSumY = 0;
                        double tempSumU = 0;
                        double tempSumV = 0;

                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {

                                double tempProductY = 1.0;
                                double tempProductU = 1.0;
                                double tempProductV = 1.0;

                                tempProductY = tempProductY * blockY.getValues()[x][y];
                                tempProductU = tempProductU * blockU.getValues()[x][y];
                                tempProductV = tempProductV * blockV.getValues()[x][y];

                                double valU_Y = 0;
                                double valU_U = 0;
                                double valU_V = 0;


                                double valV_Y = 0;
                                double valV_U = 0;
                                double valV_V = 0;


                                valU_Y = ((2 * x + 1) * u * Math.PI) / 16.0;
                                valU_U = ((2 * x + 1) * u * Math.PI) / 16.0;
                                valU_V = ((2 * x + 1) * u * Math.PI) / 16.0;


                                tempProductY = tempProductY * Math.cos(valU_Y);
                                tempProductU = tempProductU * Math.cos(valU_U);
                                tempProductV = tempProductV * Math.cos(valU_V);

                                valV_Y = ((2 * y + 1) * v * Math.PI) / 16.0;
                                valV_U = ((2 * y + 1) * v * Math.PI) / 16.0;
                                valV_V = ((2 * y + 1) * v * Math.PI) / 16.0;

                                tempProductY = tempProductY * Math.cos(valV_Y);
                                tempProductU = tempProductU * Math.cos(valV_U);
                                tempProductV = tempProductV * Math.cos(valV_V);

                                tempSumY += tempProductY;
                                tempSumU += tempProductU;
                                tempSumV += tempProductV;

                            }
                        }
                        //after sum
                        finalResultY = finalResultY * tempSumY;
                        finalResultU = finalResultU * tempSumU;
                        finalResultV = finalResultV * tempSumV;

                        blockDCTY.addValue(u, v, finalResultY);
                        blockDCTU.addValue(u, v, finalResultU);
                        blockDCTV.addValue(u, v, finalResultV);
                    }
                }

                this.dctY[i][j] = blockDCTY;
                this.dctU[i][j] = blockDCTU;
                this.dctV[i][j] = blockDCTV;
            }
        }
    }

    private void substract128() {

        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                //Block block = blocksY[i][j];
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        blocksY[i][j].getValues()[k][l] = blocksY[i][j].getValues()[k][l] - 128;
                        blocksU[i][j].getValues()[k][l] = blocksU[i][j].getValues()[k][l] - 128;
                        blocksV[i][j].getValues()[k][l] = blocksV[i][j].getValues()[k][l] - 128;
                    }
                }
            }
        }

    }

    private void transformBlocksUV() {
        Block[][] blocksUEncoder = this.encoder.getBlocksU();
        Block[][] blocksVEncoder = this.encoder.getBlocksV();

        int pos1= 0;
        int pos2 = 0;
        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {

                Block block4 = blocksUEncoder[i][j];
                Block blockV4 = blocksVEncoder[i][j];

                Block block8 = new Block("U", pos1, pos2, 8);
                Block blockV8 = new Block("V", pos1, pos2, 8);

                int row = 0;
                int column = 0;
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        //for every value from block4, put 4 values in block8
                        for (int n = row; n < row + 2; n++) {
                            for (int m = column; m < column + 2; m++) {
                                block8.addValue(n, m, block4.getValues()[k][l]);
                                blockV8.addValue(n, m, blockV4.getValues()[k][l]);
                            }
                        }
                        column += 2;
                    }
                    column = 0;
                    row += 2;
                }
                //store the new block of 8x8
                this.blocksU[i][j] = block8;
                this.blocksV[i][j] = blockV8;

                pos2+=8;
            }
            pos1 +=8;
            pos2=0;
        }
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public int[][] getQuantizationMatrix() {
        return quantizationMatrix;
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
