package Tema5_2_algParalelosForkJoin.factorial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Factorial {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<String>> lista = new ArrayList<>();
        List<Callable<String>> lista2 = new ArrayList<>();
        int inicio = 1;
        int fin = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = inicio; i < fin; i++) {
            String factorial = calcularFactorialBuena(i);
            final int finali = i;
            Callable<String> callable = () -> {
                return calcularFactorialBuena(finali);
            };
            lista.add(callable);

            Callable<String> callable2 = () -> {
                return "Hola";
            };
            lista2.add(callable2);
        }

        List<Future<String>> resultados = executorService.invokeAll(lista);
        List<Future<String>> resultados2 = executorService.invokeAll(lista2);

        for (Future<String> resultado : resultados) {
            System.out.println(resultado.get());
        }

        for (Future<String> resultado : resultados2) {
            System.out.println(resultado.get());
        }

        executorService.shutdown();
    }

    public static Integer calcularFactorial(int numero) {
        int valor = 1;
        for (int i = 1; i <= numero; i++) {
            valor *= i;
        }
        return valor;
    }


    public static BigInteger calcularFactorialBigInteger(int numero) {
        BigInteger valor = BigInteger.ONE;
        for (int i = 1; i <= numero; i++) {
            valor = valor.multiply(BigInteger.valueOf(i));
        }
        return valor;
    }

    ;


    public static String calcularFactorialBuena(int numero) {
        BigInteger valor = BigInteger.ONE;
        for (int i = 1; i <= numero; i++) {
            valor = valor.multiply(BigInteger.valueOf(i));
        }
        return "" + numero + ":" + valor;
    }

}
