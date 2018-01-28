import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Double;

public class Encoder {

    private static String imagePath;
    private Image image;
    private double[][] matrixY;
    private Block[][] blocksY;
    private double[][] matrixU;
    private Block[][] blocksU;

    public Block[][] getBlocksY() {
        return blocksY;
    }

    public Block[][] getBlocksU() {
        return blocksU;
    }

    public Block[][] getBlocksV() {
        return blocksV;
    }

    private double[][] matrixV;
    private Block[][] blocksV;

    public Encoder(final String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        return image;
    }

    public void run() {
        convertImageToYUV();
        populateMatrices();
        divideIntoBlocksY();
        divideIntoBlocksUV();

        blocksV[0][0].printValues();

//        for(int i=0;i<image.getImageWidth()/8;i++){
//            for(int j=0;j<image.getImageHeight()/8;j++){
//                 blocksY[i][j].printValues();
//                 System.out.println("---------------------------------------------------------");
//            }
//        }

//        for(int i=0;i<image.getImageWidth()/8;i++){
//            for(int j=0;j<image.getImageHeight()/8;j++){
//                blocksU[i][j].printValues();
//                System.out.println("---------------------------------------------------------");
//            }
//        }

//        for(int i=0;i<image.getImageWidth()/8;i++){
//            for(int j=0;j<image.getImageHeight()/8;j++){
//
//                blocksV[i][j].printValues();
//                System.out.println("---------------------------------------------------------");
//            }
//        }
    }


    private void divideIntoBlocksY() {

        blocksY = new Block[image.getImageHeight()/8][image.getImageWidth()/8];

        int row = 0;
        int col = 0;
        for (int i = 0; i < image.getImageHeight(); i = i + 8) {
            for (int j = 0; j < image.getImageWidth(); j = j + 8) {

                Block block = new Block("Y", i, j, 8);
                int line = 0;
                int column = 0;
                for (int k = i; k < i + 8; k++) {
                    for (int l = j; l < j + 8; l++) {
                        block.addValue(line, column, matrixY[k][l]);
                        column++;
                    }
                    line++;
                    column = 0;
                }
                blocksY[row][col] = block;
                col++;

            }
            row++;
            col=0;
        }

    }

    private void divideIntoBlocksUV() {

        blocksU = new Block[image.getImageHeight()/8][image.getImageWidth()/8];
        blocksV = new Block[image.getImageHeight()/8][image.getImageWidth()/8];

        int row=0;
        int col=0;
        for (int i = 0; i < image.getImageHeight(); i = i + 8) {
            for (int j = 0; j < image.getImageWidth(); j = j + 8) {

                Block block = new Block("U", i, j, 4);
                Block block2 = new Block("V",i,j,4);

                int line = 0;
                int column = 0;
                for (int k = i; k < i + 8; k = k + 2) {
                    for (int l = j; l < j + 8; l = l + 2) {

                        double average = 0;
                        double average2 = 0;
                        for(int n=k;n<k+2;n++){
                            for(int m=l;m<l+2;m++){
                                average+=matrixU[n][m];
                                average2+=matrixV[n][m];
                            }
                        }
                        average=average/4.0;
                        average2 =average2/4.0;
                        block.addValue(line,column,average);
                        block2.addValue(line,column,average2);
                        column++;
                    }
                    line++;
                    column=0;
                }

                blocksU[row][col] = block;
                blocksV[row][col] = block2;
                col++;
            }
            row++;
            col=0;
        }
    }

    private void populateMatrices() {
        matrixY = new double[image.getImageHeight()][image.getImageWidth()];
        matrixU = new double[image.getImageHeight()][image.getImageWidth()];
        matrixV = new double[image.getImageHeight()][image.getImageWidth()];

        for (int i = 0; i < image.getImageHeight(); i++) {
            for (int j = 0; j < image.getImageWidth(); j++) {
                matrixY[i][j] = image.getImage()[i][j].getY();
                matrixU[i][j] = image.getImage()[i][j].getU();
                matrixV[i][j] = image.getImage()[i][j].getV();
            }
        }
    }

    public void convertImageToYUV() {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(imagePath));
            String line1, line2, line3;
            String imageFormat = br.readLine().trim();
            br.readLine();
            String[] widthHeight = br.readLine().split(" ");
            int maxValue = Integer.parseInt(br.readLine());

            image = new Image(imageFormat, Integer.parseInt(widthHeight[0].trim()), Integer.parseInt(widthHeight[1].trim()), maxValue);

            for (int i = 0; i < image.getImageHeight(); i++) {
                for (int j = 0; j < image.getImageWidth(); j++) {
                    if ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null && (line3 = br.readLine()) != null) {
                        int r = Integer.parseInt(line1.trim());
                        int g = Integer.parseInt(line2.trim());
                        int b = Integer.parseInt(line3.trim());


                        double y = 0.299 * r + 0.587 * g + 0.114 * b;
                        double u = 128 - 0.169 * r - 0.331 * g + 0.499 * b;
                        double v = 128 + 0.499 * r - 0.418 * g - 0.0813 * b;

                        Pixel pixel = new Pixel(y, u, v,true);
                        image.addPixel(i, j, pixel);
                    }
                }
            }


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
}
