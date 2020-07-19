package Tema1;

/*
El algoritmo de Peterson, también conocido como solucion de Peterson
es un algoritmo que utiliza solo memoria compartida para la comunicación.
Es una simplificación del algoritmo de Dekker. El algoritmo básico puede
generalizarse fácilmente a un número arbitrario de procesos.
 */
public class AlgoritmoDePeterson {

    public static volatile boolean[] solicitaPermisoP;
    public static volatile int turno;
    public static final int N = 5;

    public static void proceso0() {
        for (int i = 0; i < N; i++) {
            solicitaPermisoP[0] = true;
            System.out.println("p0 solicito permiso");
            turno = 1;
            System.out.println("turno=1");
            while (solicitaPermisoP[1] && turno != 0){System.out.println("p0 espera: p1 solicito y turno!=0");} //espera...
            /*seccion critica*/
            System.out.println("p0 ejecuto su seccion critica "+i);
            /*end seccion critica*/
            solicitaPermisoP[0] = false;
        }

    }

    public static void proceso1() {
        for (int i = 0; i < N; i++) {
            solicitaPermisoP[1] = true;
            System.out.println("p1 solicito permiso");
            turno = 0;
            System.out.println("turno=0");
            while (solicitaPermisoP[0] && turno != 1){System.out.println("p1 espera: p0 solicito y turno!=1");} //espera...
            /*seccion critica*/
            System.out.println("p1 ejecuto su seccion critica "+i);
            /*end seccion critica*/
            solicitaPermisoP[1] = false;
        }

    }

    public static void main(String[] args) {
        solicitaPermisoP = new boolean[2];
        solicitaPermisoP[0] = false;
        solicitaPermisoP[1] = false;
        new Thread(()-> proceso0(),"pr1").start();
        new Thread(()-> proceso1(),"pr2").start();
    }
}

