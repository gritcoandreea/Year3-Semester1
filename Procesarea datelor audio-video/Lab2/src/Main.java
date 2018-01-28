public class Main {

    public static void main(String[] args) {
        System.out.println("Hello :)!");

        Encoder encoder = new Encoder("nt-P3.ppm");
        encoder.run();

        Encoder2 encoder2 = new Encoder2(encoder);
        encoder2.run();

        Decoder2 decoder2 = new Decoder2(encoder2.getQuantiziedY(), encoder2.getQuantiziedU(),
                encoder2.getQuantiziedV(), encoder2.getQuantizationMatrix(), encoder2.getWidth(),
                encoder2.getHeight());
        decoder2.run();



//        Decoder decoder3 = new Decoder(decoder2.getDctY(), decoder2.getDctU(), decoder2.getDctV(),
//                encoder.getImage().getImageFormat(), encoder.getImage().getMaximumColorValue(),
//                encoder.getImage().getImageWidth(), encoder.getImage().getImageHeight());
//        decoder3

        Decoder decoder = new Decoder(decoder2.getBlocksY(), decoder2.getBlocksU(), decoder2.getBlocksV(),
                encoder.getImage().getImageFormat(), encoder.getImage().getMaximumColorValue(),
                encoder.getImage().getImageWidth(), encoder.getImage().getImageHeight());


        decoder.run();
    }
}
