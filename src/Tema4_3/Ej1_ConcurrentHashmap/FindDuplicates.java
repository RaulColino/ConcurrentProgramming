package Tema4_3.Ej1_ConcurrentHashmap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FindDuplicates {
    private static Map<String, String> duplicates = new HashMap<String, String>();                  /*Hashmap-> NombreFichero: RutaFichero*/

    public static void findDuplicates(File root) {                                     /*'root' es el directorio especificado por el usuario desde el que parte la busqueda*/
        if (root.isDirectory()) {                                                      /*Si la carpeta especificada por el usuario es un directorio empieza la busqueda*/
            for (File file : root.listFiles()) {                                       /*Se recorre la lista de elementos del directorio*/
                if (file.isDirectory()) {                                              /*Si se encuentra un nuevo directorio recursivamente inicia una nueva busqueda ahi*/
                    findDuplicates(file);
                } else {                                                               /*Si se encuentra un archivo comprueba si ya se ha encontrado uno igual*/
                    String path = duplicates.get(file.getName());
                    if (path == null) {                                                /*Si no se ha encontrado el archivo anteriormente se añade al hashmap de archivos encontrados*/
                        duplicates.put(file.getName(), file.getAbsolutePath());
                    } else {                                                           /*Si se ha encontrado el archivo anteriormente se muestra por pantalla el path del archivo anterior y el nuevo de nombre repetido*/
                        System.out.println("Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                    }
                }

            }

        }
    }

    public static void main(String[] args) {
        findDuplicates(new File("D:\\MisArchivos"));
    }
}
