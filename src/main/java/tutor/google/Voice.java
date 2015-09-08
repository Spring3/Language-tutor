package tutor.google;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tutor.models.Language;

import tutor.Main;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Spring on 9/5/2015.
 */
public class Voice {
    private Voice(){
        mediaQueue = new ArrayBlockingQueue<Media>(20);
        consumer = new Consumer(mediaQueue);
        new Thread(consumer).start();
    }

    static{
        try {
            MEDIA_ANSWER_CORRECT = Main.class.getClassLoader().getResource("sounds/correct.mp3").toURI().toString();
            MEDIA_ANSWER_WRONG = Main.class.getClassLoader().getResource("sounds/wrong.mp3").toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static String MEDIA_ANSWER_CORRECT;
    public static String MEDIA_ANSWER_WRONG;
    private static Voice instance;
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
    private MediaPlayer mediaPlayer;
    private BlockingQueue<Media> mediaQueue;
    private Consumer consumer;
    private volatile boolean isDisposed;
    private volatile boolean isPlaying;
    private File cacheDirectory;

    public static Voice getInstance(){
        if (instance == null){
            synchronized (Voice.class){
                if (instance == null){
                    instance = new Voice();
                }
            }
        }
        return instance;
    }

    /**
     * Uses google text to speech api to play the sound of the word, passed as a query for the request.
     * @param word a word to be spoken.
     * @param language the language of the word.
     */
    public void say(String word, Language language) {
        Media media = getMedia(word, language);
        if (media != null) {
            Producer producer = new Producer(mediaQueue, media);
            new Thread(producer).start();
        }
    }

    /**
     * Plays an .mp3 or .wav sound.
     * @param soundPath path to the sound file.
     */
    public void play(String soundPath){
        Media media = new Media(soundPath);
        Producer producer = new Producer(mediaQueue, media);
        new Thread(producer).start();
    }

    private Media getMedia(String word, Language language) {
        Media result = null;
        try {
            URL url = new URL(URL_TEMPLATE + URL_LANG_PARAM + language.getShortName() + URL_ENCODING_PARAM + URL_WORD_PARAM + prepareWordForQuery(word) + URL_CLIENT_PARAM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_GET);
            connection.setRequestProperty(USER_AGENT_PARAM, USER_AGENT);
            connection.setRequestProperty(REFERER_PARAM, REFERER);
            InputStream audioSrc = connection.getInputStream();
            createDirectory();
            File sound;
            if (!(sound = new File("cache/" + word + ".mp3")).exists()) {
                OutputStream outputStream = new FileOutputStream(new File("cache/" + word + ".mp3"));
                int count;
                byte[] buffer = new byte[1 << 20];
                while ((count = audioSrc.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                    outputStream.flush();
                }
                outputStream.close();
                audioSrc.close();
            }
            result = new Media(Paths.get("cache/" + word + ".mp3").toUri().toString());
        } catch (Exception ex) {
            System.err.println("No internet connection.");
        }
        return result;
    }

    private void createDirectory(){
        File soundDirectory = new File("cache");
        if (!soundDirectory.exists()){
            soundDirectory.mkdir();
        }
        cacheDirectory = soundDirectory;
    }

    private String prepareWordForQuery(String word){
        return word.replace(" ", "%20");
    }

    /**
     * Stops active threads, clears cache folder.
     */
    public synchronized void dispose(){
        isDisposed = true;
        cacheDirectory = cacheDirectory == null ? new File("cache") : cacheDirectory;
        if (cacheDirectory.exists()){
            for(File file : cacheDirectory.listFiles()){
                file.delete();
            }
        }
        cacheDirectory.delete();
    }

    private class Producer implements Runnable{
        protected BlockingQueue<Media> queue = null;
        private Media media;

        public Producer(BlockingQueue<Media> queue, Media media){
            this.queue = queue;
            this.media = media;
        }

        public void run(){
            try {
                queue.put(media);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Consumer implements Runnable{
        protected BlockingQueue<Media> queue = null;
        private MediaPlayer mediaPlayer;

        public Consumer(BlockingQueue<Media> queue){
            this.queue = queue;
        }

        public void run() {
            while(!isDisposed) {
                if (queue.size() == 0 || isPlaying) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    mediaPlayer = new MediaPlayer(queue.poll());
                    mediaPlayer.play();
                    isPlaying = true;
                    mediaPlayer.setOnEndOfMedia(() -> {
                        isPlaying = false;
                    });
                }
            }
        }
    }
}
