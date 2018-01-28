import java.util.Random;


public abstract class IOPolynomialOperations {
    protected double[] firstPolinomial;

    protected double[] secondPolinomial;

    protected double[] resultPolinomial;

    public IOPolynomialOperations() {
        createPolinomials();
    }


    private void createPolinomials() {
        Random random = new Random();
        //  int size = 2 * random.nextInt(2500);
        int size = 3;
        firstPolinomial = new double[size];
        secondPolinomial = new double[size];
        resultPolinomial = new double[2 * size];

        for (int i = 0; i < size; i++) {
            firstPolinomial[i] = random.nextInt(100);
            secondPolinomial[i] = random.nextInt(100);
        }

         printPolinomial(firstPolinomial);
        System.out.println();
        printPolinomial(secondPolinomial);


        for (int i = 0; i < 2 * size; i++) {
            resultPolinomial[i] = 0;
        }
    }

    public void printPolinomial(double[] polinomial) {
        for (int i = 0; i < polinomial.length; i++) {
            System.out.print(polinomial[i] + "+");
        }
    }

    public abstract void run() throws InterruptedException;

}
