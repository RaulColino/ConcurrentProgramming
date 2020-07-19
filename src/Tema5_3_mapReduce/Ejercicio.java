package Tema5_3_mapReduce;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Ejercicio {
    static int n = 1000;
    static int m = 1000;
    static Random random = new Random(3);

    static void crearFichero(String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(random.nextFloat());
            for (int j = 1; j < m; j++) {
                sb.append(",");
                sb.append(random.nextFloat());
            }
            sb.append("\n");
            fw.write(sb.toString());
        }
        fw.close();
    }


    static Double promedio(String[] partes) {
        Double suma_elements = 0.0;
        for (String s : partes) {
            suma_elements += Double.parseDouble(s);
        }
        int num_elements = partes.length;
        Double max_line = suma_elements / num_elements;
        return max_line;
    }

    static void analisisIterativo(String path) throws IOException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double max = Double.MIN_VALUE;
        String line;
        while ((line = fr.readLine()) != null) {
            String[] split_line = line.split(",");
            Double suma_elements = 0.0;
            for (String s : split_line) {
                suma_elements += Double.parseDouble(s);
            }
            int num_elements = split_line.length;
            Double max_line = suma_elements / num_elements;
            if (max_line > max) max = max_line;
        }

        System.out.println("Promedio máximo (iterativo) " + max);
    }

    static void analisis(String path) throws FileNotFoundException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double promedio_maximo = fr.lines()
                .map(line -> line.split(","))
                // Calcular promedio
                .map(partes -> {
                    Double suma_elements = 0.0;
                    for (String s : partes) {
                        suma_elements += Double.parseDouble(s);
                    }
                    int num_elements = partes.length;
                    Double max_line = suma_elements / num_elements;
                    return max_line;
                }).max(Double::compare).get();

        System.out.println("Promedio máximo (stream)    " + promedio_maximo);
    }

    static void analisisReduce(String path) throws FileNotFoundException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double promedio_maximo = fr.lines()
                .map(line -> line.split(","))
                // Calcular promedio
                .reduce(Double.MIN_VALUE, (max, l) -> {
                    Double promedio = promedio(l);
                    return (max > promedio) ? max : promedio;
                }, (max1, max2) -> (max1 > max2) ? max1 : max2);

        System.out.println("Promedio máximo (reduce)    " + promedio_maximo);
    }


    static void analisisReduceP(String path) throws FileNotFoundException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double promedio_maximo = fr.lines().parallel()
                .map(line -> line.split(","))
                // Calcular promedio
                .reduce(Double.MIN_VALUE, (max, l) -> {
                    Double promedio = promedio(l);
                    return (max > promedio) ? max : promedio;
                }, (max1, max2) -> (max1 > max2) ? max1 : max2);

        System.out.println("Promedio máximo (reduceP)   " + promedio_maximo);
    }


    static void analisisCollect(String path) throws FileNotFoundException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double promedio_maximo = fr.lines()
                .map(line ->
                        Arrays.stream(line.split(","))
                                .collect(Collectors.averagingDouble(Double::parseDouble))
                )
                .max(Double::compare).get();
        // Calcular promedio
        System.out.println("Promedio máximo (collect)   " + promedio_maximo);
    }

    static void analisisCollectP(String path) throws FileNotFoundException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        Double promedio_maximo = fr.lines().parallel()
                .map(line ->
                        Arrays.stream(line.split(","))
                                .collect(Collectors.averagingDouble(Double::parseDouble))
                )
                .max(Double::compare).get();
        // Calcular promedio
        System.out.println("Promedio máximo (collectP)  " + promedio_maximo);
    }


    static void analisisExecutor(String path) throws IOException, ExecutionException, InterruptedException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        String line;
        ExecutorService executor = Executors.newFixedThreadPool(ForkJoinPool.getCommonPoolParallelism());
        List<Future<Double>> futures = new ArrayList<>(n);
        while ((line = fr.readLine()) != null) {
            final String linef = line;
            Callable<Double> promedioCall = () -> promedio(linef.split(","));
            futures.add(executor.submit(promedioCall));
        }
        executor.shutdown();
        Double max = Double.MIN_VALUE;
        for (Future<Double> future : futures) {
            Double value = future.get();
            if (value > max) max = value;
        }
        System.out.println("Promedio máximo (executor)  " + max);
    }

    static void analisisForkJoin(String path) throws IOException, ExecutionException, InterruptedException {
        // Leer fichero y mientras ejecutar
        BufferedReader fr = new BufferedReader(new FileReader(path));
        String line;
        ForkJoinPool executor = ForkJoinPool.commonPool();
        ArrayList<String> lines = new ArrayList(n);
        while ((line = fr.readLine()) != null) {
            lines.add(line);
        }
        tareaForkJoin tarea = new tareaForkJoin(lines);
        Double max = executor.invoke(tarea);
        executor.shutdown();
        System.out.println("Promedio máximo (forkj)     " + max);
    }

    public static void main2(String[] args) throws IOException, ExecutionException, InterruptedException {
        String fichero = "test.csv";
        crearFichero(fichero);
        analisisIterativo(fichero);
        analisis(fichero);
        analisisReduce(fichero);
        analisisReduceP(fichero);
        analisisCollect(fichero);
        analisisCollectP(fichero);
        analisisExecutor(fichero);
        analisisForkJoin(fichero);
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String fichero = "test.csv";
        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        crearFichero(fichero);
        long end = System.currentTimeMillis() - start;
        System.out.println(end);
        for (int i = 0; i < 10; i++) {

            start = System.currentTimeMillis();

            analisisIterativo(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisis(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisReduce(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisReduceP(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisCollect(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisCollectP(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisExecutor(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append(",");
            start = System.currentTimeMillis();

            analisisForkJoin(fichero);

            end = System.currentTimeMillis() - start;
            sb.append(end).append("\n");
        }
        System.out.println(sb.toString());
        FileWriter fw = new FileWriter("data.csv");
        fw.write(sb.toString());
        fw.close();
    }
}

class tareaForkJoin extends RecursiveTask<Double> {
    List<String> lines;
    List<String[]> lista;
    int principio, fin;
    int estado = 0;

    public tareaForkJoin(List<String> lines) {
        this.lines = lines;
        this.principio = 0;
        this.fin = lines.size();
        estado = 0;
    }

    public tareaForkJoin(List<String> lines, int principio, int fin) {
        this.lines = lines;
        this.principio = principio;
        this.fin = fin;
        estado = 1;
    }

    public tareaForkJoin(int principio, int fin, List<String[]> lista) {
        this.lista = lista;
        this.principio = principio;
        this.fin = fin;
        estado = 2;
    }

    @Override
    protected Double compute() {
        // División
        tareaForkJoin tarea1;
        tareaForkJoin tarea2;
        if (estado == 0) {
            int mid = (fin - principio) / 2;
            tarea1 = new tareaForkJoin(lines, principio, mid);
            tarea2 = new tareaForkJoin(lines, mid, fin);
        } else if (estado == 1) {
            List<String[]> lista = new ArrayList<>(Ejercicio.n);
            for (int i = principio; i < fin; i++) {
                lista.add(lines.get(i).split(","));
            }
            int mid = lista.size() / 2;
            tarea1 = new tareaForkJoin(0, mid, lista);
            tarea2 = new tareaForkJoin(mid, lista.size(), lista);

        } else {
            Double max = Double.MIN_VALUE;
            for (int i = principio; i < fin; i++) {
                double promedio = Ejercicio.promedio(lista.get(i));
                max = Double.max(max, promedio);
            }
            return max;
        }

        // Ejecución
        tarea2.fork();
        return Double.max(tarea1.compute(), tarea2.join());
    }
}
