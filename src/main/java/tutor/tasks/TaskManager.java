package tutor.tasks;

import tutor.controllers.AuthController;
import tutor.controllers.Controller;
import tutor.dao.StatsDAO;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Stats;
import tutor.models.Word;
import tutor.tasks.dictation.Dictation;
import tutor.tasks.factories.DictationFactory;

import java.util.*;

/**
 * Created by Spring on 8/30/2015.
 */
public class TaskManager {

    public TaskManager(Output outputMode){
        this.outputMode = outputMode;
    }

    private ITask currentTask;
    private int mistakes;
    private int correctAnswers;
    private Output outputMode;

    public enum Output{
        VOICE,
        TEXT;

        public static Output random(){
            Random rand = new Random();
            return values()[rand.nextInt(values().length)];
        }
    }

    public Output getOutputMode(){
        return outputMode;
    }

    public int getMistakesCount(){
        return mistakes;
    }

    public int getCorrectAnswersCount(){
        return correctAnswers;
    }

    public Word getCorrectWord(){
        return currentTask.getCorrectWord();
    }

    public String getCorrectAnswer(){
        return currentTask.getCorrectAnswer();
    }

    public void incrementMistakesCounter(){
        mistakes ++;
    }

    public void incrementCorrectAnswersCount(){
        correctAnswers ++;
    }

    public int getWordsAmount(){
        return currentTask.getWordsAmount();
    }

    public float getSuccessRate() {
        if (correctAnswers != 0) {
            if (mistakes != 0) {
                return (float) correctAnswers / currentTask.getWordsAmount() * 70 + (float) correctAnswers / mistakes + (float)(21 - mistakes / currentTask.getWordsAmount() * 21);
            } else {
                return (float) correctAnswers / currentTask.getWordsAmount() * 70 + 30;
            }
        } else {
            return (float) correctAnswers / currentTask.getWordsAmount() * 70 + 15 + (float) (15 - mistakes / currentTask.getWordsAmount() * 15);
        }
    }

    public void endTask(){
        for(Map.Entry<Word,Boolean> entry : currentTask.getPassedWords().entrySet()){
            WordDAO.getInstance().update(entry.getKey());
        }

        Stats statistics = new Stats(AuthController.getActiveUser(), currentTask.getTaskType(), Controller.selectedLanguage, getSuccessRate());
        StatsDAO.getInstance().create(statistics);
    }

    public String getNextTaskWord(){
        currentTask.getNextWord();
        return currentTask.getTaskWord();
    }

    public boolean confirmAnswer(String confirmedAnswer) {

        if (currentTask.getMode().equals(Dictation.Mode.REVERSED)) {
            if (currentTask.getCorrectWord().getArticle().get().isEmpty()) {
                return checkAnswer(confirmedAnswer);
            } else {
                return checkAnswer(confirmedAnswer, true);
            }
        } else {
            return checkAnswer(confirmedAnswer);
        }
    }

    private boolean checkAnswer(String enteredText, boolean hasArticle){
        boolean isCorrect = currentTask.check(enteredText, hasArticle);
        rememberAnswerResult(isCorrect);
        return isCorrect;
    }

    private boolean checkAnswer(String enteredText){
        boolean isCorrect = currentTask.check(enteredText);
        rememberAnswerResult(isCorrect);
        return isCorrect;
    }

    private void rememberAnswerResult( boolean isCorrect){
        if (isCorrect){
            if (currentTask.firstTry()) {
                incrementCorrectAnswersCount();
            }
        }
        else {
            incrementMistakesCounter();
        }
    }

    public ITask createTask(AbstractTask task){
        if (task == null)
            return null;

        switch (task.getTaskType()){
            case DICTATION:{
                currentTask = new DictationFactory().createTask(task);
                break;
            }
            default:{
                createRandomTask(task.getLanguage());
                break;
            }
        }
        return currentTask;
    }

    public ITask createRandomTask(Language language){
        Random random = new Random();
        TaskType type = TaskType.values()[random.nextInt(TaskType.values().length)];
        AbstractTask abstractTask = new AbstractTask(type, language);
        return createTask(abstractTask);
    }

}
