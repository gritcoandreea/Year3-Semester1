public class Token {

    /** The code of the token ( keyword,constant,identifier)*/
    private int code;
    /** Name of the token*/
    private String name;

    public Token(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
