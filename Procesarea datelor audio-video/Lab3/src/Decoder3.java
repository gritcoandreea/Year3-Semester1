import utils.AC;
import utils.DC;

import java.util.ArrayList;
import java.util.List;

public class Decoder3 {

    private int height;
    private int width;

    private Block[][] quantiziedY;
    private Block[][] quantiziedU;
    private Block[][] quantiziedV;


    //Encoded blocks from encoder3.
    private List<Object> encoder3List;
    //List of nr of coefficients in encoder3List for each block;
    private List<Integer> encoder3ListNrs;

    public Decoder3(int height, int width, List<Object> encoder3List, List<Integer> encoder3ListNrs) {
        this.height = height;
        this.width = width;
        this.encoder3List = encoder3List;
        this.encoder3ListNrs = encoder3ListNrs;

        this.quantiziedY = new Block[this.height / 8][this.width / 8];
        this.quantiziedU = new Block[this.height / 8][this.width / 8];
        this.quantiziedV = new Block[this.height / 8][this.width / 8];
    }

    public void run() {

        int arrayCounter = 0;
        int lengthArrayCounter = 0;

        int ii = 0;
        int jj = 0;
        for (int i = 0; i < this.height / 8; i++) {
            for (int j = 0; j < this.width / 8; j++) {
                //create blocks
                Block quantBlockY = new Block("Y", i*8, j*8, 8);
                Block quantBlockU = new Block("U", i*8, j*8, 8);
                Block quantBlockV = new Block("V", i*8, j*8, 8);
                //position in Image (i,j)
                ii = ii + 8;
                jj = jj + 8;

                //for block Y
                List<Integer> listY = new ArrayList<>();
                for (int k = arrayCounter; k < arrayCounter + encoder3ListNrs.get(lengthArrayCounter); k++) {

                    if (encoder3List.get(k) instanceof DC) {
                        DC dc = (DC) encoder3List.get(k);
                        listY.add(dc.getAmplitude());
                    } else if (encoder3List.get(k) instanceof AC) {
                        AC ac = (AC) encoder3List.get(k);
                        if (ac.getRunLength() != 0) {
                            for (int l = 0; l < ac.getRunLength(); l++) {
                                listY.add(0);
                            }
                            listY.add(ac.getAmplitude());

                        } else {
                            listY.add(ac.getAmplitude());
                        }
                    } else {
                        //end of block
                        while (listY.size() < 64) {
                            listY.add(0);
                        }
                    }


                }
                if (listY.size() < 64) {
                    while (listY.size() < 64) {
                        listY.add(0);
                    }
                }
                addZigZag(listY, quantBlockY);
                arrayCounter = arrayCounter + encoder3ListNrs.get(lengthArrayCounter);
                lengthArrayCounter++;

                //for block U
                List<Integer> listU = new ArrayList<>();
                for (int k = arrayCounter; k < arrayCounter + encoder3ListNrs.get(lengthArrayCounter); k++) {

                    if (encoder3List.get(k) instanceof DC) {
                        DC dc = (DC) encoder3List.get(k);
                        listU.add(dc.getAmplitude());
                    } else if (encoder3List.get(k) instanceof AC) {
                        AC ac = (AC) encoder3List.get(k);
                        if (ac.getRunLength() != 0) {
                            for (int l = 0; l < ac.getRunLength(); l++) {
                                listU.add(0);
                            }
                            listU.add(ac.getAmplitude());

                        } else {
                            listU.add(ac.getAmplitude());
                        }
                    } else {
                        //end of block
                        while (listU.size() < 64) {
                            listU.add(0);
                        }
                    }


                }
                if (listU.size() < 64) {
                    while (listU.size() < 64) {
                        listU.add(0);
                    }
                }

                addZigZag(listU, quantBlockU);
                arrayCounter = arrayCounter + encoder3ListNrs.get(lengthArrayCounter);
                lengthArrayCounter++;

                //for block V
                List<Integer> listV = new ArrayList<>();
                for (int k = arrayCounter; k < arrayCounter + encoder3ListNrs.get(lengthArrayCounter); k++) {

                    if (encoder3List.get(k) instanceof DC) {
                        DC dc = (DC) encoder3List.get(k);
                        listV.add(dc.getAmplitude());
                    } else if (encoder3List.get(k) instanceof AC) {
                        AC ac = (AC) encoder3List.get(k);
                        if (ac.getRunLength() != 0) {
                            for (int l = 0; l < ac.getRunLength(); l++) {
                                listV.add(0);
                            }
                            listV.add(ac.getAmplitude());

                        } else {
                            listV.add(ac.getAmplitude());
                        }
                    } else {
                        //end of block
                        while (listV.size() < 64) {
                            listV.add(0);
                        }
                    }


                }

                if (listV.size() < 64) {
                    while (listV.size() < 64) {
                        listV.add(0);
                    }
                }
                addZigZag(listV, quantBlockV);
                arrayCounter = arrayCounter + encoder3ListNrs.get(lengthArrayCounter);
                lengthArrayCounter++;


                this.quantiziedY[i][j] = quantBlockY;
                this.quantiziedU[i][j] = quantBlockU;
                this.quantiziedV[i][j] = quantBlockV;

            }
        }


    }

    private void addZigZag(List<Integer> list, Block quantBlock) {
        int i = 1;
        int j = 1;
        for (int element = 0; element < list.size(); element++) {
            quantBlock.getValues()[i - 1][j - 1] = list.get(element);
            if ((i + j) % 2 == 0) {
                // Even stripes
                if (j < 8)
                    j++;
                else
                    i += 2;
                if (i > 1)
                    i--;
            } else {
                // Odd stripes
                if (i < 8)
                    i++;
                else
                    j += 2;
                if (j > 1)
                    j--;
            }
        }
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
}
