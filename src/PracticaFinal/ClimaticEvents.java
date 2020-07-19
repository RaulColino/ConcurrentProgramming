package PracticaFinal;

public class ClimaticEvents implements Runnable {

    int type;//0 dia soleado, 1 lluvia normal, 2 lluvia torrencial
    Race race;

    public ClimaticEvents(int climaticEventType, Race race) {
        if (type < 0 || type > 2) {
            System.out.println("ClimaticEvents: Error al asignar el tipo. El tipo debe ser 0, 1 o 2");
        } else {
            type = climaticEventType;
            this.race = race;
        }
    }

    @Override
    public void run() {
        if (type == 1) {
            System.out.println("ClimaticEvents: Ha empezado a llover...");
            race.rainEvent();
        } else if (type == 2) {
            System.out.println("ClimaticEvents: Ha empezado a caer una Lluvia torrencial...");
            Main.cancelRace();
        } else {
            System.out.println("ClimaticEvents: error: tipo de evento asignado no valido");
        }
    }
}
