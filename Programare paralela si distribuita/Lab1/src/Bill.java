import java.util.HashMap;
import java.util.Map;

public class Bill {

    private Map<Product,Integer> billList;
    private double totalPrice;

    public Bill(Map<Product, Integer> billList, Stock stock) {
        totalPrice=0;
        this.billList = billList;
        computePrice();
    }

    public Bill() {
        billList = new HashMap<>();
        totalPrice = 0;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billList=" + billList +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public void addToBill(Product product, Integer quantity){
        //if(stock.updateStock(product,quantity,false)) {
            if (findIfPresent(product)) {
                billList.put(product, billList.get(product) + quantity);
            } else {
                billList.put(product, quantity);
            }
            totalPrice += product.getPrice() *quantity;
       //     return true;
       // }
      //  return false;
    }

    private boolean findIfPresent(Product product){
        for(Product p : billList.keySet()){
            if(p == product){
                return true;
            }
        }
        return false;
    }

    public void computePrice(){
        for(Product p : billList.keySet()){
            totalPrice +=  p.getPrice() * billList.get(p);
        }
    }

    public Map<Product, Integer> getBillList() {
        return billList;
    }

    public void setBillList(Map<Product, Integer> billList) {
        this.billList = billList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}
