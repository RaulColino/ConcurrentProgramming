package Practicas.VowelCounter;

import java.util.Scanner;

public class VowelCounterSecuential {

    public static final char[] vowels = {'a','e','i','o','u'};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String sentence;
        int nVowels=0;
        System.out.println("Write something:");
        sentence = sc.nextLine();
        for (int i = 0; i < sentence.length(); i++) {
            for (int j = 0; j < 5; j++) {
                if (sentence.charAt(i)== vowels[j])
                    nVowels++;
            }
        }
        System.out.println("number of vowels: "+nVowels);
      /*  StringTokenizer st = new StringTokenizer(frase);
        int nPalabras = st.countTokens();*/
    }
}
