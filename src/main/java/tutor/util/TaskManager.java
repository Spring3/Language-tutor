package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Spring on 8/30/2015.
 */
public class TaskManager {
    public TaskManager(Language langToLearn){
        languageToLearn = langToLearn;
        wordsForTask = new ArrayList<>();
    }

    private static final int MAX_WORDS_PER_TASK = 10;
    private Language languageToLearn;
    private List<Word> wordsForTask;
    private TaskManagerMode mode;
    private DictationMode dictationMode;

    public TaskManagerMode getMode(){
        return mode;
    }

    public DictationMode getDictationMode() {return dictationMode;}


    public enum TaskManagerMode{
        NORMAL,
        REPETITION,
        LEARNING
    }

    public enum DictationMode{
        NORMAL,
        REVERSED
    }

    public List<Word> getWords(){
        return wordsForTask;
    }

    public int getMaxWordsPerTask(){
        return MAX_WORDS_PER_TASK;
    }


    public List<Word> createTask(){
        Random random = new Random();
        mode = TaskManagerMode.values()[random.nextInt(3)];
        dictationMode = DictationMode.values()[random.nextInt(2)];
        switch (mode){
            case NORMAL:{
                createNormalTask();
                break;
            }
            case REPETITION:{
                createRepetitionTask();
                break;
            }
            case LEARNING:{
                createLearningTask();
                break;
            }
            default:{
                createNormalTask();
                break;
            }
        }
        return wordsForTask;
    }

    private void createNormalTask(){
        List<Word> allWords = WordDAO.getInstance().readAllByLangForActiveUser(languageToLearn);
        fillArrayWithWords(allWords);
    }

    private void createRepetitionTask(){
        List<Word> wordsToRepeat = WordDAO.getInstance().readAllWordsToRepeatFor(languageToLearn);
        fillArrayWithWords(wordsToRepeat);
    }

    private void createLearningTask(){
        List<Word> newWords = WordDAO.getInstance().readAllNewlyAddedWordsFor(languageToLearn);
        fillArrayWithWords(newWords);
    }

    private void fillArrayWithWords(List<Word> allWords){
        wordsForTask.clear();
        Random rand = new Random();
        try {
            if (allWords.size() < MAX_WORDS_PER_TASK) {
                wordsForTask = allWords;
                allWords = WordDAO.getInstance().readAllByLangForActiveUser(languageToLearn);
                for (int i = wordsForTask.size(); i < MAX_WORDS_PER_TASK; i++) {
                    int randomIndex = rand.nextInt(allWords.size());
                    wordsForTask.add(allWords.get(randomIndex));
                    allWords.remove(randomIndex);
                }
            } else {
                for (int i = 0; i < MAX_WORDS_PER_TASK; i++) {
                    int randomIndex = rand.nextInt(allWords.size());
                    wordsForTask.add(allWords.get(randomIndex));
                    allWords.remove(randomIndex);
                }
            }
        }
        catch (NullPointerException ex){
            createNormalTask();
        }
    }
}
