package Tema4_1;

public class Ejercicio41 {

    public static void mensajes() {
        String[] frases = new String[]{"bla1", "bla2", "bla3", "bla4"};
        for (int i = 0; i < frases.length; i++) {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + ": " + frases[i]);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Se acabó! Me han interrumpido!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        boolean continuar = true;
        int nSegundosEspera = 5;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mensajes();
            }
        });
        t.start();
        int contador = 0;
        while (continuar) {
            try {
                Thread.sleep(1000);
                contador++;
                if (contador == nSegundosEspera) {
                    System.out.println(Thread.currentThread().getName() + ": cansado de esperar");
                    t.interrupt();
                    t.join();
                    continuar = false;
                } else {
                    System.out.println(Thread.currentThread().getName() + ": todavia esperando...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": por fin");
    }
}
