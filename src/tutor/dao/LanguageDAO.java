package tutor.dao;
import tutor.models.Language;
import tutor.models.User;
import tutor.util.DbManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13.02.2015.
 */
public class LanguageDAO implements IDAO<Language> {
    @Override
    public boolean create(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            String sqlQuery = "INSERT INTO LANGUAGES(lang_name, user_id) VALUES('" + value.getLang_name() + "'," + value.getOwner().getId()+");";
            boolean result =  connection.createStatement().execute(sqlQuery);
            System.out.println("New language: " + value.getLang_name() + " for user: " + value.getOwner().getUserName() + " was created");
            connection.close();
            return result;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //"CREATE TABLE IF NOT EXISTS LANGUAGES(id integer IDENTITY PRIMARY KEY, lang_name varchar(40), user_id integer);"

    @Override
    public Language read(int id) {
        Language result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            result = readBy(statement, "SELECT * FROM LANGUAGES WHERE id=" + id + ";");
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public Language readBy(int owner_id){
        Language result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            result = readBy(statement, "SELECT * FROM LANGUAGES WHERE user_id=" + owner_id + ";");
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public Language readBy(String lang_name){
        Language result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            result = readBy(statement, "SELECT * FROM LANGUAGES WHERE lang_name='" + lang_name + "';");
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public Language readBy(String lang_name, int owner_id ){
        Language result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            result = readBy(statement, "SELECT * FROM LANGUAGES WHERE lang_name='" + lang_name + "' AND user_id=" + owner_id + ";");
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    private Language readBy(Statement sqlStatement, String query){
        Language result = null;
        try {
            sqlStatement.execute(query);
            ResultSet resultSet = sqlStatement.getResultSet();
            if (resultSet.next() == true)
            {
                result = new Language();
                result.setId(resultSet.getInt(1));
                result.setLang_name(resultSet.getString(2));
                result.setOwner(new UserDAO().read(resultSet.getInt(3)));
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<Language> readAllLanguages(String lang_name){
        List<Language> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM LANGUAGES WHERE lang_name='" + lang_name + "';";
            resultList = readAllLanguages(statement, sqlQuery);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    public List<Language> readAllLanguages(int ownerId){
        List<Language> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM LANGUAGES WHERE user_id=" + ownerId + ";";
            resultList = readAllLanguages(statement, sqlQuery);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    private List<Language> readAllLanguages(Statement statement, String sqlQuery ){
        List<Language> resultList = null;
        try {
            statement.execute(sqlQuery);
            resultList = new ArrayList<>();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next() == true) {
                Language tempLanguage = new Language();
                tempLanguage.setId(resultSet.getInt(1));
                tempLanguage.setLang_name(resultSet.getString(2));
                tempLanguage.setOwner(new UserDAO().read(resultSet.getInt(3)));
                resultList.add(tempLanguage);
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    @Override
    public boolean update(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE LANGUAGES SET id=" + value.getId() + ", lang_name='" + value.getLang_name() + "', user_id=" + value.getOwner().getId() + ";");
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM LANGUAGES WHERE id=" + value.getId());
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
