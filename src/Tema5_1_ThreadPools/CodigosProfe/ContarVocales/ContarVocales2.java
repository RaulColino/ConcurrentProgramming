package Tema5_1_ThreadPools.CodigosProfe.ContarVocales;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ContarVocales2 {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        File file = new File("words.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        List<Future<Integer>> futures = new ArrayList<>();
        while ((st = br.readLine()) != null){
            String palabra = st;
            Callable<Integer> func = () -> {
                // Contar vocales
                int contar = 0;
                for (int i = 0; i < palabra.length(); i++) {
                    if (palabra.charAt(i) == 'a')
                        contar ++;
                }
                return contar;
            };
            futures.add(executor.submit(func));
        }
        int contador = 0;
        for (Future<Integer> future : futures) {
            Integer value = future.get();
            contador += value;
        }
        System.out.println(contador);
        executor.shutdown();
    }
}