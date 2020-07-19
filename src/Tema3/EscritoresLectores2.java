package Tema3;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
EscritoresLectores2:
        Los escritores tienen prioridad a entrar en la BD
        Los lectores esperan hasta que no queden escritores en la cola
        Si consigue un lector entrar en la BD entran todos los lectores
*/
public class EscritoresLectores2 {

    public static final int N_ESC = 5;
    public static final int N_LEC = 5;

    private static Semaphore semAccesoBD;
    private static Semaphore semEscritores; /*Para control de acceso a variables compartidas*/
    private static Semaphore semLectores; /*Para control de acceso a variables compartidas*/
    private static Semaphore semAccesoLectores;

    private static int nEscritores; /*nEsc en la cola o BD*/
    private static int nLectores; /*nLec en la cola o nLec en la BD*/


    private static void sleep(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void escritor(int n) {
        new Thread(() -> {
            while (true) {
                try {
                    sleep(100);
                    semEscritores.acquire();
                    nEscritores++;
                    if (nEscritores == 1) {
                        semAccesoLectores.acquire(); /*si hay una cola de lec esperando antes que el, tendra que esperar por los lec*/
                    }
                    semEscritores.release();

                    semAccesoBD.acquire();
                    System.out.println("escritor" + n + " escribiendo...");
                    sleep(50);
                    semAccesoBD.release();

                    semEscritores.acquire();
                    nEscritores--;
                    if (nEscritores == 0) {
                        semAccesoLectores.release();
                    }
                    semEscritores.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "escritor" + n).start();
    }

    public static void lector(int n) {
        new Thread(() -> {
            while (true) {
                try {
                    sleep(50);
                    System.out.println("lector "+n+" quiere entrar");
                    semAccesoLectores.acquire(); /*Si es el primer lector y bd ocupada->queda bloqueado y espera a que acaben escritores*/
                    semLectores.acquire();       /*Seccion critica: solo un lector a la vez puede hacer nLectores++*/
                    nLectores++;
                    if (nLectores == 1) {
                        semAccesoBD.acquire();
                    }
                    semLectores.release();       /*End seccion critica: solo un lector a la vez puede hacer nLectores++*/
                    semAccesoLectores.release(); /*Libera al siguiente lector q accede a nLectores++ si hay cola de lectores esperando*/

                    /*sec critica*/
                    System.out.println("lector" + n + " leyendo");
                    sleep(50);
                    /*end sec critica*/

                    semLectores.acquire();
                    nLectores--;
                    if (nLectores == 0) {
                        semAccesoBD.release();
                    }
                    semLectores.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "lector" + n).start();
    }

    public static void main(String[] args) {
        nEscritores = 0;
        nLectores = 0;
        semAccesoBD = new Semaphore(1);
        semEscritores = new Semaphore(1);
        semLectores = new Semaphore(1);
        semAccesoLectores = new Semaphore(1);
        for (int i = 0; i < N_ESC; i++) {
            escritor(i);
        }
        for (int i = 0; i < N_LEC; i++) {
            lector(i);
        }
    }

}
