package tutor.dao;

import tutor.models.Language;
import tutor.models.Stats;
import tutor.models.User;
import tutor.tasks.TaskType;
import tutor.util.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Spring on 9/1/2015.
 */
public class StatsDAO implements IDAO<Stats>{

    private StatsDAO(){

    }

    private static StatsDAO instance;

    public static StatsDAO getInstance(){
        if (instance == null)
        {
            synchronized (StatsDAO.class){
                if (instance == null){
                    instance = new StatsDAO();
                }
            }
        }
        return instance;
    }
    //"CREATE TABLE IF NOT EXISTS USER_STATS(id integer, user_id integer, task_type varchar(20), lang_id integer, rate double, FOREIGN KEY(user_id) REFERENCES USERS(id), FOREIGN KEY(lang_id) REFERENCES LANGUAGES(id));",
    @Override
    public boolean create(Stats value) {
        if (value == null)
            return false;

        if (!exists(value)) {
            try {
                Connection connection = DbManager.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_STATS(user_id, task_type, lang_id, rate, tries) VALUES(?,?,?,?,?);");
                statement.setInt(1, value.getUser().getId());
                statement.setString(2, value.getTaskType().name());
                statement.setInt(3, value.getLanguage().getId());
                statement.setFloat(4, value.getSuccessRate());
                statement.setInt(5, 1); //first try
                statement.execute();
                connection.close();
                return true;

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
        else
        {
            return refresh(value);
        }
    }

    @Override
    public Stats read(int id) {
        Stats result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_STATS WHERE id=?");
            statement.setInt(1, id);
            result = readBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public Stats read(User user, Language language, TaskType taskType){
        Stats result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_STATS WHERE user_id=? AND lang_id=? AND task_type=?;");
            statement.setInt(1, user.getId());
            statement.setInt(2, language.getId());
            statement.setString(3, taskType.name());
            result = readBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        return result;
    }

    private Stats readBy(PreparedStatement statement){
        Stats result = null;
        try{
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                result = new Stats();
                result.setId(resultSet.getInt(1));
                result.setUser(UserDAO.getInstance().read(resultSet.getInt(2)));
                result.setTaskType(resultSet.getString(3));
                result.setLanguage(LanguageDAO.getInstance().read(resultSet.getInt(4)));
                result.setSuccessRate(resultSet.getFloat(5));
                result.setTries(resultSet.getInt(6));
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Stats value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE USER_STATS SET rate=?, tries=? WHERE id=?;");
            statement.setFloat(1, value.getSuccessRate());
            statement.setInt(2, value.getTries());
            statement.setInt(3, value.getId());
            int result = statement.executeUpdate();
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    private boolean refresh(Stats value){
        Stats statsFromDB;
        if (value.getId() != 0){
            statsFromDB = read(value.getId());
        }
        else {
            statsFromDB = read(value.getUser(), value.getLanguage(), value.getTaskType());
        }
        float newSuccessRate = (statsFromDB.getSuccessRate() * statsFromDB.getTries() + value.getSuccessRate()) / (statsFromDB.getTries() + 1);
        statsFromDB.setTries(statsFromDB.getTries() + 1);
        statsFromDB.setSuccessRate(newSuccessRate);
        return update(statsFromDB);
    }

    private boolean exists(Stats value){
        boolean result = false;

        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_STATS WHERE user_id=? AND task_type=? AND lang_id=?;");
            statement.setInt(1, value.getUser().getId());
            statement.setString(2, value.getTaskType().name());
            statement.setInt(3, value.getLanguage().getId());
            statement.execute();
            result = statement.getResultSet().first();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Stats value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE USER_STATS WHERE id=?;");
            statement.setInt(1, value.getId());
            statement.executeUpdate();
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
