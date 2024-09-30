import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// Clase que implementa el algoritmo de panadería de Lamport
class Panaderia {
    private final AtomicBoolean[] eligiendo; // Indica si un hilo está eligiendo un número
    private final AtomicInteger[] numero; // Almacena el número de turno de cada hilo
    private final int n; // Número total de hilos (turistas)

    // Constructor de la clase, inicializa las variables
    public Panaderia(int n) {
        this.n = n;
        eligiendo = new AtomicBoolean[n];
        numero = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            eligiendo[i] = new AtomicBoolean(false);
            numero[i] = new AtomicInteger(0);
        }
    }

    public void tomarTurno(int i) {
        eligiendo[i].set(true); // Indica que el hilo i está eligiendo
        numero[i].set(maximo() + 1); // Asigna el turno más alto + 1
        eligiendo[i].set(false); // Indica que ha terminado de elegir
    }

    public boolean comparar(int i, int j) {
        // Si el hilo j no tiene número asignado, no hay conflicto
        if (numero[j].get() == 0) return false;
        // Si el hilo i tiene un número menor, tiene prioridad
        if (numero[i].get() < numero[j].get()) return true;
        // Si los números son iguales, se usa el identificador para romper el empate
        if (numero[i].get() == numero[j].get() && i < j) return true;
        return false; // En caso contrario, no hay prioridad
    }

    public void entrarSeccionCritica(int i) {
        tomarTurno(i); // El hilo toma su turno
        for (int j = 0; j < n; j++) {
            if (i != j) {
                while (eligiendo[j].get()) {
                    // Espera a que el otro turista tome un número
                    // Espera activa (busy waiting)
                }
                while (numero[j].get() != 0 && !comparar(i, j)) {
                    // Espera a que termine el turista con mayor prioridad
                    // Espera activa
                }
            }
        }
    }

    public void salirSeccionCritica(int i) {
        numero[i].set(0); // Libera el turno
    }

    // Método para encontrar el número más alto en el array de números de turno
    private int maximo() {
        int max = numero[0].get(); // Inicializa el máximo con el primer número
        for (int i = 1; i < n; i++) {
            max = Math.max(max, numero[i].get()); // Actualiza el número.
        }
        return max;
    }
}

// Clase que representa a un turista que será un hilo
class TuristaLamport extends Thread {
    private final int id; // Identificador del turista
    private int transportes; // Contador de transportes restantes
    private final Panaderia panaderia; // Instancia del algoritmo de panadería
    private static final Object lancha = new Object(); // Simula la lancha

    public TuristaLamport(int id, Panaderia panaderia) {
        this.id = id;
        this.panaderia = panaderia;
        this.transportes = 4; // Cada turista tiene 4 transportes
    }

    public void run() {
        while (transportes > 0) {
            panaderia.entrarSeccionCritica(id);

            // Sección crítica: solo un turista puede usar la lancha a la vez
            synchronized (lancha) {
                System.out.println("Turista " + id + " está en la lancha. Transportes restantes: " + transportes);
                transportes--;
                try {
                    Thread.sleep(1000); // Simula el viaje en lancha
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            panaderia.salirSeccionCritica(id);
        }

        System.out.println("Turista " + id + " ha terminado todos sus transportes.");
    }
}

public class LanchaLamport {
    public static void main(String[] args) {
        int numTuristas = 5;
        Panaderia panaderia = new Panaderia(numTuristas);
        TuristaLamport[] turistas = new TuristaLamport[numTuristas];

        for (int i = 0; i < numTuristas; i++) {
            turistas[i] = new TuristaLamport(i, panaderia);
            turistas[i].start();
        }

        for (int i = 0; i < numTuristas; i++) {
            try {
                turistas[i].join(); // Espera a que cada hilo termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos los turistas han terminado sus transportes.");
    }
}
