package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConSynchronized.MonitoresProdCons;

import java.util.LinkedList;
import java.util.Queue;

public class BufferMonitor {

    Queue<Integer> buffer;

    public BufferMonitor() {
        buffer = new LinkedList<>();
    }

    /*synchronized: cuando un hilo se bloquea en uno de estos metodos, se libera la exclusión mutua de forma automática*/

    public synchronized void produce(int value) throws InterruptedException {
        while (buffer.size() == 10) {
            System.out.println("buffer lleno: " + Thread.currentThread().getName() + " esperando");
            this.wait();
        }
        buffer.add(value);
        System.out.println("valor " + value + " producido por: " + Thread.currentThread().getName());
        this.notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            System.out.println("buffer vacio: " + Thread.currentThread().getName() + " esperando");
            this.wait();
        }
        int value = buffer.poll();
        System.out.println("valor " + value + " consumido por: " + Thread.currentThread().getName());
        this.notifyAll();
    }


}
