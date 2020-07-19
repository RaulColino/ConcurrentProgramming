package Tema1;
import java.util.Random;
/*Igual que BufferMain pero con bucle infinito*/
public class ProdCons2 {

    public static volatile boolean producido;
    public static volatile int product;


    public static void producer() {
        while (true) {
            while (producido) ; //mientras haya un numero producido no consumido espera
            product = new Random().nextInt(100);
            System.out.println("numero producido: " + product);
            /*sleep(5000);*/
            producido = true;
        }
    }

    public static void consumer() {
        while (true) {
            System.out.println("esperando producto...");
            while (!producido) ; //mientras no haya un producto producido espera
            System.out.println("numero consumido: " + product);
            producido = false;
            /*sleep(1000);*/
        }
    }

    public static void main(String[] args) {
        producido = false;
        new Thread(() -> producer(), "Producer").start();
        new Thread(() -> consumer(), "Consumer").start();
    }
}
