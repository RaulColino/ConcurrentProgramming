package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConLocks.ServerWithLocks;

/* (pag41 tema 4)
Implementar la clase Servidor (monitor) para controlar el acceso a un servidor heterogéneo:
• Atiende peticiones web
• Realiza operaciones de mantenimiento
§ Utilizar un monitor con 1 Lock, 2 condiciones y 2 variables enteras que
controlan el número de operaciones de mantenimiento y de peticiones web
§ Métodos del monitor: Empezar y terminar tanto peticiones web
como operaciones de mantenimiento
§ Se producen bloqueos si:
• Hay peticiones web, no se puede hacer mantenimiento
• Si hay operaciones de mantenimiento, las peticiones web se bloquean */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        //Crear el monitor
        Server server = new Server();

        int nMant = 5;
        int nReq = 5;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < nMant; i++) {
            int finalI = i + 1;
            threads.add(new Thread(() -> {
                try {
                    server.startMaintenance("Maintenance " + finalI);
                    Thread.sleep(100 * new Random().nextInt(100));
                    server.endMaintenance("Maintenance " + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        for (int i = 0; i < nReq; i++) {
            int finalI = i + 1;
            threads.add(new Thread(() -> {
                try {
                    server.startRequest("Request " + finalI);
                    Thread.sleep(100 * new Random().nextInt(100));
                    server.endRequest("Request " + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        for (Thread t : threads) {
            t.start();
        }
    }
}
