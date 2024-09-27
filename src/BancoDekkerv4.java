// Clase que representa una cuenta bancaria
class CuentaBancariav4 {
    int saldo = 100; // Saldo inicial
    private boolean[] bandera = new boolean[2]; // Banderas para los hilos
    private int turno; // Turno del hilo

    // Método para retirar dinero de la cuenta
    public void retirar(int cantidad, int clienteId) {
        int otroCliente = clienteId == 1 ? 0 : 1; // Determina el otro cliente

        // Entrar a la sección crítica usando la prioridad de Dekker
        while (true) {
            bandera[clienteId - 1] = true; // Indica que el cliente está listo para entrar

            // Verifica si el otro cliente está listo y no es su turno
            while (bandera[otroCliente]) {
                if (turno == otroCliente) {
                    // Si es el turno del otro cliente, el hilo espera
                    bandera[clienteId - 1] = false; // Se retira para evitar el monopolio
                    while (turno == otroCliente) {
                        // Esperar activamente (bloqueo activo)
                    }
                    bandera[clienteId - 1] = true; // Intenta entrar de nuevo
                }
            }

            // Ingresar a la sección crítica
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
                // Salir de la sección crítica
                turno = otroCliente; // Cambia el turno al otro cliente
                bandera[clienteId - 1] = false; // Indica que el cliente ya no está listo
            }
            break; // Sale del bucle una vez completado el retiro
        }
    }
}

// Clase que representa un cliente que intenta retirar dinero
class Clientev4 extends Thread {
    private final CuentaBancariav4 cuenta; // Referencia a la cuenta bancaria
    private final int cantidad;
    private final int id;

    public Clientev4(CuentaBancariav4 cuenta, int cantidad, int id) {
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
public class BancoDekkerv4 {
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
