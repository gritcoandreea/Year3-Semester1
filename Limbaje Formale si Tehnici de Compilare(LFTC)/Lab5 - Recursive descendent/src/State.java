/**
 * @author Andreea Gritco on 07-Jan-18.
 */
public enum State {
    NORMAL(0),
    BACK(1),
    END(2),
    ERROR(3);


    State(int state) {
        this.state = state;
    }

    private int state;

    public int getState() {
        return state;
    }

}
