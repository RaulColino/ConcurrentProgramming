package Tema5_1_ThreadPools.CodigosProfe;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FindDuplicatesSolution {

    private static Map<String, String> duplicatesIter = new HashMap<>();
    private static Map<String, String> duplicatesSyncObj = new HashMap<>();
    private static Map<String, String> duplicatesSyncLock = new HashMap<>();
    private static Map<String, String> duplicatesSync = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, String> duplicatesConc = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> duplicatesConc2 = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    // Nota:
    // Todo está con synchronized, pero hay otros métodos para sicronizar bloques de código.
    // Revisar el código y si algo está raro avisadme.


    public static void findDuplicatesIterativeRoot(File root) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                findDuplicatesIterative(file);
            }
        }
    }

    public static void findDuplicatesIterative(File root) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    findDuplicatesIterative(file);
                } else {
                    String path = duplicatesIter.get(file.getName());
                    if (path == null) {
                        duplicatesIter.put(file.getName(), file.getAbsolutePath());
                    } else {
                        System.out.println("Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public static void findDuplicatesSyncLock(File root, List<Thread> threads) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (!file.isDirectory()) continue;
                Thread th = new Thread(() -> findDuplicatesSyncThreadLock(file));
                th.start();
                threads.add(th);
            }
        }
    }

    public static void findDuplicatesSyncThreadLock(File root) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    findDuplicatesSyncThreadLock(file);
                } else {
                    // Esto es un ejemplo si queremos hacer las cosas por separado, primero el get
                    //  y luego el put. Fijaros dónde están los unlock...
                    lock.lock();
                    String path = duplicatesSyncLock.get(file.getName());
                    if (path == null) {
                        duplicatesSyncLock.put(file.getName(), file.getAbsolutePath());
                        lock.unlock();
                    } else {
                        lock.unlock();
                        // Pero no se sincronizarían los prints, tendría que solucionarse...
                        System.out.println("Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public static void findDuplicatesSyncObj(File root, List<Thread> threads) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (!file.isDirectory()) continue;
                Thread th = new Thread(() -> findDuplicatesSyncThreadObj(file));
                th.start();
                threads.add(th);
            }
        }
    }

    public static void findDuplicatesSyncThreadObj(File root) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    findDuplicatesSyncThreadObj(file);
                } else {
                    synchronized (duplicatesSyncObj) {
                        String path = duplicatesSyncObj.get(file.getName());
                        if (path == null) {
                            duplicatesSyncObj.put(file.getName(), file.getAbsolutePath());
                        } else {
                            System.out.println("Found duplicate file: " + file.getName());
                            System.out.println(" " + path);
                            System.out.println(" " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }


    public static void findDuplicatesSync(File root, List<Thread> threads) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (!file.isDirectory()) continue;
                Thread th = new Thread(() -> findDuplicatesSyncThread(file));
                th.start();
                threads.add(th);
            }
        }
    }

    public static void findDuplicatesSyncThread(File root) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                findDuplicatesSyncThread(file);
            } else {
                // Repasar el cerrojo de los tipos de datos sincronizados.
                synchronized (duplicatesSync) {
                    String path = duplicatesSync.get(file.getName());
                    if (path == null) {
                        duplicatesSync.put(file.getName(), file.getAbsolutePath());
                    } else {
                        System.out.println("Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public static void findDuplicatesConc(File root, List<Thread> threads) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (!file.isDirectory()) continue;
                Thread th = new Thread(() -> findDuplicatesConcThread(file));
                th.start();
                threads.add(th);
            }
        }
    }

    // Está MAL
    public static void findDuplicatesConcThread(File root) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                findDuplicatesConcThread(file);
            } else {
                // Nos falta sincronizarlos, no vale solo con cambiar el tipo de datos
                String path = duplicatesConc.get(file.getName());
                // En este momento alguien podría insertar un fichero con el mismo nombre
                if (path == null) {
                    // O aquí y al hacer el put cambiamos sin querer.
                    duplicatesConc.put(file.getName(), file.getAbsolutePath());
                } else {
                    System.out.println("Found duplicate file: " + file.getName());
                    System.out.println(" " + path);
                    System.out.println(" " + file.getAbsolutePath());
                }
            }
        }
    }


    public static void findDuplicatesConc2(File root, List<Thread> threads) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (!file.isDirectory()) continue;
                Thread th = new Thread(() -> findDuplicatesConcThread2(file));
                th.start();
                threads.add(th);
            }
        }

    }

    public static void findDuplicatesConcThread2(File root) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                findDuplicatesConcThread2(file);
            } else {
                String actualpath = file.getAbsolutePath();
                // Recordar que también es válido un put normal, pero sobreescribe el valor.
                String path = duplicatesConc2.put(file.getName(), actualpath);
                //String path = duplicatesConc2.putIfAbsent(file.getName(), actualpath);

                // Sincronizamos la salida, pero hay otras soluciones.
                synchronized (lock) {
                    if (path != null) {
                        System.out.println("Found duplicate file: " + file.getName());
                        System.out.println(" " + path);
                        System.out.println(" " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // TODO: poner vuestra carpeta con duplicados
        String carpeta = "/Users/cx02166/Desktop/ProgramacionConcurrente/Programacion Concurrente (PC)/Practicas_2020/PC2020";

        long startTime = System.nanoTime();
        findDuplicatesIterativeRoot(new File(carpeta));
        long iterativeTime = System.nanoTime() - startTime;

        // Dónde guardaremos los threads
        List<Thread> threads = new ArrayList<>(100);

        startTime = System.nanoTime();
        findDuplicatesSync(new File(carpeta), threads);
        for (Thread thread : threads) {
            thread.join();
        }
        long syncTime = System.nanoTime() - startTime;

        threads.clear();
        startTime = System.nanoTime();
        findDuplicatesSyncLock(new File(carpeta), threads);
        for (Thread thread : threads) {
            thread.join();
        }
        long lockTime = System.nanoTime() - startTime;

        threads.clear();
        startTime = System.nanoTime();
        findDuplicatesSyncObj(new File(carpeta), threads);
        for (Thread thread : threads) {
            thread.join();
        }
        long syncObjTime = System.nanoTime() - startTime;

        threads.clear();
        startTime = System.nanoTime();
        findDuplicatesConc(new File(carpeta), threads);
        for (Thread thread : threads) {
            thread.join();
        }
        long concTime = System.nanoTime() - startTime;

        threads.clear();
        startTime = System.nanoTime();
        findDuplicatesConc2(new File(carpeta), threads);
        for (Thread thread : threads) {
            thread.join();
        }
        long concTime2 = System.nanoTime() - startTime;

        System.out.println();
        System.out.println("Iterative : " + iterativeTime / 1000);
        System.out.println("Lock : " + lockTime / 1000);
        System.out.println("Sync : " + syncTime / 1000);
        System.out.println("SyncObj : " + syncObjTime / 1000);
        System.out.println("Conc : " + concTime / 1000);
        System.out.println("Conc2 : " + concTime2 / 1000);
    }
}