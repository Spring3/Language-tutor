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

    public TaskManagerMode getMode(){
        return mode;
    }


    public enum TaskManagerMode{
        NORMAL,
        REVERSED,
        REPETITION,
        LEARNING
    }

    public List<Word> getWords(){
        return wordsForTask;
    }

    public int getMaxWordsPerTask(){
        return MAX_WORDS_PER_TASK;
    }


    public List<Word> createTask(){
        /*Random random = new Random();
        TaskManagerMode mode = TaskManagerMode.values()[random.nextInt(4)];*/
        mode = TaskManagerMode.NORMAL;
        switch (mode){
            case NORMAL:{
                createNormalTask();
                break;
            }
            /*case REVERSED:{
                createReversedTask();
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
            }      */
        }
        return wordsForTask;
    }

    private void createNormalTask(){
        Random rand = new Random();
        List<Word> allWords = WordDAO.getInstance().readAllByLangForActiveUser(languageToLearn);
        if (allWords.size() < MAX_WORDS_PER_TASK)    {
            wordsForTask = allWords;
        }
        else{
            wordsForTask.clear();
            for(int i = 0; i < MAX_WORDS_PER_TASK; i ++){
                int randomIndex = rand.nextInt(allWords.size());
                wordsForTask.add(allWords.get(randomIndex));
                allWords.remove(randomIndex);
            }
        }
    }

    private void createReversedTask(){
    }

    private void createRepetitionTask(){
    }

    private void createLearningTask(){
    }
}
