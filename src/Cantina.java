import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Clase que representa el baño
class Bano {
    private final Lock lock = new ReentrantLock();
    private final Condition puedeEntrar = lock.newCondition();
    private boolean enUso = false;

    public void usar(int borrachoId) throws InterruptedException {
        lock.lock();
        try {
            // Espera si el baño ya está en uso
            while (enUso) {
                System.out.println("Borracho " + borrachoId + " esperando para usar el baño.");
                puedeEntrar.await();
            }
            enUso = true; // Marca que el baño está en uso
            System.out.println("Borracho " + borrachoId + " está usando el baño.");
            Thread.sleep(2000); // Simula tiempo usando el baño
        } finally {
            enUso = false; // Libera el baño
            System.out.println("Borracho " + borrachoId + " ha salido del baño.");
            puedeEntrar.signalAll(); // Notifica a los que esperan
            lock.unlock();
        }
    }
}

// Clase que representa el bar
class Bar {
    private final Bano bano; // Referencia al baño

    public Bar(Bano bano) {
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
class Borrachito extends Thread {
    private final Bar bar; // Referencia al bar
    private final int id; // Identificador del borracho

    public Borrachito(Bar bar, int id) {
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
public class Cantina {
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
