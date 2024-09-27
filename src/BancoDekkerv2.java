public class BancoDekkerv2 {
    private int saldo = 100;
    private static int turno = 0;
    private static boolean[] interesado = {false, false};

    public void cliente1(int cantidad) {
        while (true) {
            interesado[0] = true; // Cliente 1 muestra interés
            while (interesado[1] && turno == 1) {
                // Espera ocupada
            }

            // Sección crítica: retirar dinero
            if (saldo >= cantidad) {
                System.out.println("Cliente 1 retira: " + cantidad);
                saldo -= cantidad;
                System.out.println("Saldo después de Cliente 1: " + saldo);
            } else {
                System.out.println("Cliente 1 no puede retirar. Saldo insuficiente.");
            }

            interesado[0] = false;
            turno = 1; // Cede el turno a Cliente 2
            break;
        }
    }

    public void cliente2(int cantidad) {
        while (true) {
            interesado[1] = true; // Cliente 2 muestra interés
            while (interesado[0] && turno == 0) {
                // Espera ocupada
            }

            // Sección crítica: retirar dinero
            if (saldo >= cantidad) {
                System.out.println("Cliente 2 retira: " + cantidad);
                saldo -= cantidad;
                System.out.println("Saldo después de Cliente 2: " + saldo);
            } else {
                System.out.println("Cliente 2 no puede retirar. Saldo insuficiente.");
            }

            interesado[1] = false;
            turno = 0; // Cede el turno a Cliente 1
            break;
        }
    }
}
