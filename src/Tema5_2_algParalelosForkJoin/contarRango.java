package Tema5_2_algParalelosForkJoin;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class contarRango {
    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(10000);
        ForkJoinPool forkJoinPool = new ForkJoinPool(100);
        int[] array = new int[100];

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            array[i] = random.nextInt(10);
        }

        int value = forkJoinPool.invoke(new FJRango(array, 0, array.length));
        System.out.println(value);

        int contar = 0;
        for (int arrayValue : array) {
            if (arrayValue > 1 && arrayValue < 10)
                contar++;
        }
        System.out.println(contar);

        forkJoinPool.shutdown();

    }
}

class FJRango extends RecursiveTask<Integer> {
    private int[] array;
    private int low, high;

    private final int inicio = 1;
    private final int fin = 10;
    private final int tamanoMinimo = 10;

    FJRango(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected Integer compute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (high - low < tamanoMinimo) {
            int contardor = 0;
            for (int i = low; i < high; i++) {
                if (array[i] > inicio && array[i] < fin)
                    contardor++;
            }
            return contardor;
        } else {
            int mid = low + (high - low) / 2;

            FJRango left = new FJRango(array, low, mid);
            FJRango right = new FJRango(array, mid, high);

            left.fork();
            return right.compute() + left.join();

        }
    }
}
