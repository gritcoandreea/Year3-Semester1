public class Main {

    public static void main(String[] args) {
        System.out.println("Hello :)!");
        Encoder encoder = new Encoder("nt-P3.ppm");
        encoder.run();
        Decoder decoder = new Decoder(encoder.getBlocksY(), encoder.getBlocksU(), encoder.getBlocksV(), encoder.getImage().getImageFormat(), encoder.getImage().getMaximumColorValue(),encoder.getImage().getImageWidth(),encoder.getImage().getImageHeight());
        decoder.run();
    }
}
