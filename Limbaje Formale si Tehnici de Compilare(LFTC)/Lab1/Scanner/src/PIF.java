public class PIF {

    /** -1, 0, 1 - -1 - keyword , 0 - constant, 1 - identifier*/
    private int type;
    /** The corresponding code from the codifiers list.*/
    private int code;

    public PIF(int type, int code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
