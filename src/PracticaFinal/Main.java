package PracticaFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Main {

    private static Thread thread_race;
    private static Thread thread_racejudge;

    public static void main(String[] args) {
        int nVehicles = 4;
        int nRacetracks = 4;
        int lanes = 2; // numero de carriles del circuito
        int nLaps = 1;

        int climaticEventType = 1; //0 dia soleado, 1 lluvia normal, 2 lluvia torrencial
        int climaticEventDelay = 3; // instante en el que empieza el evento climatico

        Race race = new Race(nVehicles, nRacetracks, lanes, nLaps, climaticEventType, climaticEventDelay);
        RaceJudge raceJudge = new RaceJudge(nVehicles, nLaps);

        System.out.println("Main: Creando thread de la clase Race...");
        thread_race = new Thread(race);
        System.out.println("Main: Creando thread de la clase RaceJudge...");
        thread_racejudge = new Thread(raceJudge);
        System.out.println("Main: Creando thread de espera del Input...");
        Thread thread_input = new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            FutureTask<String> awaitInput = new FutureTask<>(br::readLine);
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(awaitInput);
            try {
                System.out.println(awaitInput.get());
                System.out.println("thread_input: Se ha pulsado ENTER! Terminando el programa...");
                cancelRace();
                br.close();
            } catch (ExecutionException | IOException ex) {
                System.out.println("thread-input: error durante la espera de entrada por teclado");
            } catch (InterruptedException e) {
                System.out.println("thread_input: El programa ya ha terminado. Este hilo sera terminado.");
            } finally {
                awaitInput.cancel(true);
                executor.shutdown();
            }
        });

        System.out.println("Main: Iniciando hilos...");
        thread_input.start();
        thread_race.start();
        thread_racejudge.start();

        try {
            thread_race.join();
            System.out.println("Main: Termino el hilo de la Carrera");
            thread_racejudge.join();
            System.out.println("Main: Termino el hilo del Juez");
            thread_input.interrupt();
            thread_input.join();
            System.out.println("Main: Termino el hilo de entrada por teclado");
        } catch (Throwable e) {
            System.out.println("Main: Excepcion al hacer join()");
            e.printStackTrace();
        }

        System.out.println("Main: Fin del programa");
        System.exit(0);
    }

    /*Este metodo se ejecuta al detectar entrada por teclado o al producirse lluvia torrencial*/
    public static void cancelRace() {
        thread_race.interrupt();
        thread_racejudge.interrupt();
    }

}
