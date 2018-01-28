import java.util.concurrent.ExecutionException;

public class App {
		public static void main(String[] args) throws ExecutionException, InterruptedException {
				//exampleNotHamiltonianGraph();

				System.out.println("\n");

				exampleHamiltonianGraph();
		}

		private static void exampleHamiltonianGraph() throws ExecutionException, InterruptedException {
				Graph graph2 = new Graph();
				graph2.addEdge(2, 4);
				graph2.addEdge(2, 5);
				graph2.addEdge(5, 3);

				graph2.addEdge(1, 2);
				graph2.addEdge(2, 3);
				graph2.addEdge(3, 4);
				graph2.addEdge(4, 5);
				graph2.addEdge(5, 1);

				HamiltonianCycle hamiltonianCycle2 = new HamiltonianCycle(graph2);
				System.out.println(hamiltonianCycle2.findHamiltonianCycle(4));
		}

		private static void exampleNotHamiltonianGraph() throws ExecutionException, InterruptedException {
				Graph graph1 = new Graph();
				graph1.addEdge(1, 2);
				graph1.addEdge(2, 3);
				graph1.addEdge(3, 4);
				graph1.addEdge(4, 2);
				graph1.addEdge(2, 5);
				graph1.addEdge(5, 1);

				HamiltonianCycle hamiltonianCycle = new HamiltonianCycle(graph1);
				System.out.println(hamiltonianCycle.findHamiltonianCycle(4));
		}
}
