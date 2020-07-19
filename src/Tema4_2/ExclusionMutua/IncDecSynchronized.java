package Tema4_2.ExclusionMutua;

import java.util.ArrayList;
import java.util.List;

public class IncDecSynchronized {

    private static double x = 0;

    private static Object xLock = new Object();
    public static void inc() {
        for (int i = 0; i < 10000000; i++) {
            synchronized (xLock) {
                x = x + 1;
            }
        }
    }
    public static void dec() {
        for (int i = 0; i < 10000000; i++) {
            synchronized (xLock) {
                x = x - 1;
            }
        }
    }



    public static void main(String[] args) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        ts.add(new Thread(()->inc(),"t1"));
        ts.add(new Thread(()->inc(),"t2"));
        ts.add(new Thread(()->inc(),"t3"));
        ts.add(new Thread(()->inc(),"t4"));
        for (int i = 0; i < 4; i++) {
            Thread.currentThread().join();
        }

        System.out.println("x:" + x);
    }
}
