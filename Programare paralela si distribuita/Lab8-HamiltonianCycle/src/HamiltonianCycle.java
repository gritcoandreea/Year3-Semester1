import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HamiltonianCycle {
    private Graph graph;

    private int[] finalPath;
    private ExecutorService service;

    public HamiltonianCycle(Graph graph) {
        this.graph = graph;
    }

    public boolean findHamiltonianCycle(int nrThreads) throws ExecutionException, InterruptedException {
        //create a path of nr nodes length
        int[] path = new int[graph.getNrNodes()];
        //populate path with 0's
        for (int i = 0; i < graph.getNrNodes(); i++) {
            path[i] = 0;
        }
        service = Executors.newFixedThreadPool(nrThreads);

        //put a random node as start in the path
        Random r = new Random();
        path[0] = r.nextInt(graph.getNrNodes() + 1 - 0) + 0;

        if (!findIfHamiltonianRecursive(1, nrThreads, path)) {
            return false;
        }

        service.shutdownNow();
        printPath(finalPath, "Solution exists - the hamiltonian cycle is: ");
        return true;
    }

    /**
     * Recursive function to solve the hamiltonian cycle problem
     *
     * @param pos - to start
     * @return
     */
    private boolean findIfHamiltonianRecursive(int pos, int nrThreads, int[] pathParam) throws ExecutionException,
            InterruptedException {

        //check if all nodes are included in the Ham Cycle
        if (pos == graph.getNrNodes()) {
            //Checks if there is an edge from the last included node to the first included node
            if (graph.hasEdge(pathParam[pos - 1], pathParam[0])) {
                finalPath = Arrays.copyOf(pathParam, graph.getNrNodes());
                return true;
            } else {
                return false;
            }
        }

        if (nrThreads > 1) {
            //Try different nodes as a next candidate in Hamiltonian Cycle
            //The first node was included as a starting point already
            List<Future<Boolean>> resultList = new ArrayList<>();
            for (Integer v : graph.getNodes()) {
                if (canAddToPath(pos, v, pathParam)) {
                    pathParam[pos] = v;

                    printPath(pathParam, "sol nr th>1 : ");

                    int[] copyPath = Arrays.copyOf(pathParam, graph.getNrNodes());
                    resultList.add(service.submit(() -> findIfHamiltonianRecursive(pos + 1, nrThreads / graph.getNrNodes(),
                            copyPath)));
                }
            }

            //get the result from the executed threads -- if a Hamiltonian cycle was found
            for (Future<Boolean> f : resultList) {
                Boolean res = f.get();
                if (res) {
                    return true;
                }
            }
        } else {
            for (Integer v : graph.getNodes()) {
                if (canAddToPath(pos, v, pathParam)) {

                    printPath(pathParam, "sol: ");

                    pathParam[pos] = v;
                    if (findIfHamiltonianRecursive(pos + 1, 1, pathParam)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns false -> if the current vertex is an adjacent vertex of the previously added vertex;
     * -> if the current vertex has already been added in the path
     */
    private boolean canAddToPath(int pos, int current, int[] path) {
        /* Check if this vertex is an adjacent vertex of the previously added vertex. */
        if (!graph.hasEdge(path[pos - 1], current)) {
            return false;
        }
        //check if this vertex has already been included in the path
        for (int i = 0; i < pos; i++) {
            if (path[i] == current) {
                return false;
            }
        }
        return true;
    }

    private void printPath(int[] path, String message) {
        StringBuilder str = new StringBuilder(message);
        for (int i = 0; i < graph.getNrNodes(); i++) {
            str.append(" ").append(path[i]).append(" ");
        }
        str.append(" ").append(path[0]).append(" ");
        System.out.println(str.toString());
    }
}
