import java.util.concurrent.Semaphore;

public class CenaDeLosFilosofos {
    // Semáforos para los tenedores y habitación.
    private static final Semaphore[] tenedores = new Semaphore[5];
    private static final Semaphore habitacion = new Semaphore(4); // Límite de filósofos.

    public static void main(String[] args) {
        // Inicialización de semáforos para los tenedores.
        for (int i = 0; i < 5; i++) {
            tenedores[i] = new Semaphore(1); // Cada tenedor está inicalmente disponible.
        }

        // Crear y arrancar los hilos para los filósofos.
        for (int i = 0; i < 5; i++) {
            final int id = i; // Identificador del filósofo.
            new Thread(() -> filosofo(id)).start();
        }
    }

    private static void filosofo(int id) {
        try {
            while (true) {
                // Fase de pensar.
                System.out.println("Filósofo " + id + " está pensando.");
                Thread.sleep((int) (Math.random() * 1000)); // Simular tiempo de pensar.

                // Intentar entrar en la habitación.
                habitacion.acquire();
                System.out.println("Filósofo " + id + " entra en la habitación.");

                // Tomar los tenedores.
                tenedores[id].acquire(); // Izquierdo.
                tenedores[(id + 1 ) % 5].acquire(); // Derecho.

                // Comer.
                System.out.println("Filósofo " + id + " está comiendo.");
                Thread.sleep((int) (Math.random() * 1000)); // Simular tiempo comiendo.

                // Dejar los tenedores.
                tenedores[(id + 1) % 5].release(); //Soltar tenedor derecho.
                tenedores[id].release(); // Soltar tenedor izquierdo.

                // Salir de la habitación.
                System.out.println("Filósofo " + id + " sale de la habitación.");
                habitacion.release();
            }
        } catch (InterruptedException e) {
            System.out.println("Filósofo " + id + " fue interrumpido.");
        }
    }
}
