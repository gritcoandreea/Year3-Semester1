import utils.AC;
import utils.DC;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Encoder3 {

    private Encoder2 encoder2;
    private int width;
    private int height;
    private Block[][] quantiziedY;
    private Block[][] quantiziedU;
    private Block[][] quantiziedV;

    List<Object> finalList = new ArrayList<>();
    List<Integer> nrOfCoeffPerBlock = new ArrayList<>();


    public Encoder3(Encoder2 encoder2) {

        this.encoder2 = encoder2;
        this.width = encoder2.getWidth();
        this.height = encoder2.getHeight();
        this.quantiziedY = encoder2.getQuantiziedY();
        this.quantiziedU = encoder2.getQuantiziedU();
        this.quantiziedV = encoder2.getQuantiziedV();
    }

    public void run() {

        for (int k = 0; k < this.height / 8; k++) {
            for (int l = 0; l < this.width / 8; l++) {
                //for each block

                Block blockY = this.quantiziedY[k][l];
                Block blockU = this.quantiziedU[k][l];
                Block blockV = this.quantiziedV[k][l];

                int nrCoeffY = 0;
                int nrCoeffU = 0;
                int nrCoeffV = 0;


                //------------------------Y

                double[] blockYElements = returnElements(blockY);//= new double[64];
                //add first element for Y block
                finalList.add(new DC(getSizeForAmplitude((int) blockYElements[0]), (int) blockYElements[0]));
                nrCoeffY++;

                int nrZerosY = 0;
                for (int i = 1; i < blockYElements.length; i++) {
                    if (((int) blockYElements[i]) == 0) {
                        nrZerosY++;
                    } else {
                        finalList.add(new AC(nrZerosY, getSizeForAmplitude((int) blockYElements[i]), (int) blockYElements[i]));
                        nrZerosY = 0;

                        nrCoeffY++;
                    }
                }

                if (nrZerosY != 0) {
                    finalList.add(Tuple.getInstance());
                    nrCoeffY++;
                }

                nrOfCoeffPerBlock.add(nrCoeffY);

                //-------------------------
                //-------------------------U

                double[] blockUElements = returnElements(blockU);

                //add first element for U block
                finalList.add(new DC(getSizeForAmplitude((int) blockUElements[0]), (int) blockUElements[0]));
                nrCoeffU++;

                int nrZerosU = 0;
                for (int i = 1; i < blockUElements.length; i++) {
                    if (((int) blockUElements[i]) == 0) {
                        nrZerosU++;
                    } else {
                        finalList.add(new AC(nrZerosU, getSizeForAmplitude((int) blockUElements[i]), (int) blockUElements[i]));
                        nrZerosU = 0;

                        nrCoeffU++;
                    }
                }

                if (nrZerosU != 0) {
                    finalList.add(Tuple.getInstance());
                    nrCoeffU++;
                }

                nrOfCoeffPerBlock.add(nrCoeffU);


                //-------------------------
                //-------------------------V

                double[] blockVElements = returnElements(blockV);

                //add first element for V block
                finalList.add(new DC(getSizeForAmplitude((int) blockVElements[0]), (int) blockVElements[0]));
                nrCoeffV++;

                int nrZerosV = 0;
                for (int i = 1; i < blockVElements.length; i++) {
                    if (((int) blockVElements[i]) == 0) {
                        nrZerosV++;
                    } else {
                        finalList.add(new AC(nrZerosV, getSizeForAmplitude((int) blockVElements[i]), (int) blockVElements[i]));
                        nrZerosV = 0;

                        nrCoeffV++;
                    }
                }

                if (nrZerosV != 0) {
                    finalList.add(Tuple.getInstance());
                    nrCoeffV++;
                }

                nrOfCoeffPerBlock.add(nrCoeffV);


                //-------------------------

            }
        }


    }

    public double[] returnElements(Block block) {

        double[] result = null;

        int n = block.getValues().length;
        int m = block.getValues()[0].length;
        result = new double[n * m];
        if (n == 1) {
            return block.getValues()[0];
        }
        int i = 0, j = 0;
        int ind = 0;
        result[ind] = block.getValues()[i][j];
        ind++;
        while (ind < result.length) {
            //Right 1 step, or down
            if (j + 1 < m || i + 1 < n) {
                if (j + 1 < m) j++;
                else if (i + 1 < n) i++;
                result[ind++] = block.getValues()[i][j];
            }
            //Move left-bottom:
            while (j - 1 >= 0 && i + 1 < n) {
                result[ind++] = block.getValues()[++i][--j];
            }
            //Move down, or right
            if (j + 1 < m || i + 1 < n) {
                if (i + 1 < n) i++;
                else if (j + 1 < m) j++;
                result[ind++] = block.getValues()[i][j];
            }
            //Move right-up:
            while (j + 1 < m && i - 1 >= 0) {
                result[ind++] = block.getValues()[--i][++j];
            }
        }//end while
        return result;

    }

    public int getSizeForAmplitude(int value) {
        if (value >= -1 && value <= 1) {
            return 1;
        } else if ((value >= -3 && value <= -2) || (value >= 2 && value <= 3)) {
            return 2;
        } else if ((value >= -7 && value <= -4) || (value >= 4 && value <= 7)) {
            return 3;
        } else if ((value >= -15 && value <= -8) || (value >= 8 && value <= 15)) {
            return 4;
        } else if ((value >= -31 && value <= -16) || (value >= 16 && value <= 31)) {
            return 5;
        } else if ((value >= -63 && value <= -32) || (value >= 32 && value <= 63)) {
            return 6;
        } else if ((value >= -127 && value <= -64) || (value >= 64 && value <= 127)) {
            return 7;
        } else if ((value >= -255 && value <= -128) || (value >= 128 && value <= 255)) {
            return 8;
        } else if ((value >= -511 && value <= -256) || (value >= 256 && value <= 511)) {
            return 9;
        } else if ((value >= -1023 && value <= -512) || (value >= 512 && value <= 1023)) {
            return 10;
        }

        return -5;
    }


    public List<Object> getFinalList() {
        return finalList;
    }

    public List<Integer> getNrOfCoeffPerBlock() {
        return nrOfCoeffPerBlock;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
