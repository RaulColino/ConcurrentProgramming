package Tema3;

/*
p1 -->A-->B------>
       \ \
p2 -->C-\->D------>
       \ \
p3 -->E-->F------>
 */

import java.util.concurrent.Semaphore;

public class SincronizacionCond2Sem {

    static private Semaphore semContinuarD;
    static private Semaphore semContinuarF;

    public static void proc1(){
        new Thread(()->{
            System.out.println("A");
            semContinuarD.release();
            semContinuarF.release();
            System.out.println("B");
        },"proc1").start();
    }

    public static void proc2(){
        new Thread(()->{
            System.out.println("C");
            semContinuarF.release();
            try {
                semContinuarD.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("D");
        },"proc2").start();
    }

    public static void proc3(){
        new Thread(()->{
            System.out.println("E");
            try {
                semContinuarF.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("F");
        },"proc3").start();
    }

    public static void main(String[] args) {
        semContinuarD = new Semaphore(0);
        semContinuarF = new Semaphore(-1);
        proc1();
        proc2();
        proc3();
    }
}
