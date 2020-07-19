package PracticaFinal;

public class Racetrack {

    int racetrackType;  //0 recta, 1 curva
    int length;

    public Racetrack(int racetrackType, int length) {
        if (racetrackType==0 || racetrackType==1) {
            this.racetrackType = racetrackType;
        }else{
            System.out.println("Racetrack: "+racetrackType+" no es un tipo de tramo valido.");
            System.out.println("Racetrack: los tipos de tramo validos son 1 para tramo recto y 2 para tramo curvo");
        }
        if (length > 0){
            this.length = length;
        }
    }

}
