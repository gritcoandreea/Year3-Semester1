import java.util.*;

public class Main {

    public static void main(String[] args) {
        //FiniteAutomata fa = new FiniteAutomata("src/finite_automata.txt");
        FiniteAutomata fa = new FiniteAutomata("src/cpp_integer_literals.txt");
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.println("Enter command: ");
            String command = keyboard.nextLine();
            switch (command.trim()) {
                /**Print states.*/
                case "1":
                    printSet(fa.getStates());
                    break;
                /**Print the alphabet.*/
                case "2":
                    printSet(fa.getAlphabet());
                    break;
                /**Print the initial state.*/
                case "3":
                    System.out.println(fa.getInitialState());
                    break;
                /**Print the final states.*/
                case "4":
                    printSet(fa.getFinalStates());
                    break;
                /**Print the transitions.*/
                case "5":
                    prinTransitions(fa.getTransitions(),fa.getStates());
                    break;
                case "6":
                    /**
                     * If the FA is deterministic , we read a sequence from the keyboard and we check it,
                     * else we display a message that the FA is not deterministic.
                     */
                    if(!fa.checkIfDeterministic())
                    {
                        System.err.println("The FA is not deterministic,hence we cannot validate a sequence!");
                        break;
                    }
                    String sequence;
                    System.out.println("Give sequence: ");
                    sequence = keyboard.nextLine().trim();
                    if (fa.validateSequence(sequence)) {
                        System.out.println("The sequence " + sequence + " IS accepted by the FA!");
                    } else {
                        System.out.println("The sequence " + sequence + " IS NOT accepted by the FA!");
                    }
                    break;
                /**
                 * If the FA is deterministic , we read a sequence from the keyboard and we check it,
                 * else we display a message that the FA is not deterministic.
                 */
                case "7":
                    if(!fa.checkIfDeterministic())
                    {
                        System.err.println("The FA is not deterministic,hence we cannot find the longest valid prefix!");
                        break;
                    }
                    String sequence2;
                    System.out.println("Give sequence: ");
                    sequence2 = keyboard.nextLine().trim();
                    if (fa.findLongestAcceptedPrefix(sequence2).equals("")) {
                        System.out.println("No prefix found!");
                    } else {
                        System.out.println("The longest accepted prefix is: " + fa.findLongestAcceptedPrefix(sequence2));
                    }
                    break;
                case "8":
                    return;
                default:
                    System.err.println("Undefined command!");
                    break;
            }


        }


    }

    public static void prinTransitions(Set<Transition> trans,Set<String> states){
//        Map<String,Map<String,String>> map = new HashMap<>();
//        for(Transition t : trans){
//            if(!map.containsKey(t.getLeftState())){
//                map.put(t.getLeftState(),new HashMap<>());
//                map.get(t.getLeftState()).put(t.getLiteral(),t.getResultState());
//            }else{
//                map.get(t.getLeftState()).put(t.getLiteral(),t.getResultState());
//            }
//        }
//
//        for(String key : map.keySet()){
//            for(String key2: map.get(key).keySet()){
//                System.out.print("t(" + key + "," + key2 + ")=" + map.get(key).get(key2) + "    ");
//            }
//            System.out.println();
//        }
        List<String> statess = new ArrayList<>();
        statess.addAll(states);
        List<String> statesStrings = new ArrayList<>();
        for(String s : statess){
            statesStrings.add("");
        }
       for(Transition t : trans){
            for(int i=0;i<states.size();i++){
                if(t.getLeftState().equals(statess.get(i))){
                    String s = statesStrings.get(i);
                    s+=t.toString() + "    ";
                    statesStrings.set(i,s);
                }
            }
       }

       for(String s:statesStrings){
           System.out.println(s);
       }


    }

    /** Static funciton which prints a set of states, final states or the alphabet*/
    public static void printSet(Set<String> set) {
        System.out.print("[ ");
        for (String a : set) {
            System.out.print(a + " ");
        }

        System.out.print("]\n");
    }

    /**Prints the menu of the program.*/
    public static void printMenu() {
        System.out.println("----Menu---");
        System.out.println("1. Display the set of states");
        System.out.println("2. Display the alphabet");
        System.out.println("3. Display the initial state");
        System.out.println("4. Display the final states");
        System.out.println("5. Display the transitions");
        System.out.println("6. Verify if a sequence is accepted by the FA");
        System.out.println("7. Get the longest accepted prefix by the FA");
        System.out.println("8. Exit:)");
    }

}
