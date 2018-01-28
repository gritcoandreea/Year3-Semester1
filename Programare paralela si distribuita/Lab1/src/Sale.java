import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sale implements Runnable{

    private Stock stock;
    private Record record;
    private Bill bill;
    private Lock lock= new ReentrantLock();

    public Sale(Stock stock, Record record, Bill bill) {
        this.stock = stock;
        this.record = record;
        this.bill = bill;
    }

    @Override
    public void run(){
        boolean validBill=false;
        for(Map.Entry p : bill.getBillList().entrySet()){
           if( stock.getStockList().get(p.getKey()) >= (Integer) p.getValue()){
               validBill = true;
           }else{
               validBill =false;
           }
        }

        if(validBill){
            for(Map.Entry p : bill.getBillList().entrySet()){

                stock.updateStock((Product) p.getKey(),(Integer) p.getValue(),false);

            }
            record.getBills().add(bill);
            lock.lock();
            record.setTotalPrice(record.getTotalPrice() + bill.getTotalPrice());
            lock.unlock();
            System.out.println("YAAAY");

        }
        else{
            System.out.println("Couldn't process this bill : ");
            System.out.println(bill.toString());
        }

        System.out.println("Consistency = " +Main.checkConsistency(record) );
    }


    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
