/**
 * @author Andreea Gritco on 07-Jan-18.
 */
public class Tuple {
    public final String x;
    public final int y;

    public Tuple(String x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        return new Tuple(this.x, this.y);
    }

    @Override
    public String toString() {

        String s="";
        s+="(";
        s+=this.x;
        s+=" , ";
        s+=this.y;
        s+=") ";
        return s;
    }
}
