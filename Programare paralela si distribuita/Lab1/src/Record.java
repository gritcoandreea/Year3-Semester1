import java.util.ArrayList;
import java.util.List;

public class Record {
    List<Bill> bills;
    double totalPrice;

    public Record() {
        totalPrice = 0;
        bills = new ArrayList<>();
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
