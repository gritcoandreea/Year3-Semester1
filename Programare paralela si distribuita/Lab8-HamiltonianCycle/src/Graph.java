import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    //key : node , value : list of nodes where you can reach from key node
    private Map<Integer, List<Integer>> adjancyList;

    public Graph() {
        adjancyList = new HashMap<>();
    }

    public List<Integer> getNodes() {
        return new ArrayList<>(adjancyList.keySet());
    }

    public boolean addNode(Integer v) {
        if (adjancyList.containsKey(v)) {
            return false;
        }
        adjancyList.put(v, new ArrayList<>());
        return true;
    }

    public void addEdge(Integer v1, Integer v2) {
        addNode(v1);
        addNode(v2);
        this.adjancyList.get(v1).add(v2);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Graph \n");
        for (Integer v : adjancyList.keySet()) {
            s.append("vertex: ").append(v).append(" [");
            for (Integer v2 : adjancyList.get(v)) {
                s.append(v2).append(" ");
            }
            s.append(" ]\n");
        }
        return s.toString();
    }

    public int getNrNodes() {
        return adjancyList.keySet().size();
    }

    public boolean hasEdge(Integer v1, Integer v2) {
        return adjancyList.get(v1).contains(v2);
    }
}
