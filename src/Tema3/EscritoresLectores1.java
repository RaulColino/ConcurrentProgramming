package Tema3;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
EscritoresLectores1:
Los escritores y lectores tienen la misma prioridad
*/
public class EscritoresLectores1 {

    public static final int N_LEC = 5;
    public static final int N_ESC = 5;

    private static Semaphore semAcceso; /*Semaforo de acceo a la BD*/
    private static Semaphore semLectores; /*varios lectores pueden entrar a la vez pero solo uno puede modificar variables a la vez*/

    private static int nLectores; /*num de escritores trabajando dentro de la BD*/

    public static void sleep(int bound){
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void escritor(int n){
        new Thread(()->{
            while (true){
                try {
                    sleep(100);
                    semAcceso.acquire();

                    System.out.println("escritor" + n + " entro a la BD");
                    sleep(100);
                    System.out.println("escritor" + n + " esta escribiendo...");
                    sleep(100);
                    System.out.println("escritor" + n + " sale de la BD");

                    semAcceso.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"escritor"+n).start();
    }

    public static void lector(int n){
        new Thread(()->{
            while(true){
                try {
                    sleep(100);
                    semLectores.acquire();///////////////////////////////////////////////////////////////////////////////////+
                    nLectores++;
                    System.out.println("lector" + n + " quiere entrar a la BD");
                    if (nLectores==1){ /*si va a ser el primer lector en entrar en la BD tiene que pedir permiso*/
                        semAcceso.acquire(); /*si escritor en la BD se queda bloqueado y a los lectores que hacen semLectores.acquire()*/////++
                        System.out.println("lector" + n + " ha sido el primero en entrar en la BD vacia");
                    }
                    System.out.println("lector"+n+"entro en la BD");
                    semLectores.release();////////////////////////////////////////////////////////////////////////////////////-


                    System.out.println("lector"+n+" esta escribiendo");
                    sleep(100);


                    semLectores.acquire(); ///////////////////////////////////////////////////////////////////////////////////+
                    nLectores--;
                    System.out.println("lector"+n+"salio de la BD");
                    if (nLectores==0){
                        semAcceso.release();/////////////////////////////////////////////////////////////////////////////////////////////--
                        System.out.println("lector" + n + " ha sido el ultimo en salir de la BD. BD vacia.");
                    }
                    semLectores.release();///////////////////////////////////////////////////////////////////////////////////-
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"lector"+n).start();
    }

    public static void main(String[] args) {
        semAcceso = new Semaphore(1);
        semLectores = new Semaphore(1);
        nLectores = 0;
        for (int i = 0; i < N_ESC; i++) {
            escritor(i);
        }
        for (int i = 0; i < N_LEC; i++) {
            lector(i);
        }
    }
}
