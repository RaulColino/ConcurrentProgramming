package Tema5_1_ThreadPools.CodigosProfe.ReservaAviones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ReservaAviones1 {
    static int numero_aviones = 5;
    static int threads = 5;

    public static void main(String[] args) throws InterruptedException {
        List<Semaphore> aviones = new CopyOnWriteArrayList<>();
        //LinkedBlockingQueue<List<Integer>> reservas = new LinkedBlockingQueue<>(30);
        ConcurrentLinkedQueue<List<Integer>> reservas = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < numero_aviones; i++) {
            aviones.add(new Semaphore(i, true));
        }

        reservas.offer(Arrays.asList(1));
        reservas.offer(Arrays.asList(1));
        reservas.offer(Arrays.asList(1));
        reservas.offer(Arrays.asList(1, 2));
        reservas.offer(Arrays.asList(1, 2));
        reservas.offer(Arrays.asList(2));
        reservas.offer(Arrays.asList(2, 3));
        reservas.offer(Arrays.asList(1, 2, 3));
        reservas.offer(Arrays.asList(4));
        reservas.offer(Arrays.asList(4));
        reservas.offer(Arrays.asList(4));
        reservas.offer(Arrays.asList(4));
        reservas.offer(Arrays.asList(0));

        List<Thread> thl = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread th = new Thread(() -> {
                System.out.println("Thread empezando " + Thread.currentThread().getName());
                List<Semaphore> semaphoros_reservados = new ArrayList<>();
                List<Integer> reserva = null;
                do {
                    try {
                        reserva = reservas.poll();
                        //reserva = reservas.poll(2, TimeUnit.SECONDS);
                        if (reserva == null) continue;
                        for (Integer avion : reserva) {
                            boolean permit = aviones.get(avion).tryAcquire(2, TimeUnit.SECONDS);
                            if (!permit) {
                                throw new InterruptedException();
                            }
                            semaphoros_reservados.add(aviones.get(avion));
                        }
                        System.out.println("Reservado " + reserva + " por " + Thread.currentThread().getName());
                        semaphoros_reservados.clear();
                    } catch (InterruptedException e) {
                        System.out.println("Liberando reserva " + reserva + " por " + Thread.currentThread().getName());
                        for (Semaphore semaphoros_reservado : semaphoros_reservados) {
                            semaphoros_reservado.release();
                        }
                    }
                } while (reserva != null && !Thread.interrupted());
                System.out.println("Thread parando " + Thread.currentThread().getName());
            });
            thl.add(th);
            th.start();
        }

        for (Thread thread : thl) {
            thread.join();
        }

        System.out.println(reservas);
    }
}
