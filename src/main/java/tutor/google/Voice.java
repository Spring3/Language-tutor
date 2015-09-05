package tutor.google;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tutor.models.Language;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by Spring on 9/5/2015.
 */
public class Voice {
    public Voice(){

    }

    private static final String URL_TEMPLATE = "http://translate.google.com/translate_tts?";
    private static final String URL_LANG_PARAM = "tl=";
    private static final String URL_WORD_PARAM = "&q=";
    private static final String URL_ENCODING_PARAM = "&ie=UTF-8";
    private static final String URL_CLIENT_PARAM = "&client=t";
    private static final String USER_AGENT_PARAM = "User-Agent";
    private static final String REQUEST_GET = "GET";
    private static final String REFERER_PARAM = "REFERER";
    private static final String REFERER = "http://translate.google.com/";
    private static final String USER_AGENT = "Mozilla/5.0";

    public void play(String word, Language language){
        try{
            URL url = new URL(URL_TEMPLATE + URL_LANG_PARAM + language.getShortName() + URL_ENCODING_PARAM + URL_WORD_PARAM + prepareWordForQuery(word) + URL_CLIENT_PARAM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_GET);
            connection.setRequestProperty(USER_AGENT_PARAM, USER_AGENT);
            connection.setRequestProperty(REFERER_PARAM, REFERER);
            InputStream audioSrc = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File("1.mp3"));
            int count;
            byte[] buffer = new byte[1 << 20];
            while((count = audioSrc.read(buffer)) != -1){
                outputStream.write(buffer, 0, count);
                outputStream.flush();
            }
            outputStream.close();
            audioSrc.close();
            Media media = new Media(Paths.get("1.mp3").toUri().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String prepareWordForQuery(String word){
        return word.replace(" ", "%20");
    }
}
