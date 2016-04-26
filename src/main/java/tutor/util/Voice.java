package tutor.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.datatypes.MaryDataType;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import tutor.models.Language;
import tutor.Main;
import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.net.URISyntaxException;
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
    private BlockingQueue<Media> mediaQueue;
    private Consumer consumer;
    private volatile boolean isDisposed;
    private volatile boolean isPlaying;
    private File cacheDirectory;
    private MaryInterface marytts;
    private AudioPlayer ap;

    public enum SupportedLanguages{
        ENGLISH("dfki-spike-hsmm"),
        GERMAN("bits3-hsmm"),
        FRENCH("upmc-pierre-hsmm"),
        ITALIAN("istc-lucia-hsmm"),
        //RUSSIAN("voxforge-ru-nsh"),
        TELUGU("cmu-slt-hsmm"),
        TURKISH("dfki-ot-hsmm");

        private String voice;

        SupportedLanguages(String voice){
            this.voice = voice;
        }

        public String getVoice(){
            return voice;
        }

        public static boolean contains(Language lang){
            for(SupportedLanguages language : values()){
                if (language.name().toLowerCase().equals(lang.getLangName().toLowerCase()))
                    return true;
            }
            return false;
        }
    }

    public static Voice getInstance(){
        if (instance == null){
            synchronized (Voice.class){
                if (instance == null) {
                    instance = new Voice();

                    Thread thread = new Thread(() -> {
                        try {
                            instance.marytts = new LocalMaryInterface();


                        } catch (MaryConfigurationException ex) {
                            ex.printStackTrace();
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
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
        try{
            if (SupportedLanguages.contains(language)) {
                String voice = SupportedLanguages.valueOf(language.getLangName().toUpperCase()).getVoice();
                if (voice != null)
                    marytts.setVoice(voice);
                marytts.setOutputType(MaryDataType.AUDIO.name());
                marytts.setInputType(MaryDataType.TEXT.name());
                AudioInputStream audio = marytts.generateAudio(word);
                ap = new AudioPlayer();
                ap.setAudio(audio);
                ap.start();
            }
        }
        catch (SynthesisException ex){
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
