import java.util.HashMap;
import java.util.Map;


public class Stock {

    private Map<Product,Integer> stockList;

    public Stock() {
        stockList = new HashMap<>();
    }

    public void addProduct(Product product, Integer quantity){
        stockList.put(product,quantity);
    }

    public Map<Product, Integer> getStockList() {
        return stockList;
    }

    public void setStockList(Map<Product, Integer> stockList) {
        this.stockList = stockList;
    }

    public boolean updateStock(Product product, Integer quantity, boolean addOrSubstract){
        if(findIfPresent(product)){
            if(addOrSubstract){
                stockList.put(product,stockList.get(product) +quantity);
                return true;
            }else{
                if(quantity < stockList.get(product)){
                    stockList.put(product,stockList.get(product) - quantity);
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    private boolean findIfPresent(Product product){
        for(Product p : stockList.keySet()){
            if(p == product){
                return true;
            }
        }
        return false;
    }
}
