package utils;

/**
 * @author Andreea Gritco on 03-Jan-18.
 */
public class Tuple {
    public final int x =0;
    public final int y=0;

    private static Tuple tuple = new Tuple( );

    public Tuple() {
    }

    /* Static 'instance' method */
    public static Tuple getInstance( ) {
        return tuple;
    }

}
