public class BarDekkerv4 implements Runnable {
    private static boolean[] interesado = {false, false}; // Indica el interés de cada borracho
    private static int turno = 0; // Variable de turno/prioridad
    private int borrachoIndex; // Índice del borracho (0 o 1)

    public BarDekkerv4(int borrachoIndex) {
        this.borrachoIndex = borrachoIndex;
    }

    @Override
    public void run() {
        int otro = 1 - borrachoIndex; // Índice del otro borracho
        while (true) {
            interesado[borrachoIndex] = true; // Muestra interés
            while (interesado[otro] && turno == otro) {
                interesado[borrachoIndex] = false; // Cede el turno si es del otro
                while (turno == otro) {
                    // Espera ocupada hasta que cambie el turno
                }
                interesado[borrachoIndex] = true; // Recupera el interés
            }

            // Sección crítica
            usarBano(borrachoIndex + 1);

            // Al salir, cede la prioridad
            turno = otro; // Cede la prioridad
            interesado[borrachoIndex] = false; // Sale de la sección crítica

            // Opción para permitir un ligero retraso
            try {
                Thread.sleep(10); // Pequeño retraso antes de que el otro borracho intente acceder
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    // Simulación de uso del baño
    private void usarBano(int borrachoId) {
        System.out.println("Borracho " + borrachoId + " está usando el baño.");
        try {
            Thread.sleep(1000); // Simula el tiempo en el baño
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Borracho " + borrachoId + " ha terminado de usar el baño.");
    }

    public static void main(String[] args) {
        Thread borracho1 = new Thread(new BarDekkerv4(0), "Borracho 1");
        Thread borracho2 = new Thread(new BarDekkerv4(1), "Borracho 2");

        borracho1.start();
        borracho2.start();
    }
}
