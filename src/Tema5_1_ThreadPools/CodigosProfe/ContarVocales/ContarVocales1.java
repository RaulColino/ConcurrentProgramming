package Tema5_1_ThreadPools.CodigosProfe.ContarVocales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ContarVocales1 {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        long startTime = System.nanoTime();
        inner(executorService);
        long endTime = System.nanoTime() - startTime;
        executorService = Executors.newSingleThreadExecutor();
        startTime = System.nanoTime();
        innerList(executorService);
        long endList = System.nanoTime() - startTime;

        executorService = Executors.newCachedThreadPool();
        startTime = System.nanoTime();
        inner(executorService);
        long endTime2 = System.nanoTime() - startTime;
        executorService = Executors.newCachedThreadPool();
        startTime = System.nanoTime();
        innerList(executorService);
        long endList2 = System.nanoTime() - startTime;

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        startTime = System.nanoTime();
        inner(executorService);
        long endTime3 = System.nanoTime() - startTime;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        startTime = System.nanoTime();
        innerList(executorService);
        long endList3 = System.nanoTime() - startTime;

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        startTime = System.nanoTime();
        inner(executorService);
        long endTime4 = System.nanoTime() - startTime;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        startTime = System.nanoTime();
        innerList(executorService);
        long endList4 = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        innerIterative();
        long endTime5 = System.nanoTime() - startTime;

        System.out.println("Single: " + endTime/100000);
        System.out.println("Cache: " + endTime2/100000);
        System.out.println("Fixed * 2: " + endTime3/100000);
        System.out.println("Fixed: " + endTime4/100000);
        System.out.println("L Single: " + endList/100000);
        System.out.println("L Cache: " + endList2/100000);
        System.out.println("L Fixed * 2: " + endList3/100000);
        System.out.println("L Fixed: " + endList4/100000);
        System.out.println("Iterative: " + endTime5/100000);

    }

    public static void inner(ExecutorService executorService) throws IOException, ExecutionException, InterruptedException {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File("words.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        List<Future<Integer>> futures = new LinkedList<>();
        while ((st = br.readLine()) != null) {
            String palabras = st;
            Callable<Integer> func = () -> {
                int vocales = 0;
                for (int x = 0; x < palabras.length(); x++) {
                    if ((palabras.charAt(x) == 'a') || (palabras.charAt(x) == 'e') || (palabras.charAt(x) == 'i') || (palabras.charAt(x) == 'o') || (palabras.charAt(x) == 'u')) {
                        vocales++;
                    }
                }
                return vocales;
            };
            Future<Integer> value = executorService.submit(func);
            futures.add(value);
        }
        int total = 0;
        for (Future<Integer> future : futures) {
            total += future.get();
        }
        executorService.shutdown();
        System.out.println(total);
    }



    public static void innerList(ExecutorService executorService) throws IOException, ExecutionException, InterruptedException {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File("words.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        List<Callable<Integer>> futures = new LinkedList<>();
        while ((st = br.readLine()) != null) {
            String palabras = st;
            Callable<Integer> func = () -> {
                int vocales = 0;
                for (int x = 0; x < palabras.length(); x++) {
                    if ((palabras.charAt(x) == 'a') || (palabras.charAt(x) == 'e') || (palabras.charAt(x) == 'i') || (palabras.charAt(x) == 'o') || (palabras.charAt(x) == 'u')) {
                        vocales++;
                    }
                }
                return vocales;
            };
            futures.add(func);
        }
        int total = 0;
        for (Future<Integer> future : executorService.invokeAll(futures)) {
            total += future.get();
        }
        executorService.shutdown();
        System.out.println(total);
    }

    public static void innerIterative() throws IOException, ExecutionException, InterruptedException {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File("words.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int vocales = 0;
        while ((st = br.readLine()) != null) {
            for (int x = 0; x < st.length(); x++) {
                if ((st.charAt(x) == 'a') || (st.charAt(x) == 'e') || (st.charAt(x) == 'i') || (st.charAt(x) == 'o') || (st.charAt(x) == 'u')) {
                    vocales++;
                }
            }
        }
        System.out.println(vocales);
    }
}

