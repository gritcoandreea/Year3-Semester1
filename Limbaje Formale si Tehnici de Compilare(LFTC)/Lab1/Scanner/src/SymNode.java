

public class SymNode {
    /**Type of data (ex: float)*/
    private String type;
    /** Constant or identifier */
    private String identifier;
    /** The value of the constant/identifier (ex: 0, null, i+1)*/
    private String value;
    private int nr;
    /** Left, Right children*/
    private SymNode left, right;

    public SymNode(String type, String identifier, String value, int nr) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
        this.nr = nr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public SymNode getLeft() {
        return left;
    }

    public void setLeft(SymNode left) {
        this.left = left;
    }

    public SymNode getRight() {
        return right;
    }

    public void setRight(SymNode right) {
        this.right = right;
    }
}
