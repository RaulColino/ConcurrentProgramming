package PracticaFinal;

public class Vehicle implements Runnable, Comparable<Vehicle> {
    //atributos
    int distTraveled;         //espacio recorrido por el coche
    int fuel;           //combustible restante del vehiculo
    int finalRankingPosition;  //posicion final tras acabar la carrera
    int speed;
    int currentRacetrack;
    boolean finished;
    int boxesDelay;            //simulaciones que tiene que esperar en boxes

    //parametros fijos
    final int NAME;
    final int STRAIGHT_TRACK_SPEED;    //velocidad fija en recta
    final int CURVED_TRACK_SPEED;     //velocidad fija en curva
    final int CAPACIDAD_D;            //valor de combustible maximo que permite el deposito
    final Race race;

    public Vehicle(Race race, int name, int capacidadDeposito, int velocidadEnRecta, int velocidadEnCurva) {
        this.distTraveled = 0;
        this.fuel = capacidadDeposito;
        this.finalRankingPosition = Integer.MAX_VALUE;
        this.speed = 0;
        this.currentRacetrack = 0;
        this.finished = false;
        this.boxesDelay = 0;

        this.NAME = name;
        this.STRAIGHT_TRACK_SPEED = velocidadEnRecta;
        this.CURVED_TRACK_SPEED = velocidadEnCurva;
        this.CAPACIDAD_D = capacidadDeposito;
        this.race = race;
    }

    @Override
    public void run() {
        if (!finished) {
            /*Espera a la señal del juez*/
            if (RaceJudge.latchCountdown.getCount() != 0) {
                System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): Esperando señal del juez");
                try {
                    RaceJudge.latchCountdown.await();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): Interrumpido durante la espera de la salida");
                    return;
                }
            }
            /*Si no esta en boxes*/
            if (boxesDelay == 0) {
                /*Si todavia no ha llegado a la meta el vehiculo, avanza*/
                if (finalRankingPosition > race.N_VEHICLES) {
                    System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): corriendo");
                    move();
                    /*si ha superado la meta empieza el proceso de finalizacion de carrera*/
                    if (distTraveled > race.raceLength) {
                        /*Todos los coches de la misma carrera deben sincronizarse para acceder a este bloque*/
                        synchronized (race) {
                            System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): Paso la meta");
                            finalRankingPosition = (int) (race.N_VEHICLES + 1 - RaceJudge.latchVehiclesFinished.getCount());
                            System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): Termino la carrera con posicion: " + finalRankingPosition);
                            RaceJudge.latchVehiclesRanked.countDown();
                        }
                    }
                } else { /*Si ya paso la meta y no ha dado la vuelta extra sigue avanzando*/
                    System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): corriendo vuelta extra");
                    move();
                    if (distTraveled > (race.raceLength + race.recewayLength)) { /*termina la carrera*/
                        System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): acaba de terminar la carrera y esta entrando a boxes");
                        RaceJudge.latchVehiclesFinished.countDown();
                        finished = true;
                    }
                }
            } else { /*esta en boxes*/
                System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): esperando en boxes. turno a esperar: " + boxesDelay);
                boxesDelay--;
            }
        } else {/*ha finalizado la carrera y la vuelta extra*/
            System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): termino carrera y esta en boxes");
        }
    }

    //actualiza la velocidad, distancia recorrida y el tramo en el que se encuentra el vehiculo tras la iteracion
    private void move() {
        /*Actualizamos la velocidad del vehiculo*/
        speed = (race.racetracks.get(currentRacetrack).racetrackType == 0) ? STRAIGHT_TRACK_SPEED : CURVED_TRACK_SPEED;
        race.moveVehicle(this, speed);
        /*Actualizamos el tramo en el que esta el vehiculo*/
        int distance = 0;
        for (Racetrack t : race.racetracks) {
            distance += t.length;
            if ((distTraveled - 1) % race.recewayLength < distance) {
                currentRacetrack = race.racetracks.indexOf(t);
                break;
            }
        }
        /*Actualizamos el valor del combustible y si se queda sin combustible se envia a boxes*/
        fuel -= speed;
        if (fuel <= 0) {
            System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): se quedo sin combustible ");
            awaitInBoxes(2);
            fuel = CAPACIDAD_D; //rellenamos el deposito a su maxima capacidad para cuando salga de boxes
        }

    }

    /*Este metodo se ejecuta cuando el vehiculo se queda sin combustible y cuando tiene que cambiar los neumaticos*/
    public synchronized void awaitInBoxes(int delay) {
        boxesDelay += delay;            //tiene que esperar 2 simulaciones para repostar
        System.out.println(Thread.currentThread().getName() + "(vehicle " + NAME + "): Espera de " + delay + " añadida. Espera total: " + boxesDelay);
    }

    public void changeTires() {
        awaitInBoxes(5);
    }

    @Override
    public int compareTo(Vehicle v) {
        if (this.finalRankingPosition > v.finalRankingPosition) {
            return 1;
        } else if (this.finalRankingPosition < v.finalRankingPosition) {
            return -1;
        } else {
            if (this.distTraveled < v.distTraveled) return 1;
            else if (this.distTraveled > v.distTraveled) return -1;
            else return 0;
        }
    }

}
