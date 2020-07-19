package EjerciciosExtra;

import java.util.Random;
import java.util.concurrent.*;

public class Ejercicio3 {
    static int total = 20;

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(10);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < total; i++) {
            int finalI = i;
            Runnable runnable = () -> {
                if (random.nextBoolean()) {
                    try {
                        // Productores
                        int producto = finalI;
                        System.out.println("Fabrico: " + producto);
                        boolean estado = blockingQueue.offer(producto, 1, TimeUnit.SECONDS);
                        if (estado) {
                            System.out.println("Entregado: " + producto);
                        } else {
                            System.out.println("NO Entregado: " + producto);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Problema con " + finalI);
                    }
                } else {
                    try {
                        Integer producto = blockingQueue.poll(1, TimeUnit.SECONDS);
                        if (producto != null) {
                            System.out.println("Tenemos el producto " + producto + " " + finalI);
                        } else {
                            System.out.println("Problema con el producto " + producto + " " + finalI);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Problema con consumidor" + finalI);
                    }
                }
            };
            executorService.submit(runnable);
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        if (blockingQueue.isEmpty()){
            System.out.println("Todo entregado");
        }else{
            System.out.println("Problema con los productos:");
            blockingQueue.forEach(System.out::println);
        }

    }

}
