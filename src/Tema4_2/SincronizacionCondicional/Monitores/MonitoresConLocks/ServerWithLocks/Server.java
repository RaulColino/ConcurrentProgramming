package Tema4_2.SincronizacionCondicional.Monitores.MonitoresConLocks.ServerWithLocks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
§ Con synchronized, sólo se puede tener una única variable en la que
bloquear y desbloquear hilos (la variable representada por el cerrojo).
§ En la exclusión mutua obtenida con cerrojos (locks) también se puede implementar
sincronización condicional y se pueden crear tantas variables condicionales como sea necesario
*/
public class Server {

    private ReentrantLock lock;
    private Condition opMaintenance;
    private Condition opRequest;
    private int numOpMaintenance; //controla el numero de operaciones de mantenimiento
    private int numOpRequest; //controla el numero de peticiones

    public Server() {
        lock = new ReentrantLock();
        opMaintenance = lock.newCondition();
        opRequest = lock.newCondition();
        numOpMaintenance = 0;
        numOpRequest = 0;
    }

    public void startMaintenance(String name) throws InterruptedException {
        lock.lock();
        try {
            while ((numOpRequest > 0) || (lock.getWaitQueueLength(opRequest) > 0)) { //hay una peticion
                System.out.println(name+" esperando... Servidor ocupado");
                opMaintenance.await(); //el mantenimiento tiene que esperar
            }
            numOpMaintenance++; //incrementa el numero de operaciones de mantenimiento
            opMaintenance.signal(); //despierta a las otras operaciones de mantenimiento
            System.out.println("Empieza "+name);
        } finally { //permite empezar las operaciones de mantenimiento
            lock.unlock();
        }
    }

    public void endMaintenance(String name) {
        lock.lock();
        try {
            /*(a > b) ? x : y; is an expression.If the condition, (a > b) it is true, the first value, x, is returned.
            If it is false, the second value, y, is returned.  The condition can be any expression which returns a boolean value.*/
            System.out.println(name+" terminado..." + ((this.numOpMaintenance == 1)?"servidor libre":"servidor ocupado"));
            numOpMaintenance--;
            if (numOpMaintenance == 0) {
                opRequest.signal(); //la ultima operacion de mantenimiento comunica a las peticiones
            }
        } finally {
            lock.unlock();
        }
    }

    public void startRequest(String name) throws InterruptedException {
        lock.lock();
        try {
            while ((numOpMaintenance > 0) || (lock.getWaitQueueLength(opMaintenance) > 0)) { //hay una operacion de mantenimiento
                System.out.println(name+" esperando... Servidor ocupado");
                opRequest.await(); //la peticion tiene que esperar
            }
            numOpRequest++;
            opRequest.signal();
            System.out.println("Empieza "+name);
        } finally {
            lock.unlock();
        }
    }

    public void endRequest(String name) {
        lock.lock();
        try {
            System.out.println(name+" terminado..." + ((this.numOpRequest == 1)?"servidor libre":"servidor ocupado"));
            numOpRequest--;
            if (numOpRequest == 0) {
                opMaintenance.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}


