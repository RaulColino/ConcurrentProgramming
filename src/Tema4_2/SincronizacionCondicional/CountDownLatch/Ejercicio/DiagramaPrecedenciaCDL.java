package Tema4_2.SincronizacionCondicional.CountDownLatch.Ejercicio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/*
CountDownLatch ejercicio pag 51
 */
public class DiagramaPrecedenciaCDL {

    private static CountDownLatch latchD;
    private static CountDownLatch latchB;
    private static CountDownLatch latchG;
    private static CountDownLatch latchH;
    private static CountDownLatch latchE;
    private static CountDownLatch latchC;

    /*Proceso 1*/
    public static void proc1() {
        try {
            System.out.println("A");
            latchD.countDown();
            latchB.await();
            System.out.println("B");
            latchH.countDown();
            latchE.countDown();
            latchC.await();
            System.out.println("C");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*Proceso 2*/
    public static void proc2() {
        try {
            latchD.await(); /*Espera a que se ejecute A y F para imprimir D*/
            System.out.println("D");
            latchB.countDown();
            latchG.countDown();
            latchE.await();
            System.out.println("E");
            latchC.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*Proceso 3*/
    public static void proc3() {
        try {
            System.out.println("F");
            latchD.countDown();
            latchG.await();
            System.out.println("G");
            latchH.await();
            System.out.println("H");
            latchE.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        latchD = new CountDownLatch(2);
        latchB = new CountDownLatch(1);
        latchG = new CountDownLatch(1);
        latchH = new CountDownLatch(1);
        latchE = new CountDownLatch(2);
        latchC = new CountDownLatch(1);

        List<Thread> ts = new ArrayList<>();

        ts.add(new Thread(() -> proc1(), "Proceso1"));
        ts.add(new Thread(() -> proc2(), "Proceso2"));
        ts.add(new Thread(() -> proc3(), "Proceso3"));

        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }
}
