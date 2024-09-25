import java.util.Random;
public class NombreHilo implements Runnable {
    private String palabra;

    //Constructor para recibir una palabra.
    public NombreHilo(String palabra) {
        this.palabra = palabra;
    }


    @Override
    public void run() {
        try {
            Random random = new Random();
            int tiempoAleatorio = random.nextInt(4000);
            Thread.sleep(tiempoAleatorio);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println(palabra);
    }

    public static void main(String[] args) {
        String nombreCompleto = "Luis Alberto Velázquez Carballo";

        String[] palabras = nombreCompleto.split(" ");

        for (String palabra : palabras) {
            Thread hilo = new Thread(new NombreHilo(palabra));
            hilo.start();
        }
    }
}
