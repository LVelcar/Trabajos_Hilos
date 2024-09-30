class Turista implements Runnable {
    private static final int MAX_TRANSPORTES = 4; // Total de transportes por turista
    private static final int NUM_TURISTAS = 5; // Número total de turistas
    private static final boolean[] flag = new boolean[NUM_TURISTAS]; // Banderas para cada turista
    private static int turn = 0; // Indica quién tiene el turno
    private final int id; // Identificador del turista
    private int transportesRestantes; // Contador de transportes restantes

    public Turista(int id) {
        this.id = id;
        this.transportesRestantes = MAX_TRANSPORTES;
    }

    @Override
    public void run() {
        while (transportesRestantes > 0) {
            intentarSubirALaLancha();
        }
        System.out.println("Turista " + (id + 1) + " ha terminado su día de paseo.");
    }

    private void intentarSubirALaLancha() {
        flag[id] = true; // Indica interés en acceder a la sección crítica

        // Espera mientras otro turista está en la sección crítica y es su turno
        while (flag[turn] && turn != id) {
            Thread.yield(); // Cede la CPU a otros hilos
        }

        // Sección crítica
        if (transportesRestantes > 0) {
            System.out.println("Turista " + (id + 1) + " subiendo a la lancha. Transportes restantes: " + transportesRestantes);
            transportesRestantes--; // Decrementa el número de transportes restantes
            try {
                Thread.sleep(1000); // Simula el tiempo del viaje
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Turista " + (id + 1) + " ha llegado a la isla.");
        }

        flag[id] = false; // Sale de la sección crítica
        turn = (id + 1) % NUM_TURISTAS; // Pasa el turno al siguiente turista
    }
}

public class PaseoLancha {
    public static void main(String[] args) {
        Thread[] turistas = new Thread[5];

        // Crear y empezar los hilos de turistas
        for (int i = 0; i < turistas.length; i++) {
            turistas[i] = new Thread(new Turista(i));
            turistas[i].start();
        }

        for (Thread turista : turistas) {
            try {
                turista.join(); // Espera a que todos los turistas terminen
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Todos los turistas han terminado su día de paseo.");
    }
}
