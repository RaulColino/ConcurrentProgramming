package Tema3;

import java.util.Random;
import java.util.concurrent.Semaphore;
/*
Hay 5 filosofos y 5 palillos en una mesa circular. Para evitar interbloqueos la solucion mas simple con semaforos es que
los filosofos pares cojan primero el palillo de la izq por ejemplo y los impares al reves. De esta manera
el filosofo que coge el primer palillo comera primero ademas de otro compañero mas.
(5f:2f-4palillos->2f solo pueden comer a la vez)
- Hay 1 palillo por filosofo y cada filosofo coge el palillo de la izquierda y derecha en el orden que sea
segun sea par o impar.
 palillo de la derecha f[0]->p[1] y el de su izquierda f[0]->f[0]
 palillo de la izquierda f[1]->f[1] y el de su derecha f[1]->f[2]
 */

public class CenaDeFilosofos {

    public static final int N_PALILLOS = 5;
    private static Semaphore[] palillo = new Semaphore[N_PALILLOS];

    public static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void filosofo(int n) {
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println(System.nanoTime()+" filosofo" + n + " pensando...");
                    sleep(50);
                    /*Los filosofos pares cogen el palillo de la izq antes, los impares al reves*/
                    System.out.println(System.nanoTime()+" filosofo"+n+" quiere comer");
                    if (n % 2 == 0) { //filososfo par
                        palillo[n].acquire();                /*primero coge el palillo de la izquierda*/
                        palillo[(n+1) % N_PALILLOS].acquire(); /*y luego el de la derecha*
                        /*si se tiene que acceder a una variable compartida "mesa" hace falta un semaforo para que solo
                         acceda a la variable un filosofo a la vez*/
                        /*seccion critica*/
                        System.out.println(System.nanoTime()+" filosofo" + n + " comiendo");
                        sleep(50);
                        /*end seccion critica*
                        /*devuelve los palillos en el mismo orden*/
                        palillo[n].release();
                        palillo[(n+1) % N_PALILLOS].release();
                    } else { //filosofo impar
                        palillo[(n + 1) % N_PALILLOS].acquire(); /*primero coge el palillo de la derecha*/
                        palillo[n].acquire();                /*y luego coge el de la izquierda*/
                        /*seccion critica*/
                        System.out.println(System.nanoTime()+" filosofo" + n + " comiendo");
                        sleep(50);
                        /*end seccion critica*/
                        /*devuelve los palillos en el mismo orden*/
                        palillo[(n + 1) % N_PALILLOS].release();
                        palillo[n].release();
                    }
                    System.out.println(System.nanoTime()+" filosofo"+n+" termino de comer");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "filosofo" + n).start();
    }

    public static void main(String[] args) {
        for (int i = 0; i < N_PALILLOS; i++) {
            palillo[i] = new Semaphore(1);
        }
        for (int i = 0; i < N_PALILLOS; i++) {
            filosofo(i);
        }
    }
}
