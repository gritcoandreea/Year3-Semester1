import java.io.Console;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int size = 10;
        BigNumber[] numbers = new BigNumber[size];
        for (int i = 0; i < size; i++)
            numbers[i] = BigNumber.randomBigNumber();
        BigNumber.AddParallel(numbers);
    }
}
