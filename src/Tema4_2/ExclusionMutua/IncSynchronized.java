package Tema4_2.ExclusionMutua;

import java.util.ArrayList;
import java.util.List;

public class IncSynchronized {

    private static double x = 0;
    private static Object xLock = new Object();

    public static void inc() {
        for (int i = 0; i < 10000000; i++) {
            synchronized (xLock) {
                x = x + 1;
                System.out.println(Thread.currentThread().getName()+": "+x);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
    //Crear 3 hilos que ejecutan inc() y esperar a que acaben
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(()->inc(),"t"+i));
        }
        for (int i = 0; i < 4; i++) {
            ts.get(i).start();
        }
        for (int i = 0; i < 4; i++) {
            ts.get(i).join();
        }
        System.out.println("x:" + x);
    }
}
