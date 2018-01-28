import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello! :)\n");

        List<String> sequence = new ArrayList<>();

        //sequence.add("+");
        sequence.add("a");
        sequence.add("a");
        sequence.add("+");
        Automaton automaton = new Automaton("cfg.txt", false);
        automaton.parse(sequence);

//        Automaton automaton = new Automaton("minilanguage.txt",true);
//        List<String> sequence = automaton.pifToCfgSequence(automaton.initCodes("codes.txt"),"pif.txt");
//        automaton.parse(sequence);


        if (automaton.getState() == State.END.getState()) {
            System.out.println("SUCCESS!!!!!!!!!");
        } else {
            System.out.println("FAILURE!!!:(");
        }

        System.out.println("Bye bye! :)");

    }
}
