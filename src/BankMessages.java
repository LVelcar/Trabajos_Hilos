import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Clase que representa una cuenta bancaria
class CuentaBancaria2 {
    int saldo = 100;
    private final BlockingQueue<String> buzón = new ArrayBlockingQueue<>(1); // Buzón para manejar el paso de mensajes

    public CuentaBancaria2() {
        try {
            buzón.put("libre"); // Inicialmente, la cuenta está libre
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método para retirar dinero de la cuenta utilizando paso de mensajes
    public void retirar(int cantidad, int clienteId) throws InterruptedException {
        String mensaje = buzón.take(); // Espera a recibir el mensaje "libre"
        if (mensaje.equals("libre")) {
            // Verifica si hay suficiente saldo para realizar el retiro
            if (saldo >= cantidad) {
                System.out.println("Cliente " + clienteId + " retirando: " + cantidad);
                saldo -= cantidad; // Resta la cantidad del saldo
                System.out.println("Saldo después del retiro: " + saldo);
            } else {
                // Si no hay suficiente saldo, informa al cliente
                System.out.println("Cliente " + clienteId + " no puede retirar " + cantidad + ". Saldo actual: " + saldo);
            }
            buzón.put("libre"); // Envía el mensaje "libre" para indicar que la cuenta está disponible de nuevo
        }
    }
}

// Clase que representa un cliente que intenta retirar dinero
class Cliente2 extends Thread {
    private final CuentaBancaria2 cuenta;
    private final int cantidad;
    private final int id;

    public Cliente2(CuentaBancaria2 cuenta, int cantidad, int id) {
        this.cuenta = cuenta;
        this.cantidad = cantidad;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            cuenta.retirar(cantidad, id); // Cliente intenta retirar la cantidad especificada
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cliente " + id + " fue interrumpido.");
        }
    }
}

// Clase principal para ejecutar el programa
public class BankMessages {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria(); // Crear una nueva instancia de la cuenta bancaria

        // Crear dos clientes que intentan retirar dinero
        Cliente cliente1 = new Cliente(cuenta, 80, 1);
        Cliente cliente2 = new Cliente(cuenta, 70, 2);

        // Iniciar los hilos de los clientes
        cliente1.start();
        cliente2.start();

        // Esperar a que ambos hilos terminen
        try {
            cliente1.join();
            cliente2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Fin de las transacciones. Saldo final: " + cuenta.saldo);
    }
}
