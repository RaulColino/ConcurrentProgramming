package EjerciciosExtra;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio2 {
    static int numeroTareas = 100;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //ExecutorService executorService = Executors.newCachedThreadPool();
        ReentrantLock lock = new ReentrantLock();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, ()->{
             lock.lock();
             System.out.println("--"+Thread.currentThread().getName());
        });
        for (int i = 0; i < numeroTareas; i++) {
           Runnable tarea = () -> {
               try {
                   // Candado
                   cyclicBarrier.await();

                   if (!lock.isHeldByCurrentThread())
                       lock.lock();

                   System.out.println(Thread.currentThread().getName());
               } catch (InterruptedException | BrokenBarrierException e) {
                   e.printStackTrace();
               }finally {
                   lock.unlock();
               }
           } ;
           executorService.submit(tarea);
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}
