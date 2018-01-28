import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

public class Transition {

    /**State from which we initiate the transition.*/
    private String leftState;
    /**Symbol which is used to make the transition.*/
    private String literal;
    /**State in which we get after the transition.*/
    private String resultState;

    /**
     * Constructor for a transition.
     *
     * @param line - a transition read from file which is of the form : t(leftState,literal) = resultState
     *          String
     */
    public Transition(String line) {
        String[] sp = line.split("=");
        try {
            setElements(sp);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Function which splits the line read from file and initializes the components of a transition.
     * @param trans - line which contains a transition read from file
     *          String
     * @throws IOException if the transition is not of the form : t(leftState,literal) = resultState
     */
    private void setElements(String[] trans) throws IOException{
        String[] sp = trans[0].split(",");
        if(sp.length!=2){
            throw new IOException("The transition is not valid!");
        }
        this.leftState = sp[0].trim().split("\\(")[1];
        this.literal = sp[1].trim().substring(0,sp[1].trim().length()-1);
        if(sp[1].trim().equals("")){
            throw new IOException("The transition is missing the result state!");
        }
        this.resultState = trans[1].trim();
    }

    @Override
    public String toString() {
        return "d(" + leftState + "," + literal + ")=" + resultState;
    }

    public String getLeftState() {
        return leftState;
    }

    public void setLeftState(String leftState) {
        this.leftState = leftState;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }
}
