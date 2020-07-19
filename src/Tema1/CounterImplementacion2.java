package Tema1;

public class CounterImplementacion2 {

    static volatile double num;

    public static void rest(){
        new Thread(()->num++).start();
    }
    public static void sum(){
        new Thread(()->num--).start();
    }

    public static void main(String[] args) {
        sum();
        rest();
        System.out.println("num = "+num);
    }
}
