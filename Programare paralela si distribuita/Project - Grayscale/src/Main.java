import javafx.concurrent.Task;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    // Compulsory
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        String fileName = "bmw.jpg";
        int nrThreads = 3;

        Mat image = Imgcodecs.imread(fileName, Imgcodecs.CV_LOAD_IMAGE_COLOR); //load image in BGR format
        if (image.empty()) {
            System.out.println("No image data!");
            System.exit(-1);
        }
        System.out.println("Started working!");

        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
        for ( int t = 0; t < Math.min(image.rows(), nrThreads); ++t) {
            int finalT = t;
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    for (int i = finalT; i < image.rows(); i += nrThreads) {
                        for (int j = 0; j < image.cols(); ++j) {
                            double[] px = image.get(i, j);
                            double gray = (px[0] + px[1] + px[2]) / 3;
                            px[0] = gray;
                            px[1] = gray;
                            px[2] = gray;
                            image.put(i, j, px);
                        }
                    }

                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        Imgcodecs.imwrite("grayscale.jpg", image);
        long endTime = System.currentTimeMillis();


        System.out.println("Elapsed time: " + (endTime - startTime));
    }
}
