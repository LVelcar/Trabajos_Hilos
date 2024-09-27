public class BarDekkerv4 {
    private static boolean[] interesado = {false, false}; // Interés de los borrachos
    private static int turno = 0; // Prioridad

    // Borracho 1 quiere usar el baño
    public void borracho1() {
        while (true) {
            interesado[0] = true; // Borracho 1 muestra interés
            while (interesado[1]) {
                if (turno == 1) {
                    interesado[0] = false; // Cede el turno
                    while (turno == 1) {
                        // Espera ocupada
                    }
                    interesado[0] = true; // Recupera el interés
                }
            }

            // Sección crítica: usar el baño
            usarBano(1);

            turno = 1; // Da la prioridad al otro borracho
            interesado[0] = false; // Sale de la sección crítica
        }
    }

    // Borracho 2 quiere usar el baño
    public void borracho2() {
        while (true) {
            interesado[1] = true; // Borracho 2 muestra interés
            while (interesado[0]) {
                if (turno == 0) {
                    interesado[1] = false; // Cede el turno
                    while (turno == 0) {
                        // Espera ocupada
                    }
                    interesado[1] = true; // Recupera el interés
                }
            }

            // Sección crítica: usar el baño
            usarBano(2);

            turno = 0; // Da la prioridad al otro borracho
            interesado[1] = false; // Sale de la sección crítica
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
}
