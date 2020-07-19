package Tema5_1_ThreadPools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Analizar el rendimiento de los diferentes tipos de
thread pools.
-- Tipos de thread pools ------------------------------------------------------------------------
•newSingleThreadExecutor(): Crea un único thread, utilizado únicamente para validar el código.
•newFixedThreadPool(int nThreads): Crea un thread pool que contiene el número de threads que necesitemos.Siempre habrá nThreads activos.
•newCachedThreadPool(): Crea un thread pool que lanzará nuevos threads cuando sea necesario,pero intentará reutilizar los anteriores cuando estén disponibles.Están
 recomendados para aplicaciones que realizan tareas muy cortas. Los threads que no se hayan usado durante más de 60 segundos terminan y se eliminan del pool.
*/
public class RendimientoThreadPools {

    private static class Tarea implements Runnable {

        private final int id;

        public Tarea(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("Empezo la tarea " + id + " realizada por el thread " + Thread.currentThread().getName());
            int n_enteros = 1000;
            List<Integer> listaEnteros = new ArrayList<>(n_enteros);
            for (int j = 0; j < n_enteros; j++) {
                listaEnteros.add(new Random().nextInt(999999));
            }
            int max = listaEnteros.get(0);
            for (int j = 1; j < listaEnteros.size(); j++) {
                max = (max < listaEnteros.get(j)) ? listaEnteros.get(j) : max;
            }
            System.out.println("En la tarea " + id + " realizada por el thread '" + Thread.currentThread().getName() + "' es: " + max);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 1000; i++) {
            int nTarea = i;
            Tarea t = new Tarea(nTarea);
            executorService.execute(t);
        }
        executorService.shutdown();
    }
}
