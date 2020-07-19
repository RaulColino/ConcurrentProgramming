package EjerciciosExtra;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio5 {
    static int tareasNum = 20;
    static int permisos = 5;

    public static void main(String[] args) throws InterruptedException {
        // Nota, al utilizar diversos executores y poder ver los resultados separados
        // se necesita esperar a la finalización de las tareas.
        // Si se plantea con callables no haría falta el await, bastaría con la espera de resultados

        System.out.println("Primera idea con una barrera");
        mainCyclicBarrier();

        System.out.println("Misma idea pero con hilos cacheados");
        mainCyclicBarrierCache();

        System.out.println("Probamos con un cerrojo");
        mainCyclicBarrierLock();

        System.out.println("Probamos con un AtomicInteger");
        mainAtomicInteger();

       // Tenemos más métodos de sincronización, todos serían válidos con el uso adecuado
    }

    public static void mainCyclicBarrier() throws InterruptedException {
        // Para que funcione al hacer las pruebas
        int permisosCyclic = permisos;
        int hilos = permisos;
        ExecutorService executor = Executors.newFixedThreadPool(hilos);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(permisosCyclic, () -> {
            System.out.println("** " +Thread.currentThread().getName());
        });
        for (int i = 0; i < tareasNum; i++) {
            Runnable tarea = () -> {
                System.out.println("* " +Thread.currentThread().getName());
                try {
                    // Aquí existe un problema, no se cumple uno de los requisitos del programa
                    // Además si el número de hilos es menor a los creados se bloquea y nunca finaliza.
                    // Probarlo cambiando las variables
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            };
            executor.submit(tarea);
        }
        executor.shutdown();
        executor.awaitTermination(4, TimeUnit.SECONDS);
    }

    public static void mainCyclicBarrierCache() throws InterruptedException {
        // Para que funcione al hacer las pruebas
        int permisosCyclic = permisos;
        // Con este ejecutor funcionaría, al crear más threads en caso necesario.
        ExecutorService executor = Executors.newCachedThreadPool();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(permisosCyclic, () -> {
            System.out.println("$$ "+Thread.currentThread().getName());
        });
        for (int i = 0; i < tareasNum; i++) {
            Runnable tarea = () -> {
                System.out.println("$ "+Thread.currentThread().getName());
                try {
                    // Mismo problema que el anterior
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            };
            executor.submit(tarea);
        }
        executor.shutdown();
        executor.awaitTermination(4, TimeUnit.SECONDS);
    }


    public static void mainCyclicBarrierLock() throws InterruptedException {
        // Soluciona el problema, se imprimen siempre juntos en la 5 ejecución.
        int permisosCyclic = permisos;
        int numeroHilos = permisos;
        ExecutorService executor = Executors.newFixedThreadPool(numeroHilos);
        ReentrantLock lock = new ReentrantLock(true);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(permisosCyclic, () -> {
            // Obtenemos el cerrojo
            lock.lock();
            System.out.println("-- " + Thread.currentThread().getName());
        });
        for (int i = 0; i < tareasNum; i++) {
            Runnable tarea = () -> {
                try {
                    // Lo ponemos a principio, los hilos esperan
                    // Un hilo obtiene el cerrojo
                    cyclicBarrier.await();
                    // Los hilos ya no esperan, necesitan el cerrojo
                    // El hilo que ejecuta el runnable de CyclicBarrier tiene el cerrojo
                    if (!lock.isHeldByCurrentThread())
                        // Los otros hilos esperan al cerrojo
                        lock.lock();

                    System.out.println("- " + Thread.currentThread().getName());
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    // Por último soltamos el cerrojo
                    lock.unlock();
                }
            };
            executor.submit(tarea);
        }
        executor.shutdown();
        executor.awaitTermination(4, TimeUnit.SECONDS);
    }


    public static void mainAtomicInteger() throws InterruptedException {
        // Soluciona el problema, se imprimen siempre juntos en la 5 ejecución.
        int numeroHilos = permisos;
        AtomicInteger value = new AtomicInteger(permisos);
        ExecutorService executor = Executors.newFixedThreadPool(numeroHilos);
        ReentrantLock lock = new ReentrantLock(true);
        for (int i = 0; i < tareasNum; i++) {
            Runnable tarea = () -> {
                    // La sección crítica, manipular el AtomicInteger
                    lock.lock();
                    int actual_value = value.decrementAndGet();
                    if (actual_value == 0){
                        // Cambiamos los permisos, por eso se necesita el cerrojo
                        value.set(permisos);
                        System.out.println("== "+Thread.currentThread().getName());
                        // Ninguno de los otros hilos van a trabajar en este momento
                        System.out.println("== "+Thread.currentThread().getName());
                    }else{
                        // Si se pone el unlock aquí podrían existir condiciones de carrera
                        System.out.println("= "+Thread.currentThread().getName());
                    }
                    // Al final se realiza el unlock.
                    // Sin el requisito de escribir a la vez los prints, el unlock estaría encima de los prints.
                    lock.unlock();
            };
            executor.submit(tarea);
        }
        executor.shutdown();
        executor.awaitTermination(4, TimeUnit.SECONDS);
    }
}

