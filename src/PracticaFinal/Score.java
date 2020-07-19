package PracticaFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Score {

    int racelength; //distancia total de la carrera
    List<Racetrack> racetracks; //lista de tramos del circuito
    int racetrackLength; //distancia total del circuito
    private List<Vehicle> oldVehicles;


    public Score(int raceLength, List<Racetrack> racetracks, int racetrackLength) {
        this.racelength = raceLength;
        this.racetracks = racetracks;
        this.racetrackLength = racetrackLength;
        this.oldVehicles = new ArrayList<>();
    }

    /*showRanking() lo utilizamos para imprimir los datos cuando ya nadie puede tocar la coleccion de vehiculos (cuando
     han terminado los vehiculos la iteracion de cada simulacion dentro de los 5s). Este metodo indica los
     adelantamientos producidos en la iteracion actual*/
    public void showRanking(ConcurrentHashMap<Integer, Vehicle> vehiclesCopy) {
        //System.out.println("distancia carrera: " + racelength);
        AtomicInteger currentPosition = new AtomicInteger(1);
        System.out.println("-----------------------------------Score---------------------------------------");
        vehiclesCopy.values().stream()
                .sorted()
                .peek((v) -> {
                    if (currentPosition.get() > 1)
                        System.out.println("\n-------------------------------------------------------------------------------");
                    System.out.print(currentPosition.get() + "º :");
                    if (currentPosition.get() < getOldPosition(v)) {
                        System.out.println("Vehiculo " + v.NAME + " ▲");
                    } else if (getOldPosition(v) != 0 && currentPosition.get() > getOldPosition(v)) {
                        System.out.println("Vehiculo " + v.NAME + " ▼");
                    } else {
                        System.out.println("Vehiculo " + v.NAME);
                    }
                    currentPosition.getAndIncrement();
                    System.out.print("vuelta: " + (v.distTraveled / racetrackLength) + " | ");
                    System.out.print("dist recorrida: " + v.distTraveled + " | ");
                    int finishLineDist = racelength - v.distTraveled;
                    if (finishLineDist >= 0) {                   //no ha pasado la meta
                        System.out.print("distancia a meta: " + (racelength - v.distTraveled) + " | ");
                    } else if (-finishLineDist < racetrackLength) { //paso la meta pero todavia no ha dado la vuelta extra
                        System.out.print("Paso la meta. Vuelta extra dist. restante: " + (racetrackLength - (-finishLineDist)) + " | ");
                    } else {                                 //paso la meta y finalizo la vuelta extra
                        System.out.print("Finalizo la carrera. | ");
                    }
                    if (v.boxesDelay == 0 && !v.finished) {
                        System.out.print("velocidad: " + v.speed + " | ");
                        System.out.print("tramo: " + v.currentRacetrack + " | ");
                        System.out.print("combustible: " + v.fuel + " | ");
                    } else {
                        System.out.print("velocidad: 0 | ");
                        System.out.print("en boxes | ");
                    }
                })
                .mapToInt(v -> v.distTraveled)
                .reduce((d1, d2) -> {
                    System.out.print("distanciaConVehiculoDelante: " + (d1 - d2));
                    return d2;
                });
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
        oldVehicles = vehiclesCopy.values().stream().sorted().collect(Collectors.toList());
    }

    /*showFinalRanking() lo utilizamos para imprimir los datos del ranking final cuando ya todos los vehiculos han
    terminado la carrera y los vehiculos estan en boxes.*/
    public void showFinalRanking(ConcurrentHashMap<Integer, Vehicle> vehiclesCopy) {
        AtomicInteger currentPosition = new AtomicInteger(1);
        System.out.println("-----------------------------------Score---------------------------------------");
        vehiclesCopy.values().stream()
                .sorted()
                .peek((v) -> {
                    if (currentPosition.get() > 1)
                        System.out.println("\n-------------------------------------------------------------------------------");
                    System.out.print(currentPosition.getAndIncrement() + "º :");
                    System.out.println("Vehiculo " + v.NAME + " ");
                    System.out.print("vuelta: " + (v.distTraveled / racetrackLength) + " | ");
                    System.out.print("dist recorrida: " + v.distTraveled + " | ");
                    int finishLineDist = racelength - v.distTraveled;
                    if (finishLineDist >= 0) {                   //no ha pasado la meta
                        System.out.print("distancia a meta: " + (racelength - v.distTraveled) + " | ");
                    } else if (-finishLineDist < racetrackLength) { //paso la meta pero todavia no ha dado la vuelta extra
                        System.out.print("Paso la meta. Vuelta extra dist. restante: " + (racetrackLength - (-finishLineDist)) + " | ");
                    } else {                                 //paso la meta y finalizo la vuelta extra
                        System.out.print("Finalizo la carrera. | ");
                    }
                    if (v.boxesDelay == 0 && !v.finished) {
                        System.out.print("velocidad: " + v.speed + " | ");
                        System.out.print("tramo: " + v.currentRacetrack + " | ");
                        System.out.print("combustible: " + v.fuel + " | ");
                    } else {
                        System.out.print("velocidad: 0 | ");
                        System.out.print("en boxes | ");
                    }
                })
                .mapToInt(v -> v.distTraveled)
                .reduce((d1, d2) -> {
                    System.out.print("distanciaConVehiculoDelante: " + (d1 - d2));
                    return d2;
                });
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
    }

    /*Obtiene la posicion en el instante anterior del vehiculo pasado por parametro*/
    private int getOldPosition(Vehicle v) {
        int pos = 0;
        if (!oldVehicles.isEmpty()) {
            for (Vehicle ve : oldVehicles) {
                pos++;
                if (v.NAME == ve.NAME) break;
            }
        }
        return pos;
    }
}
