import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FiniteAutomata {

    /**File from where we read the finite automata.*/
    private String fileName;
    /**Set of states.*/
    private Set<String> states;
    /**Set of symbols (characters) which define the alphabet.*/
    private Set<String> alphabet;
    /**Initial state of FA.*/
    private String initialState;
    /**Set of final states of FA.*/
    private Set<String> finalStates;
    /**List of transitions of FA.*/
    private Set<Transition> transitions;

    /**
     * Constructor for FA.
     * Here we perfom the following:
     * -Read the FA from the file, and display errors if the states,alphabet,initial state, final states are empty or
     * if a transition is invalid.
     * -Check if the initial state, final states and the components of a transition are in the list of states and alphabet.
     * -Check if the FA is deterministic.
     * @param fileName
     */
    public FiniteAutomata(String fileName) {
        this.fileName = fileName;
        states = new HashSet<>();
        alphabet = new HashSet<>();
        initialState = "";
        finalStates = new HashSet<>();
        transitions = new HashSet<>();
        try {
            readFromFile();
            validateFiniteAutomata();
           if(! checkIfDeterministic()){
               throw new IOException("The FA is not DETERMINISTIC!");
           }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checks if a FA is deterministic.
     * @return true if it is deterministic,
     *          false otherwise
     */
    public boolean checkIfDeterministic(){
        String state = this.initialState;
        Map<String,List<String>> literals = new HashMap<>();

        for (Transition t : transitions) {
            if(!literals.containsKey(t.getLeftState())){
                literals.put(t.getLeftState(),new ArrayList<>());
            }
            if (state.equals(t.getLeftState())) {
                if (literals.get(state).contains(t.getLiteral()) ) {
                    return false;
                   // throw new IOException("The FA is not DETERMINISTIC!");
                }
                else{
                    literals.get(state).add(t.getLiteral());
                }
            } else {
                state = t.getLeftState();
                literals.get(state).add(t.getLiteral());
            }
        }
        return true;
    }


    /**
     * Validates a sequence , if it corresponds to the FA .
     * @param sequence - the sequence which we are checking
     * @return
     *      true if the sequence is valid,
     *      false otherwise
     */
    public boolean validateSequence(String sequence) {
        String state = this.initialState;
        for (Character c : sequence.toCharArray()) {
            boolean ok=false;
            for (Transition t : transitions) {
                if ((t.getLeftState().equals(state)) && c.toString().equals(t.getLiteral())) {
                    state = t.getResultState();
                    ok=true;
                    break;
                }
            }
            if(!ok){
                return false;
            }

        }

        return finalStates.contains(state);
    }


    /**
     * Given a sequence, it finds the longest accepted prefix based on the FA.
     * @param seq - the sequence which we are checking
     * @return
     *      the longest prefix, if it is found one
     *      empty string if there is no longest accepted prefix
     */
    public String findLongestAcceptedPrefix(String seq) {
        String state = this.initialState;
        String current = "";
        String last_accepted = null;
        if (this.finalStates.contains(state)) {
            last_accepted = "";
        }
        for (Character c : seq.toCharArray()) {
            if (getTransition(state, c.toString()) != null) {
                current += c.toString();
                state = getTransition(state, c.toString()).getResultState();
                if (this.finalStates.contains(state)) {
                    last_accepted = current;
                }
            } else {
                return last_accepted;
            }
        }
        return last_accepted;
    }

    /**
     * Returns a transition based on a state  and a literal.
     * @param left - leftState of a transition
     * @param literal - literal of a transition
     * @return
     *          Transition if it is found one based on given parameters,
     *          null otheriwse
     */
    public Transition getTransition(String left, String literal) {
        for (Transition t : this.transitions) {
            if (t.getLeftState().equals(left) && t.getLiteral().equals(literal)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Validates the FA.
     * @throws IOException if :- the final state is not in the list of states
     *                         - if one of the final states is not in the list of states
     *                         - if one of the states in a transition is not in the list of states
     *                         - if the literal from  one transition is not in the alphabet
     */
    private void validateFiniteAutomata() throws IOException {
        if (!states.contains(initialState)) {
            throw new IOException("The initial state is not in the set of states!");
        }
        for (String state : finalStates) {
            if (!states.contains(state)) {
                throw new IOException("The finite state " + state + " is not in the list of states!");
            }
        }

        for (Transition t : transitions) {
            if (!states.contains(t.getLeftState())) {
                throw new IOException("The state: " + t.getLeftState() + " from the transition : " + t.toString() + " is not in the list of states!");
            } else if (!states.contains(t.getResultState())) {
                throw new IOException("The state: " + t.getResultState() + " from the transition: " + t.toString() + " is not in the list of states!");
            } else if (!alphabet.contains(t.getLiteral())) {
                throw new IOException("The literal " + t.getLiteral() + " from the transition: " + t.toString() + " is not in the alphabet!");
            }
        }
    }

    /**
     * Reads the FA from file based on the rules established in the description of the FA file.
     * @throws IOException if: - The list of states is empty.
     *                         - The alphabet is empty.
     *                         - The initial state is empty.
     *                         -The final states list is empty.
     *                         -A transition is not valid.
     */
    private void readFromFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] line2 = line.split(":");
                switch (line2[0].trim()) {
                    case "states":
                        String[] states = line2[1].split(",");
                        if (states.length < 2) {
                            throw new IOException("The states list is empty!");
                        }
                        for (int i = 0; i < states.length; i++) {
                            this.states.add(states[i].trim());
                        }
                        break;

                    case "alphabet":
                        if (line2[0].length() < 1) {
                            throw new IOException("The alphabet is empty!");
                        }
//                        final Pattern pattern = Pattern.compile("^^[a-z0-9]*$");
//                        if (!pattern.matcher(line2[1].trim()).matches()) {
//                            throw new IOException("Invalid Alphabet");
//                        }
                        for (int i = 0; i < line2[1].trim().length(); i++) {
                            this.alphabet.add(Character.toString(line2[1].charAt(i)).trim());
                        }
                        break;
                    case "initial_state":
                        if (line2[0].trim().equals("")) {
                            throw new IOException("The initial state is missing!");
                        }
                        this.initialState = line2[1].trim();
                        break;
                    case "final_states":
                        if (line2[0].trim().equals("")) {
                            throw new IOException("There are no final states!");
                        }
                        String[] final_states = line2[1].split(",");
                        for (int i = 0; i < final_states.length; i++) {
                            this.finalStates.add(final_states[i].trim());
                        }
                        break;
                    case "transitions":
                        break;
                    default:
                        this.transitions.add(new Transition(line2[0].trim()));

                }
            }


        }
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<String> finiteStates) {
        this.finalStates = finiteStates;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(Set<Transition> transitions) {
        this.transitions = transitions;
    }
}
