package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConSynchronized.Sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Implementar un programa concurrente que gestione la venta de entradas online:
§ Un monitor simple que contenga una única lista (como recurso compartido) de solicitudes
§ Tres métodos en exclusión mutua
• Comprar: añadir una persona a la lista de solicitudes
• Vender: asignarle la entrada y sacarle de la lista de solicitudes
• Mostrar el estado del recurso y el dinero ganado
§ Implementar dos clases que contengan un hilo que, usando el monitor,
compre y venda respectivamente, actualizando el recurso compartido
§ Implementar un programa principal que simule 1 grupo de gente
comprando y 4 procesos que los atiendan.
 */

public class SalesMain {

    private static final int N_SELLERS = 4;
    private static final int N_CLIENTS = 10;

    private static SalesMonitor manager;

    private synchronized void seller() {
        while (true) {
            try {
                /*manager.show();*/
                sleep(100);
                manager.sell();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void buyer(int id) {
        while (true) {
            /*manager.show();*/
            sleep(200);
            manager.buy(id);
        }
    }

    public static void main(String[] args) {
        manager = new SalesMonitor();

        List<Thread> sellers = new ArrayList<>();
        for (int i = 0; i < N_SELLERS; i++) {
            Thread t = new Thread(() -> new SalesMain().seller(), "seller" + i);
            sellers.add(t);
            t.start();
        }
        List<Thread> buyers = new ArrayList<>();
        for (int i = 0; i < N_CLIENTS; i++) {
            int id = i;
            Thread t = new Thread(() -> new SalesMain().buyer(id), "buyer" + i);
            buyers.add(t);
            t.start();
        }
    }

    private static void sleep(int bound){
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


