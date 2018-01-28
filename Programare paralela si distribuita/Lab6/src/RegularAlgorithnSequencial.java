public class RegularAlgorithnSequencial extends IOPolynomialOperations {

     public void run() {
         long startTime = System.currentTimeMillis();
         for (int multiplicandIndex = 0; multiplicandIndex < this.firstPolinomial.length; ++multiplicandIndex) {
             for (int multiplierIndex = 0; multiplierIndex < secondPolinomial.length; ++multiplierIndex) {
                 resultPolinomial[multiplicandIndex + multiplierIndex] +=
                         this.firstPolinomial[multiplicandIndex] * secondPolinomial[multiplierIndex];
             }
         }

         long endTime = System.currentTimeMillis();

         System.out.println("Time: " + (endTime - startTime));
    }

    public static void main(String[] args) {
        RegularAlgorithnSequencial program = new RegularAlgorithnSequencial();
        program.run();
    }



}
