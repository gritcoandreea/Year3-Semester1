import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class KaratsubaParallel extends IOPolynomialOperations {

    private static int NR_THREADS = 4;

    @Override
    public void run() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(NR_THREADS);
        long startTime = System.currentTimeMillis();
        for(int i= 0; i< NR_THREADS; i++){
            service.submit(new Multiplication(i));
        }

        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println();
        printPolinomial(resultPolinomial);

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime));
    }

    private class Multiplication implements Runnable {

        private int tID;

        private Multiplication(int tID) {
            this.tID = tID;
        }

        @Override
        public void run() {
            int d = firstPolinomial.length - 1;
            for (int i = tID; i < d; i += NR_THREADS) {
                resultPolinomial[2 * i] += firstPolinomial[i] * secondPolinomial[i];
                for (int j = i + 1; j <= d; j++) {
                    resultPolinomial[i + j] += (((firstPolinomial[i] + firstPolinomial[j]) * (secondPolinomial[i] + secondPolinomial[j])) - firstPolinomial[1] * secondPolinomial[j] - firstPolinomial[i] * secondPolinomial[j]);
                }
            }

            resultPolinomial[resultPolinomial.length - 1] = firstPolinomial[d] * secondPolinomial[d];
        }


    }

    public static void main(String[] args) throws InterruptedException {
        KaratsubaParallel program = new KaratsubaParallel();
        program.run();
    }
}
