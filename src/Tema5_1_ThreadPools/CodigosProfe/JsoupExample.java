package Tema5_1_ThreadPools.CodigosProfe;

import org.jsoup.Connection;
import org.jsoup.Connection.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.UnknownHostException;

public class JsoupExample {
    public static void main(String[] args) throws IOException {
        // Correcto
        checkCode("https://wikipedia.es");
        checkCode("http://example.org");
        checkCode("https://ourworldindata.org/coronavirus");

        // Errores
        checkCode("https://google.com.es");
        checkCode("https://google.virus");
    }

    public static boolean checkCode(String url) throws IOException {
        try {
            Connection conection = Jsoup.connect(url);
            Response response = conection.execute();
            if (response.statusCode() == 200) {
                System.out.println("Web OK " + response.statusCode() + " url: " + url);
                return true;
            }
            System.out.println("Web KO " + response.statusCode() + " url: " + url);
        } catch (UnknownHostException ignored) {
            System.out.println("Error with url: " + url);
        }
        return false;
    }
}
