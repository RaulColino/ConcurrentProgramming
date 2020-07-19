package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConSynchronized.Sales;

import java.util.LinkedList;
import java.util.Queue;

public class SalesMonitor { /*BufferMonitor class*/

    private static final int TICKET_PRICE = 100;

    private Queue<Integer> requests;
    private int money;

    public SalesMonitor() {
        requests = new LinkedList<>();
    }

    /*Cuando llega una peticion de compra nueva se añade a la cola de peticiones*/
    public synchronized void buy(int client) {
        requests.add(client);
        System.out.println("cliente " + client + " ha llegado");
        this.notify();
    }

    /*Espera a que aparezca una peticion de cliente en la lista de peticiones.
    Si aparece alguna entonces deja de esperar y procesa la peticion.*/
    public synchronized void sell() throws InterruptedException {
        while (requests.isEmpty()) {
            this.wait();
        }
        int client = requests.poll();
        System.out.println(Thread.currentThread().getName()+": procesando cliente " + client);
        money += TICKET_PRICE;
        this.notifyAll();
    }

    public synchronized void show() {
        for (int client : requests) {
            System.out.println("request of client: " + client);
        }
    }

}
