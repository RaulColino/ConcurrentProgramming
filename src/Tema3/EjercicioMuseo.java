package Tema3;

import java.util.concurrent.Semaphore;

public class EjercicioMuseo {

    static private Semaphore sem;
    static final int Personas = 3;

    public static void visit(){
        while(true){
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hola!");
            System.out.println("que bonito");
            System.out.println("adios");
            sem.release();
            System.out.println("paseo");
        }
    }

    public static void main(String[] args) {
        sem = new Semaphore(1);
        for (int i = 0; i < Personas; i++) {
            new Thread(()->visit()).start();
        }
    }
}
