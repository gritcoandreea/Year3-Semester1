import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreea Gritco on 07-Jan-18.
 */
public class CFG {

    private List<Rule> rules;

    public CFG() {

        rules = new ArrayList<>();
    }

    public void initFromFileForProgram(String fileName) {

        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String text = null;

            while ((text = reader.readLine()) != null) {

                String[] line = text.split(":");
                String ruleName = line[0];
                String paths = line[1];
                String[] allPaths = paths.split("\\|");
                List<List<String>> allAllPaths = new ArrayList<>();
                for (int i = 0; i < allPaths.length; i++) {
                    List<String> bySpace = new ArrayList<>();

                    allPaths[i] = allPaths[i].trim();
                    String[] separated = allPaths[i].split("\\s+");
                    for (int j = 0; j < separated.length; j++) {
                        bySpace.add(separated[j].trim());
                    }
                    allAllPaths.add(bySpace);

                }

                int index = this.ruleExists(ruleName);
                if (index != -1) {
                    for (List<String> l : allAllPaths) {
                        this.rules.get(index).addPathProgram(l);
                    }
                } else {
                    this.rules.add(new Rule(ruleName));
                    for (List<String> l : allAllPaths) {
                        this.rules.get(this.rules.size() - 1).addPathProgram(l);
                    }
                }


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
    }


    public void initFromFile(String fileName) {

        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String text = null;

            while ((text = reader.readLine()) != null) {

                String[] splits = text.split(":");
                String withoutSpaces = splits[1].trim();
                String[] allPaths = withoutSpaces.split("\\|");

                for (int i = 0; i < allPaths.length; i++) {
                    allPaths[i] = allPaths[i].trim();
                }

                int idx = ruleExists(splits[0]);
                if (idx != -1) {
                    for (int i = 0; i < allPaths.length; i++) {
                        this.rules.get(idx).addPath(allPaths[i]);
                    }
                } else {
                    this.rules.add(new Rule(splits[0]));
                    for (int i = 0; i < allPaths.length; i++) {
                        this.rules.get(this.rules.size() - 1).addPath(allPaths[i]);
                    }
                }


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

    }

    public int ruleExists(String name) {
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.get(i).getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return "CFG{" +
                "rules=" + rules +
                '}';
    }
}
