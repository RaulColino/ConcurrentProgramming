package Tema5_1_ThreadPools.CodigosProfe.ReservaAviones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ReservaAviones2 {
    static int numero_aviones = 5;
    static int threads = 5;

    public static void main(String[] args) throws InterruptedException {
        List<Semaphore> aviones = new CopyOnWriteArrayList<>();
        Queue<List<Integer>> reservas = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < numero_aviones; i++) {
            aviones.add(new Semaphore(i, true));
        }

        // Añadimos los vuelos
        reservas.add(Arrays.asList(0));
        reservas.add(Arrays.asList(1, 2, 0));

        List<Thread> thl = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            // Hilo que reserva
            Thread th = new Thread(() -> {
                while (!Thread.interrupted()) {
                    List<Integer> aviones_reservados = null;
                    try {
                        List<Integer> reserva = reservas.poll();
                        if (reserva == null)
                            break;

                        aviones_reservados = new ArrayList<>();
                        for (Integer avion : reserva) {
                            boolean permiso = false;
                            permiso = aviones.get(avion).tryAcquire(2, TimeUnit.SECONDS);
                            if (!permiso) {
                                // Liberar
                                throw new InterruptedException();
                            }
                            aviones_reservados.add(avion);
                        }
                        System.out.println("Reserva " + reserva + " por "+ Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        System.out.println("Liberamos " + aviones_reservados + " por "+ Thread.currentThread().getName());
                        for (Integer aviones_reservado : aviones_reservados) {
                            aviones.get(aviones_reservado).release();
                        }
                    }
                }

            });
            th.start();
            thl.add(th);
        }

        for (Thread thread : thl) {
            thread.join();
        }
    }
}
