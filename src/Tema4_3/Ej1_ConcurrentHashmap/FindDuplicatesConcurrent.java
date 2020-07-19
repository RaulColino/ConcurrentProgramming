package Tema4_3.Ej1_ConcurrentHashmap;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FindDuplicatesConcurrent {

/*Colecciones concurrentes:
•Estas clases están diseñadas para el acceso concurrente desde varios hilos de ejecución.
•Ofrecen mucho mejor rendimiento que las colecciones sincronizadas y deberían usarse siempre que se necesite una colección compartida entre hilos*/

    private static Map<String, String> duplicates = new ConcurrentHashMap<String, String>(); /*Interfaz 'Map' -> Clase 'ConcurrentHashMap'*/
    static Lock lock = new ReentrantLock(); /*Los objetos Lock implementan la misma funcionalidad que las sentencias sincronizadas pero con más posibilidades*/

    public static void findDuplicates(File root) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    new Thread(() -> {
                        findDuplicates(file);
                    }, "Proceso de la carpeta " + file.getName()).start();
                } else {
                    /*putIfAbsent-> Returns the previous value associated with the specified key, or null if there was no mapping for the key. (A null return can also indicate that the map previously associated null with the key, if the implementation supports null values.)*/
                    /*putIfAbsent->Metodo unitario que añade el nombre y ruta de un archivo si no esta al hashmap y devuelve null.En caso contrario  devuelve el path del archivo encontrado anteriormente*/
                    String path = duplicates.putIfAbsent(file.getName(), file.getAbsolutePath());
                    if (path != null) {
                        lock.lock(); /*Las salidas por pantalla deben hacerse de forma unitaria*/
                        System.out.println(Thread.currentThread().getName() + ": Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                        lock.unlock();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        findDuplicates(new File("D:\\MisArchivos"));
    }
}
