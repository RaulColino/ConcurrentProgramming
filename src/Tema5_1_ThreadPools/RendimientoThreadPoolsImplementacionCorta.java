package Tema5_1_ThreadPools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RendimientoThreadPoolsImplementacionCorta {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        /*ExecutorService executorService = Executors.newFixedThreadPool(4);*/
        /*ExecutorService executorService = Executors.newCachedThreadPool();*/

        long startTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

        for (int i = 0; i < 1000; i++) {
            int id = i;
            executorService.execute(() -> {
                int n_enteros = 10000;
                List<Integer> listaEnteros = new ArrayList<>(n_enteros);
                for (int j = 0; j < n_enteros; j++) {
                    listaEnteros.add(new Random().nextInt());
                }
                int max = listaEnteros.get(0);
                for (int j = 1; j < n_enteros; j++) {
                    max = (max < listaEnteros.get(j)) ? listaEnteros.get(j) : max;
                }
                System.out.println("Tarea " + id + ": " + "max= " + max);
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(9, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long taskTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - startTime;
        System.out.println("tiempo: " + taskTime);
    }
}
