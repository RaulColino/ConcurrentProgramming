package Tema4_3.Ej2_BlockingQueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdConsBlockingQueue {

    private static final int NPRODUTORES = 5;
    private static final int NCONSUMIDORES = 5;
    private static BlockingQueue queueBuffer = new ArrayBlockingQueue(10);
    /*static Lock l = new ReentrantLock();*/


    private static void sleep(int bound) throws InterruptedException {
        Thread.sleep(new Random().nextInt(bound));
    }

    public static void productor() {
        while (true) {
            try {
                sleep(1000);
                String producto = "'" + Thread.currentThread().getName() + " -> Producto " + new Random().nextInt(1000) + "'";
                queueBuffer.put(producto); /*Si no hay espacio se bloquea hasta que haya un hueco libre para poner el nuevo producto*/
                System.out.println("PRODUCTOR " + Thread.currentThread().getName() + " produjo: " + producto);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void consumidor() {
        while (true) {
            try {
                sleep(1000);
                System.out.println("CONSUMIDOR " + Thread.currentThread().getName() + " consumio: " + queueBuffer.take()); /*no imprime hasta que hoya disponible algo para consumir*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < NPRODUTORES; i++) {
            new Thread(() -> productor(), "Productor " + i).start();
        }
        for (int i = 0; i < NCONSUMIDORES; i++) {
            new Thread(() -> consumidor(), "Consumidor " + i).start();
        }
    }

}
