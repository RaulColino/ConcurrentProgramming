package Tema1;

import java.util.Random;

public class ClientServer {

    public static volatile boolean request;
    public static volatile boolean done;
    public static volatile double pedido;


    public static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void client() {
        System.out.println("generando pedido...");
        request = true;
        sleep(50);
        while (!done) ;
        System.out.println("pedido recibido: " + pedido);
        request = false;
    }

    public static void server() {
        System.out.println("esperando pedido");
        while (!request) ;
        System.out.println("pedido enviado");
        pedido = new Random().nextInt(100);
        //sleep(1000);
        done = true;
    }

    public static void main(String[] args) {
        request = false;
        done = false;
        new Thread(() -> server(), "Producer").start();
        new Thread(() -> client(), "Consumer").start();
    }
}
