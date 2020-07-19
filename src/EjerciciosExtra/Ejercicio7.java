package EjerciciosExtra;

import java.util.concurrent.*;

public class Ejercicio7 {
    // Dos hilos -> gestion
    // Un hilo generara tareas 1000 -> for
    // Un hilo espera hasta que tocas acaben -> countdown
    // Segundo hilo informará a las tareas para empezar
    static int n = 1000;
    volatile static boolean inicio = false;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(n);

        // Pruebas
        CountDownLatch count2 = new CountDownLatch(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(n + 1);

        // Hilo tareas
        Thread th = new Thread(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            // Posible soluciones por la barrera
            //ExecutorService executor = Executors.newFixedThreadPool(n);
            //ExecutorService executor = Executors.newCachedThreadPool();
            Semaphore semaphore = new Semaphore(5);
            for (int i = 0; i < n; i++) {
                Runnable run = () -> {
                    // Solución extrema
                    // while(!inicio);

                    // Locura v2 (BUENA Solución)
                    try {
                        count2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Espera barrera
                    //    try {
                    //   cyclicBarrier.await();
                    //   } catch (InterruptedException e) {
                    //   e.printStackTrace();
                    //   } catch (BrokenBarrierException e) {
                    //   e.printStackTrace();
                    //   }

                    // EXTRA:
                    StringBuilder sb = new StringBuilder();

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //sección critita es la construcción
                    sb.append("Hola y adios");


                    // Print antiguo
                    //System.out.println("Hola y adios");
                    semaphore.release();

                    System.out.println(sb.toString());
                    count.countDown();

                };
                executor.submit(run);
            }
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
                // Posibles soluciones:
                //   matar al mismo tiempo los dos hilos
                //   cambiar el tipo de executor
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: executor
        }, "Hilo de las tareas");

        // Hilo gestión
        Thread th2 = new Thread(() -> {
            System.out.println("Inicio");

            // Solución extrema
            //    inicio = true;

            // Solución locura (BUENA solución)
            //count2.countDown();

            //   try {
            //   Thread.sleep(5000);
            //    cyclicBarrier.await();
            //cyclicBarrier.await(10, TimeUnit.SECONDS);
            //    } catch (InterruptedException e) {
            //    e.printStackTrace();
            //    } catch (BrokenBarrierException e) {
            //    e.printStackTrace();
            //    }

            System.out.println("Fin");
            try {
                while (count.getCount() != 0) count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Final");
        }, "Hilo gestion");

        Thread.sleep(20000);

        // Gestión de hilos
        th.start();
        th2.start();


        // sin esperar a nadie
        System.out.println("sección importante");

        th.join();

        // Cuando acabe th
        System.out.println("sección importante");

        th2.join();
        // los hilos acaban

        // Cuando ambos acaben
        System.out.println("sección importante");
    }
}

