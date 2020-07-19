package Tema3;

import java.util.concurrent.Semaphore;

public class SincronizacionCondicional1Sem {

    static private Semaphore semContinuarA;
    static private Semaphore semContinuarD;
    static private Semaphore semContinuarE;

    public static void proc1() {
        new Thread(() -> {
            try {
                semContinuarA.acquire(); //permisos-1 = -1 y por lo tanto se bloquea
                System.out.println("A");
                System.out.println("B");
                semContinuarE.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "proc1").start();
    }

    public static void proc2() {
        new Thread(() -> {
            try {
                System.out.println("C");
                semContinuarA.release();
                semContinuarD.acquire(); //permisos-1 = -1 y por lo tanto se bloquea
                System.out.println("D");
                semContinuarE.acquire(); //permisos-1 = -2 y por lo tanto se bloquea hasta 2 release()
                System.out.println("E");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "proc2").start();


    }

    public static void proc3() {
        new Thread(() -> {
            System.out.println("F");
            semContinuarD.release();
            System.out.println("G");
            semContinuarE.release();
        }, "proc3").start();

    }

    public static void main(String[] args) {
        semContinuarA = new Semaphore(0); //0 permisos
        semContinuarD = new Semaphore(0); //0 permisos
        semContinuarE = new Semaphore(-1); //tras hacer semContinuarE.acquire() necesita 2 release()
        proc1();
        proc2();
        proc3();
    }
}
