package Tema4_1;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class GeneradorNumeros2 {

    public static void generateNumbers() {
        try {
            FileWriter writer = new FileWriter("output2.txt");
            while (true) {
                BigInteger prime = BigInteger.probablePrime(1024, new Random());
                writer.append(prime.toString());
                writer.append("\r\n");
                try {
                    /*sleep bloquea el hilo 1s.Pero si está bloqueado no puede consultar el método Thread.interrupted()*/
                    Thread.sleep(1000);
                    /*Entonces hay que hacer lo siguiente:Si esperamos 1 seg entre cada generación de un número,
                    se desbloquea el método en cuanto se interrumpe el hilo*/
                } catch (InterruptedException e) {
                    writer.append("Fin fichero");
                    writer.close();
                    return;
                }
            }
        } catch (IOException e) {//catch de la apertura de fichero
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
