import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RegularAlgorithmParallel extends IOPolynomialOperations {

    private static final int NR_THREADS = 4;

    @Override
    public void run() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(NR_THREADS);
        int tasksPerThread = firstPolinomial.length / NR_THREADS;

        int iStart = 0;
        int iStop = iStart + tasksPerThread;

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < NR_THREADS; i++){
            service.execute(new Multiplication(iStart, iStop));
            iStart = iStop;
            iStop = iStop + tasksPerThread;
        }

        if(firstPolinomial.length % NR_THREADS != 0){
            service.submit(new Multiplication(iStart, firstPolinomial.length));
        }

        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime));
    }

    public static void main(String[] args) {
        RegularAlgorithmParallel program = new RegularAlgorithmParallel();
        try {
            program.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Multiplication implements  Runnable{

        private int iStart;
        private int iStop;

        private Multiplication(int iStart, int iStop){
            this.iStart = iStart;
            this.iStop = iStop;
        }

        @Override
        public void run() {
            for(int i = iStart; i < iStop; i++){
                for(int j = iStart; j < iStop; j++){
                    resultPolinomial[i + j] = firstPolinomial[i] * firstPolinomial[j];
                }
            }
        }
    }
}
