public class BancoDekkerv4 {
    private int saldo = 100;
    private static boolean[] interesado = {false, false}; // Clientes muestran interés
    private static int turno = 0; // Prioridad entre clientes

    // Cliente 1 intenta retirar dinero
    public void cliente1(int cantidad) {
        while (true) {
            interesado[0] = true; // Cliente 1 muestra interés
            while (interesado[1]) {
                if (turno == 1) {
                    interesado[0] = false; // Cede el turno
                    while (turno == 1) {
                        // Espera ocupada
                    }
                    interesado[0] = true; // Recupera el interés
                }
            }

            // Sección crítica: retirar dinero
            if (saldo >= cantidad) {
                System.out.println("Cliente 1 retira: " + cantidad);
                saldo -= cantidad;
                System.out.println("Saldo después de Cliente 1: " + saldo);
            } else {
                System.out.println("Cliente 1 no puede retirar. Saldo insuficiente.");
            }

            turno = 1; // Da la prioridad a Cliente 2
            interesado[0] = false; // Sale de la sección crítica
            break;
        }
    }

    // Cliente 2 intenta retirar dinero
    public void cliente2(int cantidad) {
        while (true) {
            interesado[1] = true; // Cliente 2 muestra interés
            while (interesado[0]) {
                if (turno == 0) {
                    interesado[1] = false; // Cede el turno
                    while (turno == 0) {
                        // Espera ocupada
                    }
                    interesado[1] = true; // Recupera el interés
                }
            }

            // Sección crítica: retirar dinero
            if (saldo >= cantidad) {
                System.out.println("Cliente 2 retira: " + cantidad);
                saldo -= cantidad;
                System.out.println("Saldo después de Cliente 2: " + saldo);
            } else {
                System.out.println("Cliente 2 no puede retirar. Saldo insuficiente.");
            }

            turno = 0; // Da la prioridad a Cliente 1
            interesado[1] = false; // Sale de la sección crítica
            break;
        }
    }
}
