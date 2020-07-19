package Tema4_2.ExclusionMutua;

public class IncSynchronized2 {
    private static double x = 0;
    private static Object xLock = new Object();
    private static double y = 0;
    private static Object yLock = new Object();

    public static void incX() {
        for (int i = 0; i < 10000000; i++) {
            synchronized (xLock) {
                x = x + 1;
            }
        }
    }

    public static void incY() {
        for (int i = 0; i < 10000000; i++) {
            synchronized (yLock) {
                y = y + 1;
            }
        }
    }

    class Counter {
        private int x = 0;
        public synchronized void inc(){
            x = x + 1;
        }
        public int getValue(){
            return x;
        }
    }

   /* public class IncSynchronizedMethod {
        private static Counter c = new Counter();
        public static void inc() {
            for (int i = 0; i < 10000000; i++) {
                c.inc();
            }
        }
    }*/
}
