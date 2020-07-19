package PracticaFinal;

import java.util.concurrent.CountDownLatch;

public class RaceJudge implements Runnable {

    static final int COUNTDOWN = 3;
    final int N_VEHICLES;
    final int N_LAPS;

    public static CountDownLatch latchVehiclesReady; //se desbloquea cuando todos los coches estan listos
    public static CountDownLatch latchCountdown = new CountDownLatch(COUNTDOWN); //cuenta atras desde el 3
    public static CountDownLatch latchVehiclesRanked;   //se desbloquea cuando todos los vehiculos han pasado la meta y obtienen su posicion final
    public static CountDownLatch latchVehiclesFinished; //se desbloquea cuando todos los vehiculos han terminado la carrera y estan en boxes

    public RaceJudge(int nVehicles,int nLaps) {
        this.N_VEHICLES = nVehicles;
        this.N_LAPS = nLaps;
        latchVehiclesReady = new CountDownLatch(N_VEHICLES);
        latchVehiclesRanked = new CountDownLatch(N_VEHICLES);
        latchVehiclesFinished = new CountDownLatch(N_VEHICLES);
    }

    @Override
    public void run() {
        try {
            System.out.println("RaceJudge: Esperando a que los vehiculos esten preparados...");
            latchVehiclesReady.await();

            System.out.println("RaceJudge: Todos los vehiculos estan preparados. Iniciando cuenta atras!");
            for (int i=COUNTDOWN; i>0; i--){
                System.out.println("RaceJudge: "+i);
                Thread.sleep(1000);
                if (i==1) System.out.println("RaceJudge: Empieza la carrera ya!!");
                latchCountdown.countDown();
            }
            latchVehiclesRanked.await();
            System.out.println("Racejudge: Todos los vehiculos han pasado la meta. Esperando a que terminen la vuelta extra");
            latchVehiclesFinished.await();
            System.out.println("Racejudge: Todos los vehiculos terminaron la carrera y estan en boxes. Terminando hilo del Juez...");

        } catch (InterruptedException e) {
            System.out.println("RaceJudge: La carrera se ha interrumpido. Terminando de forma ordenada...");
        }
    }
}
