package Tema4_2.SincronizacionCondicional.Exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

/*
Aunque el intercambio es bidireccional, esta clase permite implementar un esquema de
productor/consumidor SIN BUFFER (usando sólo un sentido de la comunicación):
• El primer hilo que ejecuta exchange() queda bloqueado hasta que el otro hilo ejecuta también ese método
• Cuando el segundo hilo ejecuta exchange() ambos hilos intercambian los valores pasados como parámetro y
continúan su ejecución
 */

public class ProdConsExchanger {

    private static final int N_PRODUCTS= 10; //se crean y consumen 10 productos

    private static Exchanger<Double> exchanger = new Exchanger<Double>();
    private static Random random = new Random();

    public static void producer() {
        try {
            double productoL = 0;
            for (int i = 0; i < N_PRODUCTS; i++) {
                Thread.sleep(1000);
                productoL = random.nextDouble();
                System.out.println("Producto producido: "+productoL);
                System.out.println("Productor esperando consumicion del producto...");
                exchanger.exchange(productoL); //El productor queda bloqueado hasta que el consumidor obtiene el valor
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void consumer() {
        try {
            for (int i = 0; i < N_PRODUCTS; i++) {
                System.out.println("Consumidor esperando producto...");
                double producto = exchanger.exchange(null); //El consumidor queda bloqueado hasta que el productor ha producido el valor
                System.out.println("Producto consumido: " + producto);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //Se crean e inician dos hilos que llaman
        //a productor() y consumidor()
        List<Thread> ts = new ArrayList<>();

        ts.add(new Thread(() -> producer(), "Producer"));
        ts.add(new Thread(() -> consumer(), "Producer"));

        for (Thread t : ts) {
            t.start();
        }
        for (Thread t:ts) {
            t.join();
        }
    }
}
