import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Clase que representa una cuenta bancaria
class CuentaBancaria {
    int saldo = 100;
    private final Lock lock = new ReentrantLock(); // Lock para controlar el acceso a la cuenta

    // Método para retirar dinero de la cuenta
    public void retirar(int cantidad, int clienteId) {
        lock.lock(); // Adquiere el lock antes de entrar a la sección crítica
        try {
            // Verifica si hay suficiente saldo para realizar el retiro
            if (saldo >= cantidad) {
                System.out.println("Cliente " + clienteId + " retirando: " + cantidad);
                saldo -= cantidad; // Resta la cantidad del saldo
                System.out.println("Saldo después del retiro: " + saldo);
            } else {
                // Si no hay suficiente saldo, informa al cliente
                System.out.println("Cliente " + clienteId + " no puede retirar " + cantidad + ". Saldo actual: " + saldo);
            }
        } finally {
            lock.unlock(); // Libera el lock para que otros hilos puedan acceder a la sección crítica
        }
    }
}

// Clase que representa un cliente que intenta retirar dinero
class Cliente extends Thread {
    private final CuentaBancaria cuenta; // Referencia a la cuenta bancaria
    private final int cantidad;
    private final int id;

    public Cliente(CuentaBancaria cuenta, int cantidad, int id) {
        this.cuenta = cuenta;
        this.cantidad = cantidad;
        this.id = id;
    }

    @Override
    public void run() {
        // Cuando el hilo se ejecuta, intenta retirar la cantidad especificada
        cuenta.retirar(cantidad, id);
    }
}

// Clase principal para ejecutar el programa
public class Banco {
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
            cliente1.join(); // Espera a que el hilo del primer cliente termine
            cliente2.join(); // Espera a que el hilo del segundo cliente termine
        } catch (InterruptedException e) {
            e.printStackTrace(); // Maneja cualquier excepción de interrupción
        }

        System.out.println("Fin de las transacciones. Saldo final: " + cuenta.saldo);
    }
}
