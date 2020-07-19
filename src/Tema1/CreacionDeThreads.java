package Tema1;

public class CreacionDeThreads {
    //variable estatica
    public final static int staticVar=10;

    //variable no estatica
    private int nonStaticVar=10;


    //metodo no estatico
    public void launchNonStaticThread(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Datos = "+nonStaticVar);
            }
        });
    }

    //main
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Datos = "+ CreacionDeThreads.staticVar);

                /*for (int j = 0; j < 5; j++) {
                    System.out.println("---");
                }*/
            }


        }, "Thread1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int j = 0; j < 5; j++) {
                    System.out.println("---");
                }
            }
        },"Thread2");



        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Datos = "+staticVar);

                for (int j = 0; j < 5; j++) {
                    System.out.println("***");
                }
            }
        },"Thread3");

        t1.start();
        t2.start();
        t3.start();

        CreacionDeThreads e = new CreacionDeThreads();
        e.launchNonStaticThread();


    }

}
