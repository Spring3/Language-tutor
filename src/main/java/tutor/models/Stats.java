package tutor.models;

import tutor.util.TaskManager;

/**
 * Created by Spring on 9/1/2015.
 */
public class Stats {

    public Stats(){

    }

    public Stats(User user, TaskManager.TaskManagerMode task_type, Language language, float successRate){
        setUser(user);
        setTaskType(task_type);
        setLanguage(language);
        setSuccessRate(successRate);
    }

    private int id;
    private User user;
    private Language language;
    private float successRate;
    private TaskManager.TaskManagerMode taskType;

    public TaskManager.TaskManagerMode getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskManager.TaskManagerMode taskType) {
        this.taskType = taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = TaskManager.TaskManagerMode.valueOf(taskType);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stats stats = (Stats) o;

        if (Float.compare(stats.getSuccessRate(), getSuccessRate()) != 0) return false;
        if (!getUser().equals(stats.getUser())) return false;
        if (!getLanguage().equals(stats.getLanguage())) return false;
        return getTaskType() == stats.getTaskType();

    }

    @Override
    public int hashCode() {
        int result = getUser().hashCode();
        result = 31 * result + getLanguage().hashCode();
        result = 31 * result + (getSuccessRate() != +0.0f ? Float.floatToIntBits(getSuccessRate()) : 0);
        result = 31 * result + getTaskType().hashCode();
        return result;
    }
}
