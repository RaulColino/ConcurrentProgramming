package Tema3;
// proc1 -----A----B------
//           /      \
// proc2 ---C-->D--->E------>
//            /     /
// proc3 ---F---->G--------


public class SincronizacionCondicional1 {
    static volatile boolean continuarA;
    static volatile boolean continuarD;
    static volatile boolean continuarE1;
    static volatile boolean continuarE2;

    public static void proc1() {
        //while (!continuarA);
        while (!continuarA) {
            System.out.println("esperando A");
        }
        System.out.println("A");
        System.out.println("B");
        continuarE1 = true;
    }

    public static void proc2() {
        System.out.println("C");
        continuarA = true;
        //while (!continuarD);
        while (!continuarD) {
            System.out.println("esperando D");
        }
        System.out.println("D");
        /*while (!continuarE1);*/
        while (!continuarE1) {
            System.out.println("esperando E1");
        }
        /*while (!continuarE2);*/
        while (!continuarE2){
            System.out.println("esperando E2");
        }
        System.out.println("E");
    }

    public static void proc3() {
        System.out.println("F");
        continuarD = true;
        System.out.println("G");
        continuarE2 = true;
    }

    public static void main(String[] args) {
        continuarA = false;
        continuarD = false;
        continuarE1 = false;
        continuarE2 = false;
        new Thread(() -> proc1(), "pr1").start();
        new Thread(() -> proc2(), "pr2").start();
        new Thread(() -> proc3(), "pr3").start();
    }
}
