package tutor.dao;

import tutor.models.Stats;
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
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_STATS(user_id, task_type, lang_id, rate) VALUES(?,?,?,?);");
            statement.setInt(1, value.getUser().getId());
            statement.setString(2, value.getTaskType().name());
            statement.setInt(3, value.getLanguage().getId());
            statement.setFloat(4, value.getSuccessRate());
            statement.execute();
            connection.close();
            return true;

        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
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

    private Stats readBy(PreparedStatement statement){
        Stats result = null;
        try{
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                result = new Stats();
                result.setUser(UserDAO.getInstance().read(resultSet.getInt(1)));
                result.setTaskType(resultSet.getString(2));
                result.setLanguage(LanguageDAO.getInstance().read(resultSet.getInt(3)));
                result.setSuccessRate(resultSet.getFloat(4));
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
            PreparedStatement statement = connection.prepareStatement("UPDATE USER_STATS SET user_id=? AND task_type=? AND lang_id=? AND rate=? WHERE id=?;");
            statement.setInt(1, value.getUser().getId());
            statement.setString(2, value.getTaskType().name());
            statement.setInt(3, value.getLanguage().getId());
            statement.setFloat(4, value.getSuccessRate());
            statement.setInt(5, value.getId());
            statement.executeUpdate();
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
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
