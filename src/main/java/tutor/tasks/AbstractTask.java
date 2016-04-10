package tutor.tasks;

import tutor.models.Language;
import tutor.tasks.dictation.AbstractDictation;

/**
 * Created by Spring on 4/10/2016.
 */
public class AbstractTask {

    //For basic/random task
    public AbstractTask(TaskType taskType, Language langToLearn){
        this.taskType = taskType;
        language = langToLearn;
    }

    //For concrete dictation
    public AbstractTask(TaskType taskType, AbstractDictation dictation, Language langToLearn){
        this.taskType = taskType;
        this.dictation = dictation;
        language = langToLearn;
    }


    private TaskType taskType;
    private AbstractDictation dictation;
    private Language language;


    public TaskType getTaskType(){
        return taskType;
    }

    public AbstractDictation getDictationData(){
        return dictation;
    }

    public Language getLanguage(){
        return language;
    }
}
