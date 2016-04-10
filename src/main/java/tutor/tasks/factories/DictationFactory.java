package tutor.tasks.factories;

import tutor.models.Language;
import tutor.tasks.*;
import tutor.tasks.dictation.*;

import java.util.Random;

/**
 * Created by Spring on 4/10/2016.
 */
public class DictationFactory extends TaskFactory {
    @Override
    public ITask createTask(AbstractTask task) {
        ITask result;

        if (task.getTaskType() == null)
            return null;

        if (task.getDictationData() == null) {
            return createRandomTask(task.getLanguage());
        }

        switch (task.getDictationData().getDictationType()) {
            case NORMAL: {
                result = new NormalDictation(task.getDictationData());
                break;
            }
            case REPEATING: {
                result = new RepeatDication(task.getDictationData());
                if (result.getWordsAmount() == 0)
                {
                    result = createTask(new AbstractTask(task.getTaskType(), new AbstractDictation(Dictation.DictationType.NORMAL, task.getLanguage()), task.getLanguage()));
                }
                break;
            }
            case LEARNING: {
                result = new LearnDictation(task.getDictationData());
                if (result.getWordsAmount() == 0)
                {
                    result = createTask(new AbstractTask(task.getTaskType(), new AbstractDictation(Dictation.DictationType.NORMAL, task.getLanguage()), task.getLanguage()));
                }
                break;
            }
            default: {
                result = createRandomTask(task.getLanguage());
                break;
            }
        }
        return result;
    }

    @Override
    public ITask createRandomTask(Language language) {
        Random random = new Random();
        AbstractDictation abstractDictation = new AbstractDictation(
                Dictation.DictationType.values()[random.nextInt(Dictation.DictationType.values().length)],
                language
        );

        AbstractTask task = new AbstractTask(TaskType.DICTATION, abstractDictation, language);
        return createTask(task);
    }
}
