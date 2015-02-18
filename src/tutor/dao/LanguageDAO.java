package tutor.dao;
import tutor.models.Language;
import tutor.models.User;
import tutor.util.DbManager;

import java.sql.*;
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LANGUAGES(lang_name, user_id) VALUES(?,?);");
            preparedStatement.setString(1, value.getLang_name());
            preparedStatement.setInt(2, value.getOwner().getId());
            boolean result = preparedStatement.execute();
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE id=?;");
            statement.setInt(1, id);
            result = readBy(statement);
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE user_id=?;");
            statement.setInt(1, owner_id);
            result = readBy(statement);
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE lang_name=?;");
            statement.setString(1, lang_name);
            result = readBy(statement);
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE lang_name=? AND user_id=?;");
            statement.setString(1, lang_name);
            statement.setInt(2, owner_id);
            result = readBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    private Language readBy(PreparedStatement sqlStatement){
        Language result = null;
        try {
            sqlStatement.execute();
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE lang_name=?;");
            statement.setString(1, lang_name);
            resultList = readAllLanguages(statement);
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES WHERE user_id=?;");
            statement.setInt(1, ownerId);
            resultList = readAllLanguages(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    private List<Language> readAllLanguages(PreparedStatement statement){
        List<Language> resultList = null;
        try {
            statement.execute();
            resultList = new ArrayList<>();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next() == true) {
                Language tempLanguage = new Language();
                tempLanguage.setId(resultSet.getInt(1));
                tempLanguage.setLang_name(resultSet.getString(2));
                User owner = new UserDAO().read(resultSet.getInt(3));
                tempLanguage.setOwner(owner);
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
            PreparedStatement statement = connection.prepareStatement("UPDATE LANGUAGES SET id=?, lang_name=?, user_id=?;");
            statement.setInt(1, value.getId());
            statement.setString(2, value.getLang_name());
            statement.setInt(3, value.getOwner().getId());
            statement.executeUpdate();
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
            PreparedStatement statement = connection.prepareStatement("DELETE FROM LANGUAGES WHERE id=?;");
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
