package Tema5_1_ThreadPools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ContarVocales {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        File file = new File("output1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));


        String st;
        List<Future<Integer>> futures = new ArrayList<>();
        /*List<Callable<Integer>> futures = new ArrayList<>();*/
        while ((st = br.readLine()) != null) {
            String palabra = st;
            Callable<Integer> func = () -> {
                //contar vocales
                int vocales = 0;
                for (int i = 0; i < palabra.length(); i++) {
                    if (palabra.charAt(i) == 'a' || palabra.charAt(i) == 'e' || palabra.charAt(i) == 'i' || palabra.charAt(i) == 'o' || palabra.charAt(i) == 'u' ){
                        vocales++;
                    }
                }
                return vocales;
            };
            futures.add(executor.submit(func));
            /*futures.add(func);*/
        }
        int contador = 0;
        for (Future<Integer> future : futures) {
            Integer value = future.get();
            contador += value;
        }
        /*for (Future<Integer> future : executorService.invokeAll(futures)) {
            total += future.get();
        }*/
        

        System.out.println(contador);
        executor.shutdown();
    }

}
