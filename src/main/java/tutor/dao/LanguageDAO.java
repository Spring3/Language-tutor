package tutor.dao;
import tutor.controllers.AuthController;
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

    private static LanguageDAO instance;

    private LanguageDAO(){

    }

    public static LanguageDAO getInstance(){
        if (instance == null){
            synchronized (LanguageDAO.class){
                if (instance == null){
                    instance = new LanguageDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean create(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LANGUAGES(lang_name) VALUES(?);");
            preparedStatement.setString(1, value.getLang_name());
            preparedStatement.execute();
            System.out.println("New language: " + value.getLang_name() + " was created");
            assignLangToCurrentUser(value);
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean assignLangToCurrentUser(Language lang){
        try{
            if (!contains(AuthController.getActiveUser(), lang)) {
                Connection connection = DbManager.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_LANG(user_id, lang_id) VALUES(?,?)");
                statement.setInt(1, AuthController.getActiveUser().getId());
                statement.setInt(2, readBy(lang.getLang_name()).getId());
                statement.execute();
                connection.close();
                return true;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean unAssignLangFromCurrentUser(Language lang){
        try{
            if (contains(AuthController.getActiveUser(), lang)){
                Connection connection = DbManager.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM USER_LANG WHERE user_id = ? AND lang_id = ?;");
                statement.setInt(1, AuthController.getActiveUser().getId());
                statement.setInt(2, lang.getId());
                return statement.execute();
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

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

    private Language readBy(PreparedStatement sqlStatement){
        Language result = null;
        try {
            sqlStatement.execute();
            ResultSet resultSet = sqlStatement.getResultSet();
            if (resultSet.next())
            {
                result = new Language();
                result.setId(resultSet.getInt(1));
                result.setLang_name(resultSet.getString(2));
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<Language> readAllLanguagesByUser(int userId){
        List<Language> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT langs.id, langs.lang_name FROM LANGUAGES as langs INNER JOIN USER_LANG as ulang ON ulang.user_id =? WHERE langs.id = ulang.lang_id GROUP BY langs.id;");
            statement.setInt(1, userId);
            resultList = readAllLanguages(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    public boolean contains(User user, Language language){
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT lang.id, lang.lang_name FROM LANGUAGES as lang INNER JOIN USER_LANG as ulang ON ulang.user_id=? WHERE ulang.lang_id = lang.id AND lang.lang_name = ?;");
            statement.setInt(1, user.getId());
            statement.setString(2, language.getLang_name());
            statement.execute();
            boolean result  = statement.getResultSet().next();
            connection.close();
            return result;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public List<Language> readAllLanguages(){
        List<Language> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LANGUAGES");
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
                resultList.add(tempLanguage);
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    @Deprecated
    @Override
    public boolean update(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE LANGUAGES SET lang_name=? WHERE id=?;");
            statement.setString(1, value.getLang_name());
            statement.setInt(2, value.getId());
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USER_LANG WHERE user_id = ? AND lang_id = ?;");
            preparedStatement.setInt(1, AuthController.getActiveUser().getId());
            preparedStatement.setInt(2, value.getId());
            preparedStatement.executeUpdate();
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
