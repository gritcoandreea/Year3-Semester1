import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreea Gritco on 07-Jan-18.
 */
public class Rule {

    private String name;
    private List<List<String>> paths;

    public Rule(String name) {
        this.name = name;
        paths = new ArrayList<>();
    }

    public void addPath(String path){
        List<String> tokens = new ArrayList<>();
        for(char c : path.toCharArray()){
            tokens.add(Character.toString(c));
        }
        this.paths.add(tokens);
    }

    public void addPathProgram(List<String> path){
        this.paths.add(path);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<String>> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return this.name + " - " + paths.toString();
    }
}
