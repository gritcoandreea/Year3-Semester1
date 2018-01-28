import java.io.*;

public class Decoder {

    private Block[][] blocksY;
    private Block[][] blocksU;
    private Block[][] blocksV;

    private Image image;
    private String imageFormat;
    private int maxColorValue;
    private int width;
    private int height;

    private double[][] matrixY;
    private double[][] matrixU;
    private double[][] matrixV;


    public Decoder(Block[][] blocksY, Block[][] blocksU, Block[][] blocksV, String imageFormat, int maxColorValue, int width, int height) {
        this.blocksY = blocksY;
        this.blocksU = blocksU;
        this.blocksV = blocksV;
        this.imageFormat = imageFormat;
        this.maxColorValue = maxColorValue;
        matrixY = new double[height][width];
        matrixU = new double[height][width];
        matrixV = new double[height][width];
        this.width = width;
        this.height = height;
        this.image = new Image(this.imageFormat, this.width, this.height, this.maxColorValue);
    }

    public void run() {
        computeMatrixY();
        computeMatrixUV();
        convertToRGB();
        createImage();
    }

    private void createImage() {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("output-P3.ppm", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(this.imageFormat);
        writer.println("#Final picture");
        writer.println(this.width + " " + this.height);
        writer.println(this.maxColorValue);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                writer.println(image.getImage()[i][j].getR());
                writer.println(image.getImage()[i][j].getG());
                writer.println(image.getImage()[i][j].getB());
//                    writer.println(i + " " + j + " " + i*j);
//                    writer.println(i + " " + j + " " + i*j);
//                    writer.println(i + " " + j + " " + i*j);
            }
        }
        writer.close();
    }

    /**
     * R = Y + 1.140V
     * G = Y - 0.395U - 0.581V
     * B = Y + 2.032U
     */
    private void convertToRGB() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double r = matrixY[i][j] + (matrixV[i][j] - 128) * 1.370705;
                double g = matrixY[i][j] - 0.337633 * (matrixU[i][j] - 128) - 0.698001 * (matrixV[i][j] - 128);
                double b = matrixY[i][j] + (matrixU[i][j] - 128) * 1.732446;

                Pixel pixel = new Pixel((int) r, (int) g, (int) b, false);
                this.image.addPixel(i, j, pixel);

            }
        }


    }

    private void computeMatrixUV() {
        //for every block
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width / 8; j++) {
                //parse block

                int k = 0;
                int l = 0;
                for (int n = blocksU[i][j].getPositionInImage1(); n < blocksU[i][j].getPositionInImage1() + 8; n = n + 2) {
                    for (int m = blocksU[i][j].getPositionInImage2(); m < blocksU[i][j].getPositionInImage2() + 8; m = m + 2) {

                        for (int o = n; o < n + 2; o++) {
                            for (int p = m; p < m + 2; p++) {
                                matrixU[o][p] = blocksU[i][j].getValues()[k][l];
                                matrixV[o][p] = blocksV[i][j].getValues()[k][l];
                            }
                        }
                        l++;
                    }
                    k++;
                    l = 0;
                }

            }
        }
    }

    private void computeMatrixY() {
        //for every block
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width / 8; j++) {

                //parse block
                int k = 0;
                int l = 0;
                for (int n = blocksY[i][j].getPositionInImage1(); n < blocksY[i][j].getPositionInImage1() + 8; n++) {
                    for (int m = blocksY[i][j].getPositionInImage2(); m < blocksY[i][j].getPositionInImage2() + 8; m++) {
                        matrixY[n][m] = blocksY[i][j].getValues()[k][l];
                        l++;
                    }
                    k++;
                    l = 0;
                }
            }
        }
    }
}
