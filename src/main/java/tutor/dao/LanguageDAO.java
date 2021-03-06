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

    /**
     * Saves the passed object as a new entity of the database.
     * @param value an object to be saved.
     * @return true if the object was saved successfully, false if otherwise.
     */
    @Override
    public boolean create(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LANGUAGES(lang_name, short_name) VALUES(?,?);");
            preparedStatement.setString(1, value.getLangName());
            preparedStatement.setString(2, value.getLangName());
            if (preparedStatement.execute())
                System.out.println("New language: " + value.getLangName() + " was created");
            assignLangToCurrentUser(value, null);
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Assigns a passed language to the currently logged in user. Marks that the user wants to learn this language.
     * @param lang language to learn.
     * @return true, if language has been assigned successfully.
     */
    public boolean assignLangToCurrentUser(Language lang, User user){
        try{
            if (!contains(user, lang)) {
                Connection connection = DbManager.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_LANG(user_id, lang_id) VALUES(?,?)");
                if (user == null)
                    statement.setInt(1, AuthController.getActiveUser().getId());
                else
                    statement.setInt(1, user.getId());
                statement.setInt(2, read(lang.getLangName()).getId());
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

    /**
     * Marks that a user doesn't want to learn a given language anymore.
     * @param lang language to unassign.
     * @return true, if a language has been successfully assigned.
     */
    public boolean unAssignLangFromCurrentUser(Language lang, User user){
        try{
            if (contains(user, lang)){
                Connection connection = DbManager.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM USER_LANG WHERE user_id = ? AND lang_id = ?;");
                if (user == null)
                    statement.setInt(1, AuthController.getActiveUser().getId());
                else
                    statement.setInt(1, user.getId());
                statement.setInt(2, lang.getId());
                boolean result =  statement.execute();
                connection.close();
                return result;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Searches for the language that matches the given id.
     * @param id id of the language to find.
     * @return language if found.
     */
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

    /**
     * Searches for the language that matches the given name.
     * @param lang_name the name of the language to look for.
     * @return language if found.
     */
    public Language read(String lang_name){
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

    private Language readBy(PreparedStatement sqlStatement) throws SQLException {
        Language result = null;
        sqlStatement.execute();
        ResultSet resultSet = sqlStatement.getResultSet();
        if (resultSet.next()) {
            result = new Language();
            result.setId(resultSet.getInt(1));
            result.setLangName(resultSet.getString(2));
            result.setShortName(resultSet.getString(3));
        }
        resultSet.close();
        return result;
    }

    /**
     * Read all languages, assigned to the user with given id.
     * @param userId id of the user.
     * @return a list of assigned languages.
     */
    public List<Language> readAllLanguagesByUser(int userId){
        List<Language> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT langs.id, langs.lang_name, langs.short_name FROM LANGUAGES as langs INNER JOIN USER_LANG as ulang ON ulang.user_id =? WHERE langs.id = ulang.lang_id GROUP BY langs.id;");
            statement.setInt(1, userId);
            resultList = readAllLanguages(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    /**
     * Checks if such language was already assigned to a given user.
     * @param user a user to be checked.
     * @param language a language to look for.
     * @return true if one has already been assigned.
     */
    public boolean contains(User user, Language language){
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT lang.id, lang.lang_name, lang.short_name FROM LANGUAGES as lang INNER JOIN USER_LANG as ulang ON ulang.user_id=? WHERE ulang.lang_id = lang.id AND lang.lang_name = ? AND lang.short_name=?;");
            statement.setInt(1, user.getId());
            statement.setString(2, language.getLangName());
            statement.setString(3, language.getShortName());
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

    /**
     * Finds all the languages accessible.
     * @return the list of languages.
     */
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

    private List<Language> readAllLanguages(PreparedStatement statement) throws SQLException {
        statement.execute();
        List<Language> resultList = new ArrayList<>();
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            Language tempLanguage = new Language();
            tempLanguage.setId(resultSet.getInt(1));
            tempLanguage.setLangName(resultSet.getString(2));
            tempLanguage.setShortName(resultSet.getString(3));
            resultList.add(tempLanguage);
        }
        resultSet.close();
        return resultList;
    }

    /**
     * Updates the given language.
     * @param value language to be updated.
     * @return true if updating process was successful.
     * @deprecated since it was implemented only to match interface policy.
     */
    @Deprecated
    @Override
    public boolean update(Language value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE LANGUAGES SET lang_name=?, short_name=? WHERE id=?;");
            statement.setString(1, value.getLangName());
            statement.setString(2, value.getShortName());
            statement.setInt(3, value.getId());
            statement.executeUpdate();
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes from the database the entity of a given object if such exists.
     * @param value the language to delete.
     * @return true if the deletion process was successful.
     */
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
