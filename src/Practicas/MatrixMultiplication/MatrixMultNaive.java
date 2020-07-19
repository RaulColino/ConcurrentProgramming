package Practicas.MatrixMultiplication;

/*
multithreaded naive version
one thread per element of the resultant matrix
*/
public class MatrixMultNaive {

    private static final int MAX = 2;

    private static int[][] m1;
    private static int[][] m2;
    private static int[][] mr;

    private static int nOperation; /*in 2x2 matrix there are 4 operations*/

    public static void multiply() {
        int displ = nOperation++;
        for (int i = displ / MAX; i < (displ + 1) / MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                for (int k = 0; k < MAX; k++) {
                    mr[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[MAX * MAX];
        nOperation = 0;
        for (int i = 0; i < MAX * MAX; i++) {

        }
        for (int i = 0; i < MAX * MAX; i++) {
            threads[i].join();
        }
    }
}
