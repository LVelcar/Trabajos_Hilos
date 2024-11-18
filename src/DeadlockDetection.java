import java.util.*;
public class DeadlockDetection {
    // Grafo de precedencia (matriz de adyacencia)
    private static boolean [][] graph;
    private static int numNodes;

    // Constructor para inicializar el grafo.
    public DeadlockDetection(int nodes) {
        numNodes = nodes;
        graph = new boolean[nodes][nodes];
    }

    // Método para agregar una arista al grafo.
    public void addEdge(int from, int to) {
        graph[from][to] = true;
    }

    // Método principal para detectar ciclos.
    public boolean hasDeadlock() {
        boolean[] visited = new boolean[numNodes];
        boolean[] stack = new boolean[numNodes];

        for (int i = 0; i < numNodes; i++) {
            if (detectCycle(i, visited, stack)) {
                return true; // Ciclo encontrado, hay interbloqueo.
            }
        }
        return false; // No se detectó ningún ciclo.
    }

    // DFS para detectar ciclos.
    private boolean detectCycle(int node, boolean[] visited, boolean[] stack) {
        if (stack[node]) {
            return true; // Nodo en pila, ciclo detectado.
        }
        if (visited[node]) {
            return false; // Nodo ya procesado, no hay ciclo aquí.
        }

        visited[node] = true;
        stack[node] = true;

        for (int neighbor = 0; neighbor < numNodes; neighbor++) {
            if (graph[node][neighbor]) { // Existe una arista.
                if (detectCycle(neighbor, visited, stack)) {
                    return true;
                }
            }
        }
        stack[node] = false; // Quitar el nodo de la pila.
        return false;
    }

    public static void main(String[] args) {
        // Ejemplo de grafo de precedencia con 5 nodos.
        DeadlockDetection detection = new DeadlockDetection(5);

        // Agregamos aristas (procesos y recursos).
        detection.addEdge(0, 1); // P0 -> R1
        detection.addEdge(1, 2); // R1 -> P2
        detection.addEdge(2, 3); // P2 -> R3
        detection.addEdge(3, 1); // R3 -> P1 (Crea un ciclo)
        detection.addEdge(4, 0); // P4 -> R0

        // Comprobamos si hay un interbloqueo.
        if (detection.hasDeadlock()) {
            System.out.println("¡Interbloqueo detectado!");
        } else {
            System.out.println("No hay interbloqueo");
        }
    }
}
