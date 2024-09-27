public class BancoDekkerv1 {
    private int saldo = 100;
    private static int turno = 0; // Alternancia entre los dos clientes

    // Cliente 1 intenta retirar dinero
    public void cliente1(int cantidad) {
        while (true) {
            // Espera si no es su turno
            while (turno != 0) {
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

            // Cede el turno a Cliente 2
            turno = 1;
            break;
        }
    }

    // Cliente 2 intenta retirar dinero
    public void cliente2(int cantidad) {
        while (true) {
            // Espera si no es su turno
            while (turno != 1) {
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

            // Cede el turno a Cliente 1
            turno = 0;
            break;
        }
    }
}
