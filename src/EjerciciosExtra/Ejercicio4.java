package EjerciciosExtra;

import javax.crypto.spec.PSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejercicio4 {
    static int threadNum = 10;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("CountDown");
        mainCountDown();
        System.out.println("Atomic");
        mainAtomic();
        System.out.println("Integer");
        mainInteger();
    }

    public static void mainCountDown() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            Thread th = new CountDownThread("" + i);
            th.start();
            threads.add(th);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void mainAtomic() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            Thread th = new AtomicThread("" + i);
            th.start();
            threads.add(th);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void mainInteger() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            Thread th = new IntegerThread("" + i);
            th.start();
            threads.add(th);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}

class CountDownThread extends Thread {
    static Integer globalIterations = 10;
    static int parties = 5;
    // Disponemos de tres opciones (de clase) CountDownLatch, CyclicBarrier, AtomicInteger.
    // CyclicBarrier no es válida... se bloquea el hilo en ejecución.
    // CountDownLatch se puede... pero como si fuese un integer
    private CountDownLatch countDown;
    Integer iterations = 0;

    CountDownThread(String name) {
        super(name);
        this.createCountDown();
    }

    @Override
    public void run() {
        while (iterations < globalIterations && !interrupted()) {
            iterations++;
            this.printIterations();
            this.countDown();
        }
    }

    protected void printIterations() {
        System.out.println(iterations + " - " + currentThread().getName());
    }

    protected void createCountDown() {
        countDown = new CountDownLatch(parties);
    }

    protected void countDown() {
        this.countDown.countDown();
        if (this.countDown.getCount() == 0) {
            this.printIterations();
            this.createCountDown();
        }

    }
}

class AtomicThread extends Thread {
    static Integer globalIterations = 10;
    static int parties = 5;
    // Segunda opción AtomicInteger
    AtomicInteger atomicInteger;
    Integer iterations = 0;


    AtomicThread(String name) {
        super(name);
        atomicInteger = new AtomicInteger(parties);
    }

    @Override
    public void run() {
        while (iterations < globalIterations && !interrupted()) {
            iterations++;
            this.printIterations();
            this.countDown();
        }
    }

    protected void resetCounter() {
        // Cambiamos el atomic integer
        this.atomicInteger.set(parties);
    }

    protected void countDown() {
        try {
            // Dormir un poco, trabajan muy rápido
            // Para ver el funcionamiento
            sleep(100);
        } catch (InterruptedException e) {
            this.interrupt();
        }

        // Decrementar y comprobar
        // También se pueden plantear con un número
        if (this.atomicInteger.decrementAndGet() == 0) {
            this.printIterations();
            this.resetCounter();
        }
    }

    protected void printIterations() {
        System.out.println(iterations + " - " + currentThread().getName());
    }
}

class IntegerThread extends Thread {
    static Integer globalIterations = 10;
    static int parties = 5;
    Integer iterations = 0;


    IntegerThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        while (iterations < globalIterations && !interrupted()) {
            iterations++;
            this.printIterations();
            this.countDown();
        }
    }

    protected void countDown() {
        try {
            // Dormir un poco, trabajan muy rápido
            // Para ver el funcionamiento
            sleep(100);
        } catch (InterruptedException e) {
            this.interrupt();
        }

        // Comprobar si se tiene que volver a imprimir
        if ((iterations % parties) == 0) {
            this.printIterations();
        }
    }

    protected void printIterations() {
        System.out.println(iterations + " - " + currentThread().getName());
    }
}

