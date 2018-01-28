import sun.awt.image.ImageWatched;

import java.io.*;
import java.util.*;

/**
 * @author Andreea Gritco on 07-Jan-18.
 */
public class Automaton {

    private CFG cfg;

    private int state = State.NORMAL.getState();
    private int position;
    LinkedList<String> inputStack;
    LinkedList<Tuple> workStack;


    public Automaton(String fileName, boolean forProgram) {
        this.cfg = new CFG();
        if (!forProgram) {
            this.cfg.initFromFile(fileName);
        }else{
            this.cfg.initFromFileForProgram(fileName);
        }
        this.position = 1;
        inputStack = new LinkedList<>();
        inputStack.add(this.cfg.getRules().get(0).getName());
        workStack = new LinkedList<>();
        System.out.println("State:  " + this.state + "\n" + "Position: " + this.position + "\n" + "Work Stack: " + this.workStack + "\n" + "Input Stack: " + this.inputStack + "\n");

    }

    public int parse(List<String> sequence) {
        if (this.inputStack.size() == 0) {
            if (this.position == sequence.size() + 1) {
                this.state = State.END.getState();
                return -1;
            }
        }

        if (this.position > sequence.size()) {
            System.out.println("FAILURE!");
            System.exit(1);
        }

        if (this.inputStack.size() > 0) {
            int index = this.cfg.ruleExists(this.inputStack.get(0));
            if (index != -1) {

                System.out.println("\tExpand\n");
                this.state = State.NORMAL.getState();

                for (int i = 0; i < this.cfg.getRules().get(index).getPaths().size(); i++) {
                    LinkedList<Tuple> workStackCopy = (LinkedList<Tuple>) this.workStack.clone();
                    LinkedList<String> inputStackCopy = (LinkedList<String>) this.inputStack.clone();

                    this.workStack.add(new Tuple(this.inputStack.get(0), i));
                    this.inputStack.pollFirst();
                    //this.inputStack.addFirst(makeString(this.cfg.getRules().get(index).getPaths().get(i)));

                    //add a path( which is represented as a list of characters) in input stack
                    for (int j = this.cfg.getRules().get(index).getPaths().get(i).size() - 1; j >= 0; j--) {
                        this.inputStack.addFirst(this.cfg.getRules().get(index).getPaths().get(i).get(j));

                    }

                    if (this.state != State.END.getState()) {
                        System.out.println("State:  " + this.state + "\n" + "Position: " + this.position + "\n" + "Work Stack: " + this.workStack + "\n" + "Input Stack: " + this.inputStack + "\n");
                    }

                    this.parse(sequence);
                    if (this.state == State.END.getState()) {
                        return -1;
                    }

                    while (!workStack.isEmpty()) {
                        workStack.removeFirst();
                    }
                    workStack.addAll(workStackCopy);

                    while (!inputStack.isEmpty()) {
                        inputStack.removeFirst();
                    }

                    inputStack.addAll(inputStackCopy);


                }

            } else {


                if (this.inputStack.get(0).equals((sequence.get(this.position - 1)))) {
                    System.out.println("\tAdvance");
                    this.state = State.NORMAL.getState();
                    this.position++;
                    this.workStack.add(new Tuple(this.inputStack.get(0), -555));
                    List<String> save = new ArrayList<>();
                    save.add(this.inputStack.get(0));
                    this.inputStack.remove(0);

                    if (this.state != State.END.getState()) {
                        System.out.println("State:  " + this.state + "\n" + "Position: " + this.position + "\n" + "Work Stack: " + this.workStack + "\n" + "Input Stack: " + this.inputStack);
                    }

                    this.parse(sequence);
                    if (this.state == State.END.getState()) {
                        return -1;
                    }
                    this.position--;
                    this.workStack.remove(this.workStack.size() - 1);
                    this.inputStack.addFirst(save.get(0));
                } else {
                    System.out.println("\tLocal failure ( & go back, another try)");
                    this.state = State.BACK.getState();
                }
            }


        }
        return -1;

    }


    public Map<Integer, String> initCodes(String fileName) {
        Map<Integer, String> codes = new HashMap<>();

        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(":");
                codes.put(Integer.parseInt(tokens[0].trim()), tokens[1].trim());

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return codes;

    }

    public List<String> pifToCfgSequence(Map<Integer, String> codes, String fileName) {
        List<String> sequence = new ArrayList<>();

        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                sequence.add(codes.get(Integer.parseInt(line.trim())));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return sequence;
    }


    private boolean checkCFG() {
        List<String> allRulesNames = new ArrayList<>();
        for (Rule r : this.cfg.getRules()) {
            allRulesNames.add(r.getName());
        }

        for (Rule r : this.cfg.getRules()) {
            for (List<String> paths : r.getPaths()) {
                boolean is = false;
                for (String name : allRulesNames) {
                    if (name.equals(paths.get(0))) {
                        is = true;
                    }
                }

                if (!is) {
                    return true;
                }
                is = false;
            }
        }

        return false;
    }

    public int getState() {
        return state;
    }
}
