class Dekker {
    private boolean[] flag = new boolean[5]; // Bandera para cada hilo (turista)
    private int turno = 0; // Indica a qué turista le toca usar la lancha

    public synchronized void enter(int i) {
        flag[i] = true; // El turista 'i' quiere subir a la lancha
        while (turno != i) { // Solo puede entrar si es su turno
            boolean someoneElseWants = false; // Variable para verificar si otro hilo quiere entrar
            for (int j = 0; j < 5; j++) {
                if (flag[j] && j != i) { // Si hay otro turista que quiere entrar
                    someoneElseWants = true; // Se establece a true si otro quiere entrar
                    break;
                }
            }
            if (someoneElseWants) {
                flag[i] = false; // Dejar de querer entrar
                while (turno != i) {
                    try {
                        wait(); // Espera hasta que sea su turno
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                flag[i] = true; // Volver a querer entrar
            }
        }
    }

    public synchronized void exit(int i) {
        flag[i] = false; // El turista ha dejado de querer usar la lancha
        turno = (turno + 1) % 5; // Asigna el turno al siguiente turista
        notifyAll(); // Notifica a todos los turistas que pueden intentar usar la lancha
    }
}

class Turista extends Thread {
    private int id;
    private Dekker dekker;
    private int transportes = 4; // Cada turista tiene derecho a 4 transportes

    public Turista(int id, Dekker dekker) {
        this.id = id;
        this.dekker = dekker;
    }

    @Override
    public void run() {
        while (transportes > 0) {
            dekker.enter(id); // Intentar entrar en la sección crítica

            // Sección crítica: uso de la lancha
            System.out.println("Turista " + id + " subió a la lancha. Transportes restantes: " + (transportes - 1));
            transportes--;

            dekker.exit(id); // Salir de la sección crítica

            // Simular el tiempo del viaje
            try {
                Thread.sleep(1000); // Simula el tiempo de viaje
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Mensaje final cuando el turista ha terminado sus transportes
        System.out.println("Turista " + id + " ha finalizado sus viajes.");
    }
}

public class PaseoLancha {
    public static void main(String[] args) {
        Dekker dekker = new Dekker();
        Turista[] turistas = new Turista[5];

        // Crear hilos para cada turista
        for (int i = 0; i < 5; i++) {
            turistas[i] = new Turista(i, dekker);
            turistas[i].start();
        }

        // Esperar a que todos los turistas terminen
        for (int i = 0; i < 5; i++) {
            try {
                turistas[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Todos los turistas han finalizado sus viajes.");
    }
}
