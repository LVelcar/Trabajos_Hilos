import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Productor implements Runnable {
    private BlockingQueue<String> buffer;

    public Productor(BlockingQueue<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        String[] mensajes = {"Mensaje 1", "Mensaje 2", "Mensaje 3", "FIN"};
        try {
            for (String mensaje : mensajes) {
                System.out.println("Productor: enviando " + mensaje);
                buffer.put(mensaje);  // Coloca el mensaje en el buffer
                Thread.sleep(1000);   // Simula tiempo de producción
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumidor implements Runnable {
    private BlockingQueue<String> buffer;

    public Consumidor(BlockingQueue<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            String mensaje;
            while (!(mensaje = buffer.take()).equals("FIN")) {
                System.out.println("Consumidor: procesando " + mensaje);
                Thread.sleep(1500);  // Simula tiempo de procesamiento
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class BufferAsincrono {
    public static void main(String[] args) {
        // Definir el tamaño del buffer
        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(5);

        // Crear productor y consumidor
        Thread productor = new Thread(new Productor(buffer));
        Thread consumidor = new Thread(new Consumidor(buffer));

        // Iniciar los hilos
        productor.start();
        consumidor.start();
    }
}

