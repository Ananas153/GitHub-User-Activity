package nam.nam;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        String GIT_URL_OF_USER = "https://api.github.com/users/";
        GIT_URL_OF_USER = GIT_URL_OF_USER + args[0] + "/events";
        HttpsURLConnection urlConnection = null;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        try {
            URL gitUrl = new URL(GIT_URL_OF_USER);
            urlConnection = (HttpsURLConnection) gitUrl.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(10000);

            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append('\n');
            }

            System.out.println(stringBuilder);

        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }
}