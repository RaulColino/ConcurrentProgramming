package Tema4_1;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class GeneradorNumeros {

    public static void generateNumbers() {
        try {
            PrintWriter writer = new PrintWriter("output1.txt");
            while (true) {
                BigInteger prime = BigInteger.probablePrime(1024, new Random());
                writer.println(prime.toString());
               /* Si se interrumpe, se cierra el fichero y finaliza la ejecución*/
                if (Thread.interrupted()) {
                    writer.append("Fin fichero");
                    writer.close();
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Exception using file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                generateNumbers();
            }
        });
        t.start();
        Scanner teclado = new Scanner(System.in);
        System.out.print("Pulse ENTER para finalizar...");
        teclado.nextLine();
        t.interrupt();
        System.out.println("Hilo interrumpido.");
    }
}
