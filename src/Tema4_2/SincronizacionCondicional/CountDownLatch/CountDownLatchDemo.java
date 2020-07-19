package Tema4_2.SincronizacionCondicional.CountDownLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/*
Un objeto de la clase CountDownLatch permite implementar una cuenta atrás:
•Uno o más hilos invocan await() y eso le/s bloquea a la espera de que le/s
 den la salida.
•La salida de la carrera (el desbloqueo de los hilos) se produce cuando se invoca
 countDown() tantas veces como se haya especificado en el constructor del objeto
 */

public class CountDownLatchDemo {

    private static final int NRUNNERS = 3;
    private static final int NJUDGES = 1;

    /*Se crea el objeto indicando el número de countDown necesarios para desbloquear a los await()*/
    static CountDownLatch latch = new CountDownLatch(4);

    public static void runner() {
        try {
            System.out.println("Ready "+Thread.currentThread().getName());
            latch.await();
            System.out.println("Running "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void judge() {
        try {
            for (int i = 3; i >= 0; i--) {
                System.out.println(i);
                latch.countDown();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //Create and start 3 threads executing runner()
        //Create and start 1 thread executing judge()

        List<Thread> ts = new ArrayList<>();

        for (int i = 0; i < NRUNNERS; i++) {
            ts.add(new Thread(() -> runner(), "Runner " + (i + 1)));
        }
        for (int i = 0; i < NJUDGES; i++) {
            ts.add(new Thread(() -> judge(), "Judge " + (i + 1)));
        }
        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }

}
