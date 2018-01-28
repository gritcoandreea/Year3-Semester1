package utils;

/**
 * @author Andreea Gritco on 03-Jan-18.
 */
public class DC extends Object{

    private int size;
    private int amplitude;

    public DC(int size, int amplitude) {
        this.size = size;
        this.amplitude = amplitude;
    }

    public DC() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public String toString() {
        return "DC{" +
                "size=" + size +
                ", amplitude=" + amplitude +
                '}';
    }
}
