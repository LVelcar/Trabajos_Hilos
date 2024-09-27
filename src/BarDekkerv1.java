import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Clase que representa el baño
class Banov1 {
    private final Lock lock = new ReentrantLock();
    private final Condition puedeEntrar = lock.newCondition();
    private boolean enUso = false;
    private int turno = 1; // Controla el turno de los borrachos

    public void usar(int borrachoId) throws InterruptedException {
        lock.lock();
        try {
            while (enUso || turno != borrachoId) {
                System.out.println("Borracho " + borrachoId + " esperando para usar el baño.");
                puedeEntrar.await();
            }

            enUso = true; // Borracho entra al baño
            System.out.println("Borracho " + borrachoId + " está usando el baño.");
            Thread.sleep(2000); // Simula tiempo usando el baño

        } finally {
            enUso = false; // Libera el baño
            System.out.println("Borracho " + borrachoId + " ha salido del baño.");

            // Alterna el turno después de usar el baño
            turno = (borrachoId == 1) ? 2 : 1;
            puedeEntrar.signalAll(); // Notifica a todos
            lock.unlock();
        }
    }

}

// Clase que representa el bar
class Barv1 {
    private final Banov1 bano; // Referencia al baño

    public Barv1(Banov1 bano) {
        this.bano = bano; // Inicializa el baño
    }

    public void tomar(int borrachoId) {
        System.out.println("Borracho " + borrachoId + " está tomando.");
        try {
            Thread.sleep(1000); // Simula el tiempo tomando
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void usarBano(int borrachoId) {
        try {
            bano.usar(borrachoId); // Llama al método usar del baño
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase que representa a un borracho
class Borrachitov1 extends Thread {
    private final Barv1 bar; // Referencia al bar
    private final int id; // Identificador del borracho

    public Borrachitov1(Barv1 bar, int id) {
        this.bar = bar;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            bar.tomar(id); // Borracho toma
            bar.usarBano(id); // Borracho intenta usar el baño
        }
    }
}

// Clase principal para ejecutar el programa
public class BarDekkerv1 {
    public static void main(String[] args) {
        Bano bano = new Bano(); // Crear el baño
        Bar bar = new Bar(bano); // Crear el bar con el baño

        // Crear e iniciar dos borrachos
        Borrachito borrachito1 = new Borrachito(bar, 1);
        Borrachito borrachito2 = new Borrachito(bar, 2);

        borrachito1.start(); // Iniciar el hilo del primer borracho
        borrachito2.start(); // Iniciar el hilo del segundo borracho
    }
}
