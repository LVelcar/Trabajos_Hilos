import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Clase que representa el baño usando una cola de mensajes
class Bano2 {
    private final BlockingQueue<String> buzon; // Buzón para manejar el paso de mensajes

    public Bano2() {
        buzon = new ArrayBlockingQueue<>(1); // Cola con capacidad para 1 mensaje
        try {
            buzon.put("libre"); // Inicialmente, el baño está libre
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void usar(int borrachoId) throws InterruptedException {
        String mensaje = buzon.take(); // Espera a recibir el mensaje "libre"
        if (mensaje.equals("libre")) {
            System.out.println("Borracho " + borrachoId + " está usando el baño.");
            Thread.sleep(2000); // Simula tiempo usando el baño
            System.out.println("Borracho " + borrachoId + " ha salido del baño.");
            buzon.put("libre"); // Envía el mensaje "libre" para indicar que el baño está disponible
        }
    }
}

// Clase que representa el bar
class Bar2 {
    private final Bano2 bano; // Referencia al baño

    public Bar2(Bano2 bano) {
        this.bano = bano;
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
class Borrachito2 extends Thread {
    private final Bar2 bar;
    private final int id;

    public Borrachito2(Bar2 bar, int id) {
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
public class BarMessages {
    public static void main(String[] args) {
        Bano bano = new Bano();
        Bar bar = new Bar(bano);

        // Crear e iniciar dos borrachos
        Borrachito borrachito1 = new Borrachito(bar, 1);
        Borrachito borrachito2 = new Borrachito(bar, 2);

        borrachito1.start();
        borrachito2.start();
    }
}
