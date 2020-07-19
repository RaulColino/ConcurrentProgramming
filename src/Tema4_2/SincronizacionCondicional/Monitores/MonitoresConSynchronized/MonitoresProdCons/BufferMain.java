package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConSynchronized.MonitoresProdCons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Ejercicio de Productor y Consumidor utilizando monitores.

Productor y consumidor, 10 productores y 10 consumidores
El productor produce valores infinitos que el consumidor
consume de manera infinita.

Se pide que se implemente una clase Test para testear el comportamiento del
monitor y un buffer manager para gestionar el acceso al recurso

El temaño máximo del buffer es 10 y pueden implementarlo como quieran.
Recuerden que el orden de consumicion/produccion:
primero que se produce primero que se consume.

Los hilos creados deben esperar a que todos ejecuten los métodos asignados.
 */
public class BufferMain {

    private static final int NPRODUCERS = 10;
    private static final int NCONSUMERS = 10;
    private static BufferMonitor manager;

    private static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void producer() {
        try {
            Random random = new Random();
            while (true) {
                //sleep(5000);
                manager.produce(random.nextInt());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void consumer() {
        try {
            while (true) {
                //sleep(5000);
                manager.consume();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        manager = new BufferMonitor();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NPRODUCERS; i++) {
            threads.add(new Thread(() -> producer(), "Producer " + (i + 1)));
        }
        for (int i = 0; i < NCONSUMERS; i++) {
            threads.add(new Thread(() -> consumer(), "Consumer " + (i + 1)));
        }
        for (Thread t : threads) {
            t.start();
        }
    }

}
