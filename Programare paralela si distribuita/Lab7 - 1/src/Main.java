import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private int[] sequence;
    private int[] result;
    private Lock lock;
    private Condition condition;



    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.generateSourceSequence();
        main.run();

    }

    private void run() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(this.sequence.length);
        lock = new ReentrantLock();
        condition = lock.newCondition();

        long start = System.currentTimeMillis();

        result[0] = sequence[0];

        for (int i = 1; i < this.sequence.length; i++) {
            int finalI = i;
            lock.lock();
            while(result[i-1] == 0){
                condition.await();
            }

            executor.submit(() -> {
                lock.lock();
                result[finalI] = result[finalI - 1] + sequence[finalI];
                condition.signal();
                lock.unlock();
            });
            lock.unlock();
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        long stop = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (stop - start));
        System.out.println();

        printSequence(sequence);
        printSequence(result);

    }

    private void printSequence(int[] seq){
        for(int i=0 ; i<seq.length;i++){
            System.out.print(seq[i] + "  ");
        }
        System.out.println();
    }

    private void generateSourceSequence() {
        Random random = new Random();
        int size = random.nextInt(100);

        sequence = new int[size];
        result = new int[size];

        for (int i = 0; i < size; i++) {
            sequence[i] = random.nextInt(100);
        }

    }
}
