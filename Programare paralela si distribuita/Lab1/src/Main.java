import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Stock stock = new Stock();
        List<Product> products = new ArrayList<>();
        Product p1 = new Product("mar",2);
        products.add(p1);
        Product p2 =new Product("apa micelara",13);
        products.add(p2);
        Product p3 =new Product("Nestea",5);
        products.add(p3);
        Product p4 =new Product("Vogue La Cigarette Verde",17);
        products.add(p4);
        Product p5 =new Product("Paine",1);
        products.add(p5);
        Product p6 =new Product("Cascaval",8);
        products.add(p6);
        Product p7 =new Product("Servetele",3);
        products.add(p7);
        Product p8 =new Product("Fixativ",11);
        products.add(p8);
        Product p9 =new Product("Biscuiti",6);
        products.add(p9);
        Product p10 =new Product("Sare",2);
        products.add(p10);
        Product p11 =new Product("Migdale",15);
        products.add(p11);
        Product p12 =new Product("Ulei",6);
        products.add(p12);
        Product p13 = new Product("Bec",10);
        products.add(p13);
        Product p14 = new Product("Pix",45);
        products.add(p14);
        Product p15 = new Product("Laptop",30);
        products.add(p15);
        Product p16 = new Product("Cafea",100);
        products.add(p16);
        Product p17 = new Product("Boxe",20);
        products.add(p17);
        Product p18 = new Product("Iaurt",5);
        products.add(p18);
        Product p19 = new Product("Geanta",60);
        products.add(p19);
        Product p20 = new Product("Geaca",150);
        products.add(p20);
        Product p21 = new Product("Mouse",30);
        products.add(p21);

        Random random = new Random();
        Random random2 = new Random();

        stock.addProduct(p1,25);
        stock.addProduct(p2,5);
        stock.addProduct(p3,10);
        stock.addProduct(p4,100);
        stock.addProduct(p5,30);
        stock.addProduct(p6,9);
        stock.addProduct(p7,200);
        stock.addProduct(p8,50);
        stock.addProduct(p9,45);
        stock.addProduct(p10,500);
        stock.addProduct(p11,20);
        stock.addProduct(p12,70);
        stock.addProduct(p13,120);
        stock.addProduct(p14,350);
        stock.addProduct(p15,120);
        stock.addProduct(p16,200);
        stock.addProduct(p17,70);
        stock.addProduct(p18,20);
        stock.addProduct(p19,100);
        stock.addProduct(p20,170);
        stock.addProduct(p21,210);


        Record record = new Record();
        int n = random.nextInt(100)+1;


        List<Bill> bills = new ArrayList<>();
        for(int i=0;i<random.nextInt(100)+1;i++){
            Bill bill10 = new Bill();
            int m = random2.nextInt(19)+1;
            for(int j=0;j<m;j++){
                bill10.addToBill(products.get(j),random.nextInt(100)+1);

            }
            System.out.println(bill10.toString());
            bills.add(bill10);
        }

        System.out.println("-------------");
//        for(int i=0;i<bills.size();i++){
//            System.out.println(bills.get(i).toString());
//        }
        System.out.println("-------------------------------------------");


        for(int i=0;i<bills.size();i++){
            (new Thread(new Sale(stock,record,bills.get(i)))).start();
        }


    }

    public static boolean checkConsistency(Record record){
        double price=0;
        for(Bill b: record.getBills()){
            price += b.getTotalPrice();
        }
        if(price == record.getTotalPrice()){
            return true;
        }
        return false;
    }
}
