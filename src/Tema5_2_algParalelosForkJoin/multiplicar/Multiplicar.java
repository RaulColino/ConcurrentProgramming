package Tema5_2_algParalelosForkJoin.multiplicar;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Multiplicar {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int[] array = new int[20];

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            array[i] = random.nextInt(100);
        }

        System.out.println(Arrays.toString(array));
        forkJoinPool.invoke(new FJMultiply(array, 0, array.length));
        System.out.println(Arrays.toString(array));

        forkJoinPool.shutdown();

    }
}

class FJMultiply extends RecursiveAction{
    private int[] array;
    private int low, high;

    private final int multiplicando = 3;
    private final int tamanoMinimo = 10;

    FJMultiply(int [] array, int low, int high){
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (high - low < tamanoMinimo){
            for(int i = low; i < high; i++){
                array[i] = array[i]* multiplicando;
            }
        }else {
            int mid = low + (high - low)/2;

            FJMultiply left = new FJMultiply(array, low, mid);
            FJMultiply right = new FJMultiply(array, mid, high);

            left.fork();
            right.compute();
            left.join();
        }
    }
}

