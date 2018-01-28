public class Main {

    public static void main(String[] args) {
        System.out.println("Hello :)!");

        Encoder encoder = new Encoder("nt-P3.ppm");
        encoder.run();

        Encoder2 encoder2 = new Encoder2(encoder);
        encoder2.run();

        Encoder3 encoder3 = new Encoder3(encoder2);
        encoder3.run();

        Decoder3 decoder3 = new Decoder3(encoder3.getHeight(), encoder3.getWidth(), encoder3.getFinalList(), encoder3.getNrOfCoeffPerBlock());
        decoder3.run();


        Decoder2 decoder2 = new Decoder2(decoder3.getQuantiziedY(), decoder3.getQuantiziedU(),
                decoder3.getQuantiziedV(), encoder2.getQuantizationMatrix(), encoder2.getWidth(),
                encoder2.getHeight());
        decoder2.run();


        Decoder decoder = new Decoder(decoder2.getBlocksY(), decoder2.getBlocksU(), decoder2.getBlocksV(),
                encoder.getImage().getImageFormat(), encoder.getImage().getMaximumColorValue(),
                encoder.getImage().getImageWidth(), encoder.getImage().getImageHeight());
        decoder.run();
    }
}
