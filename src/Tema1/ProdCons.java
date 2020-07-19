package Tema1;
import java.util.Random;

public class ProdCons {

    public static volatile boolean producido;
    public static volatile int product;

    public static void producer(){
        product = new Random().nextInt(100);
        System.out.println("numero producido: "+product);
        producido = true;
    }

    public static void consumer(){
        System.out.println("esperando producto...");
        while (!producido);
        System.out.println("numero consumido: "+product);
        producido = false;
    }

    public static void main(String[] args) {
        producido = false;
        new Thread(()->producer(),"Producer").start();
        new Thread(()->consumer(),"Consumer").start();
    }
}
