package Tema1.Counter2;

public class RunnableAdder implements Runnable{

    //region atributos
    private Thread t;
    private String threadName;
    //endregion


    public RunnableAdder(String threadName) {
        System.out.println("Creating " +  threadName );
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println("running "+threadName);
        try {
            for (int i = 0; i < 50; i++) {
                System.out.println("Thread: " + threadName + ", " + i);
                Thread.sleep(50);
            }
        }catch (InterruptedException e){
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start(){
        System.out.println("starting "+threadName);
        if (t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
