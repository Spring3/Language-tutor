package tutor.models;

import tutor.dao.StatsDAO;
import tutor.tasks.TaskManager;
import tutor.tasks.TaskType;

/**
 * Created by Spring on 9/1/2015.
 */
public class Stats {

    public Stats(){

    }

    public Stats(User user, TaskType task_type, Language language, float successRate){
        setUser(user);
        setTaskType(task_type);
        setLanguage(language);
        setSuccessRate(successRate);
    }

    private int id;
    private User user;
    private Language language;
    private float successRate;
    private TaskType taskType;
    private int tries;

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = TaskType.valueOf(taskType);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public int getTries(){
        return tries;
    }

    public void setTries(int tries)
    {
        this.tries = tries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stats stats = (Stats) o;

        if (Float.compare(stats.successRate, successRate) != 0) return false;
        if (tries != stats.tries) return false;
        if (user != null ? !user.equals(stats.user) : stats.user != null) return false;
        if (language != null ? !language.equals(stats.language) : stats.language != null) return false;
        return taskType == stats.taskType;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (successRate != +0.0f ? Float.floatToIntBits(successRate) : 0);
        result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
        result = 31 * result + tries;
        return result;
    }
}
