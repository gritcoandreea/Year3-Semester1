public class Pixel {

    private double y;
    private double u;
    private double v;

    private int r;
    private int g;
    private int b;

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    /**
     * If flag == true, it is a YUV pixel, else it is a RGB pixel.
     * @param y
     * @param u
     * @param v
     * @param flag
     */
    public Pixel(double y, double u, double v, boolean flag) {
        if (flag) {
            this.y = y;
            this.u = u;
            this.v = v;
        } else {
            this.r = (int) y;
            this.g = (int) u;
            this.b = (int) v;

        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
