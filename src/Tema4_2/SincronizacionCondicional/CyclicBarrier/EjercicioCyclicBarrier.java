package Tema4_2.SincronizacionCondicional.CyclicBarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*The major difference between CyclicBarrier and CoundDownLatch is that
CyclicBarrier can be reused (You can not use CountDownLatch once used).*/
public class EjercicioCyclicBarrier {

    static final int NPROCESOS = 3;

    static CyclicBarrier barrier;

    private static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void proceso() {
        while (!Thread.interrupted()) {
            try {
                System.out.println("A");
                /*When 3 parties will call await(),CyclicBarrrierEvent will be triggered and all waiting threads will be released.*/
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
       /* barrier = new CyclicBarrier(NPROCESOS, new Runnable() {
            @Override
            public void run() {
                System.out.println("*");
            }
        });*/

        barrier = new CyclicBarrier(NPROCESOS, () -> {
            System.out.println("*");
        });

        for (int i = 0; i < NPROCESOS; i++)
            new Thread(() -> proceso(), "proceso" + (i + 1)).start();
    }
}
