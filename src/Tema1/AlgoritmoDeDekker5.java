package Tema1;

/*
5º Algoritmo de Dekker (solucion final):
- combinación de la 1ª y 4ª aproximación.
- Cumple todas las propiedades de corrección que debe cumplir programa concurrente
§ Propiedades de seguridad (safety)
•Exclusión Mutua
•Ausencia de Interbloqueo pasivo

§ Propiedades de Vida (liveness)
•Ausencia de Retrasos innecesarios
•Ausencia de inanición (starvation)
•Ausencia de interbloqueo activo (livelock)
*/
public class AlgoritmoDeDekker5 {

    public static volatile boolean solicitaPermisoP1;
    public static volatile boolean solicitaPermisoP2;
    public static volatile int turno;
    public static final int N = 5;

    public static void proceso1() {
        for (int i = 0; i < N; i++) {
            solicitaPermisoP1 = true;
            System.out.println("p1 solicito permiso");
            while (solicitaPermisoP2) {
                System.out.println("p1:colision de permisos");
                if (turno != 1) {
                    System.out.println("turno de p2. p2 gana");
                    solicitaPermisoP1 = false;
                    while (turno != 1) ;
                    solicitaPermisoP1 = true;
                }
            }
            /*seccion critica*/
            System.out.println("p1 ejecuta su seccion critica " + i);
            /*end seccion critica*/
            solicitaPermisoP1 = false;
            turno = 2;
        }
    }

    public static void proceso2() {
        for (int i = 0; i < N; i++) {
            solicitaPermisoP2 = true;
            System.out.println("p2 solicito permiso");
            while (solicitaPermisoP1) {
                System.out.println("p2:colision de permisos");
                if (turno != 2) {
                    System.out.println("turno de p1. p1 gana");
                    solicitaPermisoP2 = false;
                    while (turno != 2) ;
                    solicitaPermisoP2 = true;
                }
            }
            /*seccion critica*/
            System.out.println("p2 ejecuta su seccion critica " + i);
            /*end seccion critica*/
            solicitaPermisoP2 = false;
            turno = 1;
        }
    }

    public static void main(String[] args) {
        solicitaPermisoP1 = false;
        solicitaPermisoP2 = false;
        turno = 1;
        new Thread(() -> proceso1(), "pr1").start();
        new Thread(() -> proceso2(), "pr2").start();
    }
}
