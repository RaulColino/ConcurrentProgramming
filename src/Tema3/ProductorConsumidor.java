package Tema3;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProductorConsumidor {

    private static final int T_BUFFERCIRCULAR = 8;

    private static Semaphore nHuecos; /*num huecos disponibles del buffer circular*/
    private static Semaphore nProductos;/*num de productos disponibles en el buffer circular*/
    private static Semaphore semAccesoBuffer;/*solo puede acceder un proceso a la vez al buffer (excl. mutua)*/

    /* No es necesario utilizar volatile si los hilos utilizan alguna herramienta de sincronización
    •Por ejemplo:
     § Si los atributos compartidos están bajo exclusión mutua  con un semáforo, se garantiza que se leerán los valores correctos
     § Si un proceso escribe un atributo y desbloquea a otro proceso, el otro proceso leerá el valor escrito
     */
    private static boolean[] bufferCircular;
    private static int posInsertar = 0;/*solo puede acceder a esta variable un proceso a la vez (excl. mutua)*/
    private static int posSacar = 0;/*solo puede acceder a esta variable un proceso a la vez (excl. mutua)*/

    private static void sleep(int bound) throws InterruptedException {
        Thread.sleep(new Random().nextInt(bound));
    }

    public void inicializarBuffer() {
        for (int i = 0; i < T_BUFFERCIRCULAR; i++) {
            bufferCircular[i] = false;
        }
    }

    public static void productor() {
        while (true) {
            try {
                sleep(100);
                System.out.println("productor quiere producir...");
                nHuecos.acquire(); /*Si hay 0 huecos espera a que haya uno*/
                semAccesoBuffer.acquire();
                /*seccion critica*/
                bufferCircular[posInsertar] = true;
                System.out.println("producido elem " + posInsertar + ": " + Arrays.toString(bufferCircular));
                posInsertar = (posInsertar + 1) % T_BUFFERCIRCULAR;
                /*end seccion critica*/
                semAccesoBuffer.release();
                nProductos.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void consumidor() {
        while (true) {
            try {
                sleep(100);
                System.out.println("consumidor quiere consumir...");
                nProductos.acquire(); /*Si hay 0 productos espera a que haya uno*/
                semAccesoBuffer.acquire();
                /*seccion critica*/
                bufferCircular[posSacar] = false;
                System.out.println("consumido elem " + posSacar + ": " + Arrays.toString(bufferCircular));
                posSacar = (posSacar + 1) % T_BUFFERCIRCULAR;
                /*end seccion critica*/
                semAccesoBuffer.release();
                nHuecos.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        bufferCircular = new boolean[T_BUFFERCIRCULAR];
        nHuecos = new Semaphore(T_BUFFERCIRCULAR);
        nProductos = new Semaphore(0);
        semAccesoBuffer = new Semaphore(1);
        System.out.println("buffer inicial:" + Arrays.toString(bufferCircular));
        new Thread(() -> productor(), "prod").start();
        new Thread(() -> consumidor(), "cons").start();
    }


}
