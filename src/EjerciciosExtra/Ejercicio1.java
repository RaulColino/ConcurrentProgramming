package EjerciciosExtra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Ejercicio1 {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ThreadCy thread = new ThreadCy();
            thread.start();
            threads.add(thread);

        }

        for (Thread thread : threads) {
            thread.join();
        }

    }
}
class ThreadContador extends Thread{
    CountDownLatch contador;
    ThreadContador(){
        contador = new CountDownLatch(10);
    }

    public void run(){
        while(!interrupted() && contador.getCount() > 0){
            contador.countDown();
            //var cuente = contador.getCount(); //Java 10 only
            long cuente = contador.getCount();
            if (cuente == 5 || cuente == 0){
                System.out.println(cuente);
            }
            System.out.println(cuente);
        }
    }
}


class ThreadCy extends Thread{
    CyclicBarrier contador;
    ThreadCy(){
        contador = new CyclicBarrier(5);
    }

    public void run(){
        while(!interrupted() ){
            try {
                contador.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
