public class BarDekkerv1 implements Runnable {
    private static volatile boolean[] quiereEntrar = new boolean[2]; // Indica si un borracho quiere entrar
    private static volatile int turno = 0; // Control de turno

    // Método que se ejecutará en el hilo
    @Override
    public void run() {
        // Determina si este hilo es el borracho 1 o 2
        if (Thread.currentThread().getName().equals("Borracho 1")) {
            borracho1();
        } else {
            borracho2();
        }
    }

    // Borracho 1 quiere usar el baño
    public void borracho1() {
        while (true) {
            quiereEntrar[0] = true; // Borracho 1 quiere entrar
            while (quiereEntrar[1]) { // Si el borracho 2 también quiere entrar
                if (turno == 1) { // Si es el turno del borracho 2
                    quiereEntrar[0] = false; // No puede entrar
                    while (turno == 1) { // Espera hasta que el borracho 2 termine
                    }
                    quiereEntrar[0] = true; // Vuelve a querer entrar
                }
            }

            // Sección crítica: usar el baño
            usarBano(1);

            // Cede el turno al borracho 2
            turno = 1;
            quiereEntrar[0] = false; // Ya no quiere entrar
        }
    }

    // Borracho 2 quiere usar el baño
    public void borracho2() {
        while (true) {
            quiereEntrar[1] = true; // Borracho 2 quiere entrar
            while (quiereEntrar[0]) { // Si el borracho 1 también quiere entrar
                if (turno == 0) { // Si es el turno del borracho 1
                    quiereEntrar[1] = false; // No puede entrar
                    while (turno == 0) { // Espera hasta que el borracho 1 termine
                    }
                    quiereEntrar[1] = true; // Vuelve a querer entrar
                }
            }

            // Sección crítica: usar el baño
            usarBano(2);

            // Cede el turno al borracho 1
            turno = 0;
            quiereEntrar[1] = false; // Ya no quiere entrar
        }
    }

    // Simulación de uso del baño
    public void usarBano(int borrachoId) {
        System.out.println("Borracho " + borrachoId + " está usando el baño.");
        try {
            Thread.sleep(1000); // Simula el uso del baño
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Borracho " + borrachoId + " ha terminado de usar el baño.");
    }

    // Main para iniciar los hilos de los borrachos
    public static void main(String[] args) {
        BarDekkerv1 bar = new BarDekkerv1();

        // Crear y ejecutar los hilos para los dos borrachos
        Thread borracho1Thread = new Thread(bar, "Borracho 1");
        Thread borracho2Thread = new Thread(bar, "Borracho 2");

        // Iniciar los hilos
        borracho1Thread.start();
        borracho2Thread.start();
    }
}
