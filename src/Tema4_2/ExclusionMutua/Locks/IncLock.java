package Tema4_2.ExclusionMutua.Locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IncLock {

    private static double x = 0;
    private static Lock xLock = new ReentrantLock();

    public static void inc() {
        /*for (int i = 0; i < 10000000; i++) {
            xLock.lock();
            x = x + 1;
            xLock.unlock();
        }*/

        for (int i = 0; i < 10000000; i++) {
            xLock.lock();
            try {
                x = x + 1;
            } finally { /*meter la sección crítica en un bloque try/finally para liberar el cerrojo tanto si se produce una excepción como si no*/
                xLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> inc(), "t" + i));
        }
        for (int i = 0; i < 4; i++) {
            ts.get(i).start();
        }
        for (int i = 0; i < 4; i++) {
            ts.get(i).join();
        }
    }

}

/*
Las sentencias sincronizadas son muy sencillas de
usar pero tienen limitaciones:

§ La exclusión mutua no se puede adquirir en un
método y liberar en otro

§No se puede especificar un tiempo máximo de
espera para adquirir el cerrojo

§No se puede crear una exclusión mutua
extendida como en el caso de los lectores escritores
*/
