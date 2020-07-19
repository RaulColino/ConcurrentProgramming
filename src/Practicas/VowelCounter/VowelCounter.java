package Practicas.VowelCounter;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

/*
The string tokenizer class allows an application to break a string into tokens.
The tokenization method is much simpler than the one used by the StreamTokenizer class.
The StringTokenizer methods do not distinguish among identifiers, numbers, and
quoted strings, nor do they recognize and skip comments.
 */

/*
String[] listaPalabras = frase.split("\\s+");
**/
public class VowelCounter {

    private static final int nThreads = 4;
    private static final char[] vowels = {'a', 'e', 'i', 'o', 'u'};
    private static String sentence;
    private static int nVowels;

    private static Semaphore sem;
    private static Semaphore sem2;


    private static void countVowels(String word) {
        new Thread(() -> {
            try {
                for (int i = 0; i < word.length(); i++) {
                    for (int j = 0; j < 5; j++) {
                        if (word.charAt(i) == vowels[j]) {
                            sem2.acquire();
                            nVowels++;
                            sem2.release();
                        }
                    }
                }
                sem.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        String sentence;
        int nVowels = 0;
        sem = new Semaphore(1);
        sem2 = new Semaphore(1);

        Scanner sc = new Scanner(System.in);
        System.out.println("Write something:");
        sentence = sc.nextLine();

        StringTokenizer st = new StringTokenizer(sentence);
        int nWords = st.countTokens();
        for (int i = 0; i < nThreads - 1; i++) {
            for (int j = 0; j < nWords / nThreads - 1; j++) {
                String word = st.nextToken();
                countVowels(word);
            }
        }
        /*El ultimo thread ejecuta el resto*/
        String remainingWords = "";
        while (st.hasMoreTokens()) {
            remainingWords = remainingWords.concat(st.nextToken());
        }
        countVowels(remainingWords);
        for (int i = 0; i < nThreads ; i++) {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("number of vowels: "+nVowels);
    }
}

