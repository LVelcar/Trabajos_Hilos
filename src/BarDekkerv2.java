public class BarDekkerv2 implements Runnable {
    private static volatile int turno = 0; // Volatile para la visibilidad
    private static volatile boolean[] interesado = {false, false}; // Indica si los borrachos están interesados

    @Override
    public void run() {
        // Determina si este hilo es el borracho 1 o 2
        if (Thread.currentThread().getName().equals("Borracho 1")) {
            borracho1();
        } else {
            borracho2();
        }
    }

    public void borracho1() {
        while (true) {
            interesado[0] = true; // Borracho 1 muestra interés

            // Espera si es el turno del otro borracho y está interesado
            while (interesado[1]) {
                // Si es el turno del otro borracho, ceder
                if (turno == 1) {
                    interesado[0] = false; // Cede su interés
                    while (turno == 1) {
                        Thread.yield(); // Espera activa
                    }
                    interesado[0] = true; // Muestra interés nuevamente
                }
            }

            // Sección crítica: usar el baño
            usarBano(1);

            // Sale de la sección crítica
            interesado[0] = false; // Indica que ya no quiere entrar
            turno = 1; // Cede el turno a Borracho 2
        }
    }

    public void borracho2() {
        while (true) {
            interesado[1] = true; // Borracho 2 muestra interés

            // Espera si es el turno del otro borracho y está interesado
            while (interesado[0]) {
                // Si es el turno del otro borracho, ceder
                if (turno == 0) {
                    interesado[1] = false; // Cede su interés
                    while (turno == 0) {
                        Thread.yield(); // Espera activa
                    }
                    interesado[1] = true; // Muestra interés nuevamente
                }
            }

            // Sección crítica: usar el baño
            usarBano(2);

            // Sale de la sección crítica
            interesado[1] = false; // Indica que ya no quiere entrar
            turno = 0; // Cede el turno a Borracho 1
        }
    }

    public void usarBano(int borrachoId) {
        System.out.println("Borracho " + borrachoId + " está usando el baño.");
        try {
            Thread.sleep(1000); // Simula el uso del baño
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
            System.out.println("Borracho " + borrachoId + " ha sido interrumpido.");
        }
        System.out.println("Borracho " + borrachoId + " ha terminado de usar el baño.");
    }

    // Main para iniciar los hilos de los borrachos
    public static void main(String[] args) {
        BarDekkerv2 bar = new BarDekkerv2();

        // Crear y ejecutar los hilos para los dos borrachos
        Thread borracho1Thread = new Thread(bar, "Borracho 1");
        Thread borracho2Thread = new Thread(bar, "Borracho 2");

        // Iniciar los hilos
        borracho1Thread.start();
        borracho2Thread.start();
    }
}
