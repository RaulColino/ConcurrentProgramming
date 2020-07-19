package Tema3;

import java.util.concurrent.Semaphore;

public class EjercicioMuseoInfinito {

    static private Semaphore sem;
    public static final int Personas = 3;
    public static volatile int contador;

    public static void visit() {
        while (true) {
            try {
                sem.acquire();
                contador++;
                System.out.println("hola somos " + contador + " personas");
                sem.release();

                System.out.println("que bonito");
                System.out.println("adios");

                sem.acquire();
                contador--;
                sem.release();

                System.out.println("paseo");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        sem = new Semaphore(1);
        contador = 0;
        for (int i = 0; i < Personas; i++) {
            new Thread(() -> visit()).start();
        }
    }
}
