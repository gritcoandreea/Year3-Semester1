package utils;

/**
 * @author Andreea Gritco on 03-Jan-18.
 */
public class AC extends Object {

    private int runLength;
    private int size;
    private int amplitude;

    public AC() {
    }

    public AC(int runLength, int size, int amplitude) {
        this.runLength = runLength;
        this.size = size;
        this.amplitude = amplitude;
    }

    public int getRunLength() {
        return runLength;
    }

    public void setRunLength(int runLength) {
        this.runLength = runLength;
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
        return "AC{" +
                "runLength=" + runLength +
                ", size=" + size +
                ", amplitude=" + amplitude +
                '}';
    }
}
