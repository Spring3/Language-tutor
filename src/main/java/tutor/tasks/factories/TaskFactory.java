package tutor.tasks.factories;

import tutor.models.Language;
import tutor.tasks.AbstractTask;
import tutor.tasks.ITask;

/**
 * Created by Spring on 4/9/2016.
 */
public abstract class TaskFactory {
    abstract ITask createTask(AbstractTask task);
    abstract ITask createRandomTask(Language languageToLearn);

}
