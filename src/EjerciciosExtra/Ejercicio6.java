package EjerciciosExtra;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Ejercicio6 {
    static int tareasNum = 20;
    static int capacidad = 10;
    static long tiempoEspera = 2;
    static TimeUnit unidadesEspera = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        // Depende del tipo del Executor funcionará de una forma u otra.
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Random random = new Random();
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(capacidad);
        List<Integer> problemas = new CopyOnWriteArrayList<>();
        for (int i = 0; i < tareasNum; i++) {
            final int num = i;
            Runnable runnable = () -> {
                boolean productor = random.nextBoolean();
                if (productor) {
                    try {
                        System.out.println("Produce " + num);
                        boolean couldOffer = blockingQueue.offer("P" + num, tiempoEspera, unidadesEspera);
                        if (couldOffer) {
                            System.out.println("* Producto entregado P" + num);
                            return;
                        } else {
                            System.out.println("Producto NO entregado P" + num);
                        }
                    } catch (InterruptedException ignore) {
                        System.out.println("Problema con el producto " + num + ", no se envió.");
                    }
                } else {
                    try {
                        System.out.println("Espera " + num);
                        String producto = blockingQueue.poll(tiempoEspera, unidadesEspera);
                        if (producto != null) {
                            System.out.println("= Recibido producto: " + producto + " por " + num);
                            return;
                        } else {
                            System.out.println("Producto no recibido a tiempo por " + num);
                        }
                    } catch (InterruptedException ignore) {
                        System.out.println("Problema con el consumidor " + num);
                    }
                }
                // Si falla, lo añadimos a problemas
                problemas.add(num);
            };
            executor.submit(runnable);
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Si no termina en el tiempo esperado
        if (!executor.isTerminated()) {
            // Matamos al que quede ejecutando
            executor.shutdownNow();
            System.err.println("Problema con la ejecución");
            System.exit(-1);
        }
        if (!blockingQueue.isEmpty()) {
            System.out.println("Faltan algunos productos por entregar:");
            blockingQueue.forEach(System.out::println);
        } else {
            System.out.println("Todos los productos entregados");
        }

        if (!problemas.isEmpty()) {
            System.out.println("Problemas con:");
            problemas.forEach(System.out::println);
        } else {
            System.out.println("Sin problemas");
        }
    }
}

