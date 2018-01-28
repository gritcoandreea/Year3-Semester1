import javafx.concurrent.Task;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by gobi on 12/7/2017.
 */
public class BigNumber {

    private static Random random = new Random();

    private int[] digits;

    private BigNumber(int[] digits) {
        reverse(digits);
        this.digits = digits;
        System.out.println(this + "+");
    }


    public static BigNumber randomBigNumber() {
        final int minSize = 100;
        final int maxSize = 1000;

        int size = random.nextInt(maxSize - minSize) + minSize;
        int[] ints = new int[size];

        ints[0] = random.nextInt(9) + 1;
        for (int i = 1; i < size; i++) {
            ints[i] = random.nextInt(10);
        }

//        int size = 8;
//        int[] ints = {1,2,3,4,5,6,7,8};

        return new BigNumber(ints);
    }

    private static void reverse(int[] array){
        for(int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    private static BigNumberQueue AddParallel(BigNumberQueue previousQueue, BigNumber currentBigNumber, ExecutorService service) throws InterruptedException {
        BigNumberQueue result = new BigNumberQueue();


        service.execute(new Runnable() {
            @Override
            public void run() {
                int transport = 0;

                int i;
                for (i = 0; i < currentBigNumber.digits.length; ++i) {
                    //Waiting for a digit
                    while (!previousQueue.isDone && previousQueue.size() == 0)
                    {

                    }

                    //We finished with the queue, we add the bigNumber.
                    if (previousQueue.isDone && previousQueue.size() == 0)
                        synchronized (result) {
                            result.add(new Tuple<>(i, (transport + currentBigNumber.digits[i]) % 10));
                            transport = (transport + currentBigNumber.digits[i]) / 10;
                        }

                    //If we have a digit from previous computation
                    if (previousQueue.size() > 0)
                        synchronized (previousQueue) {
                            Tuple<Integer, Integer> digit = null;
                            try {
                                digit = previousQueue.take();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (digit.x != i)
                                throw new RuntimeException("Out of order!");
                            synchronized (result) {
                                result.add(new Tuple<>(i, (transport + digit.y + currentBigNumber.digits[i]) % 10));
                                transport = (transport + digit.y + currentBigNumber.digits[i]) / 10;
                            }
                        }
                }

                //Finished with bigNumber, we add what is in the queue
                while (!(previousQueue.isDone && previousQueue.size() == 0))
                    if (previousQueue.size() > 0)
                        synchronized (previousQueue) {
                            Tuple<Integer, Integer> digit = null;
                            try {
                                digit = previousQueue.take();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            synchronized (result) {
                                try {
                                    result.put(new Tuple<>(i, (transport + digit.y) % 10));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                transport = (transport + digit.y) / 10;
                                i++;
                            }
                        }

                //If we have transport
                if (transport > 0)
                    synchronized (result) {
                        try {
                            result.put(new Tuple<>(i, transport));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                //The computation is finished.
                result.isDone = true;

            }
        });

        return result;
    }

    public static BigNumber AddParallel(BigNumber[] numbers) throws InterruptedException {
        if (numbers.length <= 1) {
            throw new RuntimeException("Array too small!");
        }

        //first big number
        BigNumber first = numbers[0];
        // nr threads :  n -1
        ExecutorService service = Executors.newFixedThreadPool(numbers.length - 1);
        //first queue
        BigNumberQueue firstQueue = new BigNumberQueue();
        //for every digit from first big number add them in the first queue
        for (int i = 0; i < first.digits.length; ++i)
            firstQueue.put(new Tuple<>(i, first.digits[i]));
        firstQueue.isDone = true;


        //create n queues
        BigNumberQueue[] queues = new BigNumberQueue[numbers.length];
        //first queue
        queues[0] = firstQueue;


        long start = System.currentTimeMillis();


        //for every number we pass the queue for the last element before it and the big number to the thread
        for (int i = 1; i < numbers.length; ++i)
            queues[i] = AddParallel(queues[i - 1], numbers[i], service);


        service.awaitTermination(5, TimeUnit.SECONDS);
        service.shutdown();

        long stop = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (stop - start));
        System.out.println();

        List<Integer> result = new ArrayList<>();
        while (queues[numbers.length - 1].size() > 0) {
            result.add(queues[numbers.length - 1].take().y);
        }

        int[] resultedArray = Arrays.stream(result.toArray()).mapToInt(o -> (int) o).toArray();
        reverse(resultedArray);
        return new BigNumber(resultedArray);
    }


    @Override
    public String toString() {
        int[] ints = digits.clone();
        reverse(ints);
        StringBuilder builder = new StringBuilder();

        for (int anInt : ints) {
            builder.append(anInt);
        }

        return builder.toString();
    }
}
