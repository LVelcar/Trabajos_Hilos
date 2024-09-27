public class BarDekkerv3 implements Runnable {
    private static boolean[] interesado = {false, false}; // Interés de cada borracho
    private static int turno = 0; // Controla de quién es el turno
    private int id; // Identificador del borracho

    public BarDekkerv3(int id) {
        this.id = id; // 0 para Borracho 1, 1 para Borracho 2
    }
    private void borracho1() {
        while (true) {
            interesado[0] = true; // Borracho 1 muestra interés

            // Espera si el Borracho 2 está interesado y es su turno
            while (interesado[1]) {
                if (turno == 1) {
                    interesado[0] = false; // Borracho 1 cede el turno
                    // Espera a que Borracho 2 complete su turno
                    while (turno == 1) {
                        // Espera activa hasta que cambie el turno
                    }
                    interesado[0] = true; // Borracho 1 vuelve a mostrar interés
                }
            }

            // Sección crítica: Borracho 1 usa el baño
            usarBano(1);

            // Sale de la sección crítica
            interesado[0] = false; // Borracho 1 ya no está interesado
            turno = 1; // Cede el turno al Borracho 2
        }
    }

    private void borracho2() {
        while (true) {
            interesado[1] = true; // Borracho 2 muestra interés

            // Espera si el Borracho 1 está interesado y es su turno
            while (interesado[0]) {
                if (turno == 0) {
                    interesado[1] = false; // Borracho 2 cede el turno
                    // Espera a que Borracho 1 complete su turno
                    while (turno == 0) {
                        // Espera activa hasta que cambie el turno
                    }
                    interesado[1] = true; // Borracho 2 vuelve a mostrar interés
                }
            }

            // Sección crítica: Borracho 2 usa el baño
            usarBano(2);

            // Sale de la sección crítica
            interesado[1] = false; // Borracho 2 ya no está interesado
            turno = 0; // Cede el turno al Borracho 1
        }
    }

    private void usarBano(int borrachoId) {
        // Simula que el borracho está usando el baño
        System.out.println("Borracho " + borrachoId + " está usando el baño.");
        try {
            Thread.sleep(1000); // Simula el tiempo que el borracho usa el baño
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
            System.out.println("Borracho " + borrachoId + " fue interrumpido.");
        }
        System.out.println("Borracho " + borrachoId + " ha terminado de usar el baño.");
    }
    @Override
    public void run() {
        if (id == 0) {
            borracho1();
        } else {
            borracho2();
        }
    }

    public static void main(String[] args) {
        Thread borracho1 = new Thread(new BarDekkerv3(0), "Borracho 1");
        Thread borracho2 = new Thread(new BarDekkerv3(1), "Borracho 2");

        borracho1.start();
        borracho2.start();
    }
}