public class Image {

    private String imageFormat;
    private int imageWidth;
    private int imageHeight;
    private int maximumColorValue;
    private Pixel[][] image;



    public Image(String imageFormat, int imageWidth, int imageHeight, int maximumColorValue, Pixel[][] image) {
        this.imageFormat = imageFormat;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.maximumColorValue = maximumColorValue;
        this.image = image;
    }

    public Image(String imageFormat, int imageWidth, int imageHeight, int maximumColorValue) {
        this.imageFormat = imageFormat;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.maximumColorValue = maximumColorValue;
        this.image = new Pixel[imageHeight][imageWidth];
    }

    public void addPixel(int i, int j, Pixel pixel){
        this.image[i][j] = pixel;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getMaximumColorValue() {
        return maximumColorValue;
    }

    public void setMaximumColorValue(int maximumColorValue) {
        this.maximumColorValue = maximumColorValue;
    }

    public Pixel[][] getImage() {
        return image;
    }

    public void setImage(Pixel[][] image) {
        this.image = image;
    }
}
