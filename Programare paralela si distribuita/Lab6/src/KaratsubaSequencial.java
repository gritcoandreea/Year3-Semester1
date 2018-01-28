
public class KaratsubaSequencial extends IOPolynomialOperations {

    private double[][] D;

        private void initialize(int d) {
        D = new double[d+1][d+1];
        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                D[i][j] = 0;
            }

        }
    }

    public static void main(String[] args) {
        KaratsubaSequencial program = new KaratsubaSequencial();
        program.run();
    }

    @Override
    public void run() {
        int d = firstPolinomial.length - 1;
        initialize(d);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i <=d; i++) {
            D[i][i] = firstPolinomial[i] * secondPolinomial[i];
        }
        for (int i1 = 0; i1 < d; i1++) {
            for (int i2 = i1 + 1; i2 <= d; i2++) {
                D[i1][i2] = ((firstPolinomial[i1] + firstPolinomial[i2]) * (secondPolinomial[i1] + secondPolinomial[i2]));
            }
        }
        for (int i1 = 0; i1 < d; i1++) {
            for (int i2 = i1 + 1; i2 <= d; i2++) {
                resultPolinomial[i1 + i2] += (D[i1][i2] - D[i1][i1] - D[i2][i2]);
            }
        }
        for (int i = 2; i < 2 * d; i += 2) {
            resultPolinomial[i] += (D[i / 2][i / 2]);
        }
        resultPolinomial[0] = D[0][0];
        resultPolinomial[2 * d] = D[d][d];

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime));
        printPolinomial(resultPolinomial);
    }
}
