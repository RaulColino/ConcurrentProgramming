package PracticaFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Race implements Runnable {

    final int N_VEHICLES;
    final int N_RACETRACKS;
    final int N_LAPS;
    public List<Racetrack> racetracks = new ArrayList<>();
    public Map<Integer, Vehicle> vehicles = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService;
    private final Score score;
    int crossedFinishLine;  //Numero de vehiculos que han llegado a la meta y estan en la vuelta extra o en boxes
    int raceLength;         //distancia total de la carrera
    int recewayLength;    //distancia del circuito
    int climaticEventType;
    int climaticEventDelay;
    int currentIteration;
    int nLanes;

    public Race(int nVehicles, int nRacetracks, int lanes, int nLaps, int climaticEventType, int climaticEventDelay) {
        this.N_VEHICLES = nVehicles;
        this.N_RACETRACKS = nRacetracks;
        this.N_LAPS = nLaps;
        executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        initRace();
        raceLength = N_LAPS * racetracks.stream().mapToInt(r -> r.length).sum();
        recewayLength = racetracks.stream().mapToInt(r -> r.length).sum();
        score = new Score(raceLength, racetracks, recewayLength);
        crossedFinishLine = 0;
        this.nLanes = (lanes > 0)? lanes: 1;
        this.climaticEventType = climaticEventType;
        this.climaticEventDelay = climaticEventDelay;
        this.currentIteration = 0;
    }

    @Override
    public void run() {
        /*Lanzamiento de tareas*/
        for (int i = 0; i < N_VEHICLES; i++) {
            executorService.scheduleAtFixedRate(vehicles.get(i), 0, 5, TimeUnit.SECONDS);
            System.out.println("Vehiculo " + i + " preparado!");
            RaceJudge.latchVehiclesReady.countDown();
        }
        executorService.scheduleAtFixedRate(() -> {
            System.out.println("Race: Ranking en el instante " + (currentIteration++) + ":");
            /*Necesitamos pasar una copia estatica de la coleccion de vehiculos porque Score guarda esa copia para la proxima iteracion.
            * De esta manera podemos averiguar si en la siguiente iteracion el vehiculo ha adelantado o no o si ha sido adelantado)*/
            score.showRanking(new ConcurrentHashMap<>(vehicles));
        }, 4, 5, TimeUnit.SECONDS);
        if (climaticEventType != 0)
            executorService.schedule(new ClimaticEvents(climaticEventType, this), climaticEventDelay * 5, TimeUnit.SECONDS);
        /*Espera por la finalizacion de los vehiculos*/
        System.out.println("Race: carrera en espera de finalizacion de los vehiculos...");
        try {
            //Los vehiculos hacen countdown antes de terminar
            RaceJudge.latchVehiclesFinished.await();
            System.out.println("Race: Todos los vehiculos terminaron la carrera y estan en boxes. Terminando la carrera de forma ordenada...");
            executorService.shutdown();
        } catch (InterruptedException e) {
            stopRace();
        }
        //se muestran los resultados finales de la carrera
        System.out.println("Race: Ranking final:");
        score.showFinalRanking(new ConcurrentHashMap<>(vehicles));
    }

    /*Creacion del circuito y vehiculos*/
    private void initRace() {
        Random random = new Random();
        for (int i = 0; i < N_RACETRACKS; i++) {
            int type = random.nextInt(2); //entre 0 y 1
            int length = random.nextInt(6) + 3;  //longitud entre 3 y 8
            System.out.println("racetrack " + i + "> type: " + type + " length: " + length);
            racetracks.add(new Racetrack(type, length));
        }
        for (int i = 0; i < N_VEHICLES; i++) {
            //para mayor competitividad los que tienen mas velocidad en curva tienen menos en recta y viceversa
            int velocidadEnCurva = random.nextInt(3) + 2;  //entre 2 y 4
            int velocidadEnRecta = (random.nextInt(1) + 7) - velocidadEnCurva;  //entre 7 y 8 - (1-4) = entre 3 y 6
            int capacidadDeposito = random.nextInt(2) + 20 - (velocidadEnRecta + velocidadEnCurva); //entre 20 y 21 menos (2-6 mas 1-3) los mas rapidos suelen quedarse sin combustible antes
            System.out.println("vehicle " + i + "> capDeposito: " + capacidadDeposito + " velEnRecta: " + velocidadEnRecta + " velEnCurva: " + velocidadEnCurva);
            vehicles.put(i, new Vehicle(this, i, capacidadDeposito, velocidadEnRecta, velocidadEnCurva));
        }
    }

    /*Metodo que se ejecuta al interrumpirse la carrera*/
    private void stopRace() {
        System.out.println("Race: La carrera ha sido interrumpida. Terminando de forma ordenada...");
        vehicles.forEach((k, v) -> v.finished = true);
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedException) {
            System.out.println("Race: interrumpida la espera a la finalizacion del ejecutor");
        }
    }

    /*Obliga a cambiar los neumaticos a todos los vehiculos durante 5 instantes en boxes*/
    public void rainEvent() {
        System.out.println("Race: Los vehiculos van a cambiar sus neumaticos en boxes durante 5 instantes");
        vehicles.forEach((k, v) -> v.changeTires());
    }

    /*Movimiento atomico de cada vehiculo durante la carrera*/
    public synchronized void moveVehicle(Vehicle vehicle, int speed) {
        int avancesRestantes = speed;
        while (avancesRestantes > 0) {
            //si hay espacio para avanzar una unidad de distancia adelante, avanza.(los vehiculos en boxes o que han acabado la carrera no bloquean el paso)
            if (vehicles.values().stream().filter(v -> (v.distTraveled == (vehicle.distTraveled + 1)) && (v.boxesDelay == 0) && (!v.finished)).count() < nLanes) {
                vehicle.distTraveled++;
                avancesRestantes--;
                System.out.println("Race: vehiculo " + vehicle.NAME + " -> avanzo a una distancia de " + vehicle.distTraveled);
            } else { //si tiene vehiculos delante ocupando todos los carriles no puede avanzar
                System.out.println("Race: vehiculo " + vehicle.NAME + " -> quedo bloqueado por coches de delante");
                avancesRestantes = 0;
            }
        }
    }
}
