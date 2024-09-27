public class BarDekkerv3 implements Runnable {
    private static volatile boolean[] interesado = {false, false}; // Interés de cada borracho
    private static volatile int turno = 0; // Controla de quién es el turno
    private static final Object lock = new Object(); // Objeto para la sincronización

    public void borracho1() {
        while (true) {
            synchronized (lock) {
                interesado[0] = true; // Borracho 1 muestra interés

                // Espera si Borracho 2 está interesado
                while (interesado[1] && turno == 1) {
                    try {
                        lock.wait(); // Espera sin consumir recursos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                        return; // Sale del método si es interrumpido
                    }
                }

                // Sección crítica
                usarBano(1); // Borracho 1 usa el baño

                // Sale de la sección crítica
                interesado[0] = false; // Indica que Borracho 1 ya no está interesado
                turno = 1; // Cede el turno al Borracho 2
                lock.notifyAll(); // Notifica a otros hilos que pueden intentar entrar
            }
        }
    }

    public void borracho2() {
        while (true) {
            synchronized (lock) {
                interesado[1] = true; // Borracho 2 muestra interés

                // Espera si Borracho 1 está interesado
                while (interesado[0] && turno == 0) {
                    try {
                        lock.wait(); // Espera sin consumir recursos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                        return; // Sale del método si es interrumpido
                    }
                }

                // Sección crítica
                usarBano(2); // Borracho 2 usa el baño

                // Sale de la sección crítica
                interesado[1] = false; // Indica que Borracho 2 ya no está interesado
                turno = 0; // Cede el turno al Borracho 1
                lock.notifyAll(); // Notifica a otros hilos que pueden intentar entrar
            }
        }
    }

    public void usarBano(int borrachoId) {
        // Simula que el borracho está usando el baño
        System.out.println("Borracho " + borrachoId + " está usando el baño.");
        try {
            Thread.sleep(1000); // Simula el tiempo que el borracho usa el baño
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
            System.out.println("Borracho " + borrachoId + " fue interrumpido.");
            return; // Sale del método si es interrumpido
        }
        System.out.println("Borracho " + borrachoId + " ha terminado de usar el baño.");
    }

    public static void main(String[] args) {
        BarDekkerv3 bar = new BarDekkerv3();
        Thread borracho1 = new Thread(bar, "Borracho 1");
        Thread borracho2 = new Thread(bar, "Borracho 2");

        borracho1.start();
        borracho2.start();
    }

    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("Borracho 1")) {
            borracho1();
        } else {
            borracho2();
        }
    }
}
