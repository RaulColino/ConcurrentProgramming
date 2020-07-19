package Tema1;
/*
Se crean 2 hilos que modifican una variable global:
uno resta 1 y el otro suma 1 a la variable global n veces.
*/
public class Counter {

    static int result = 0;
    static final int TIMES = 50;

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < TIMES; i++) {
                result++;
                System.out.println("INC: "+result);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i < TIMES; i++) {
                result--;
                System.out.println("DEC: "+result);
            }
        }).start();
    }
}
