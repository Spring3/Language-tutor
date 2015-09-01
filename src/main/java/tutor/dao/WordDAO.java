package tutor.dao;

import tutor.controllers.AuthController;
import tutor.models.Word;
import tutor.models.Language;
import tutor.util.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17.02.2015.
 */
public class WordDAO implements IDAO<Word> {

    private static WordDAO instance;

    private WordDAO(){

    }

    public static WordDAO getInstance(){
        if (instance == null){
            synchronized (WordDAO.class){
                instance = new WordDAO();
            }
        }
        return instance;
    }

    @Override
    public boolean create(Word value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            if (!contains(value)) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO WORD(article, word, word_translation, lang_id, translation_id, whenAdded, wrongAnswers, correctAnswers) VALUES(?,?,?,?,?,?,?,?);");
                statement.setString(1, value.getArticle().get());
                statement.setString(2, value.getWord().get());
                statement.setString(3, value.getTranslation().get());
                statement.setInt(4, value.getWordLang().getId());
                statement.setInt(5, value.getTranslationLang().getId());
                statement.setDate(6, value.getAddedDate());
                statement.setInt(7, value.getWrongAnswerCount());
                statement.setInt(8, value.getCorrectAnswerCount());
                statement.execute();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER_WORD(user_id, word_id) VALUES(?,?);");
                preparedStatement.setInt(1, AuthController.getActiveUser().getId());
                preparedStatement.setInt(2, getLastAdded().getId());
                preparedStatement.execute();
                System.out.println("DataUnit: " + value.getWord() + " was created.");

                connection.close();
                return true;
            }
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    private Word getLastAdded(){
        Word result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM WORD ORDER BY id DESC LIMIT 1;");
            result = readBy(statement);
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public Word readByContentForActiveUser(String word){
        Word result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT w.id, article, word, word_translation, lang_id, translation_id, whenAdded, wrongAnswers, correctAnswers FROM WORD as w INNER JOIN USER_WORD as uw ON uw.user_id =? WHERE w.word = ? GROUP BY w.id;");
            statement.setInt(1, AuthController.getActiveUser().getId());
            statement.setString(2, word);
            result = readBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public Word read(int wordId) {
        Word result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM WORD WHERE id=? ;");
            statement.setInt(1, wordId);
            result = readBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    private Word readBy(PreparedStatement statement){
        Word result = null;
        try {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                result = new Word();
                result.setId(resultSet.getInt(1));
                result.setArticle(resultSet.getString(2));
                result.setWord(resultSet.getString(3));
                result.setTranslation(resultSet.getString(4));
                result.setWordLang(LanguageDAO.getInstance().read(resultSet.getInt(5)));
                result.setTranslationLang(LanguageDAO.getInstance().read(resultSet.getInt(6)));
                result.setAddedDate(resultSet.getDate(7));
                result.setWrongAnswerCount(resultSet.getInt(8));
                result.setCorrectAnswerCount(resultSet.getInt(9));
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<Word> readAllByLang(Language lang){
        List<Word> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM WORD WHERE lang_id=?;");
            statement.setInt(1, lang.getId());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultList = new ArrayList<>();
            while(resultSet.next()){
                Word result = new Word();
                result.setId(resultSet.getInt(1));
                result.setArticle(resultSet.getString(2));
                result.setWord(resultSet.getString(3));
                result.setTranslation(resultSet.getString(4));
                result.setWordLang(LanguageDAO.getInstance().read(resultSet.getInt(5)));
                result.setTranslationLang(LanguageDAO.getInstance().read(resultSet.getInt(6)));
                result.setAddedDate(resultSet.getDate(7));
                result.setWrongAnswerCount(resultSet.getInt(8));
                result.setCorrectAnswerCount(resultSet.getInt(9));
                resultList.add(result);
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    public List<Word> readAllByLangForActiveUser(Language lang){
        List<Word> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT w.id, w.article, w.word, w.word_translation, w.lang_id, w.translation_id, w.whenAdded, w.wrongAnswers, w.correctAnswers FROM WORD as w INNER JOIN USER_WORD ON user_id=? WHERE w.lang_id=? GROUP BY w.id ORDER BY w.id DESC; ");
            statement.setInt(1, AuthController.getActiveUser().getId());
            statement.setInt(2, lang.getId());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultList = new ArrayList<>();
            while(resultSet.next()){
                Word result = new Word();
                result.setId(resultSet.getInt(1));
                result.setArticle(resultSet.getString(2));
                result.setWord(resultSet.getString(3));
                result.setTranslation(resultSet.getString(4));
                result.setWordLang(LanguageDAO.getInstance().read(resultSet.getInt(5)));
                result.setTranslationLang(LanguageDAO.getInstance().read(resultSet.getInt(6)));
                result.setAddedDate(resultSet.getDate(7));
                result.setWrongAnswerCount(resultSet.getInt(8));
                result.setCorrectAnswerCount(resultSet.getInt(9));
                resultList.add(result);
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    public boolean contains(Word value){
        boolean result = false;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT w.id, w.article, w.word, w.word_translation, w.lang_id, w.translation_id, w.whenAdded, w.wrongAnswers, w.correctAnswers FROM WORD as w INNER JOIN USER_WORD as uw ON uw.word_id=w.id WHERE uw.user_id=? AND w.article=? AND w.word=? AND w.word_translation=? AND w.lang_id=? AND w.translation_id=? AND w.whenAdded=? AND w.wrongAnswers=? AND w.correctAnswers=? GROUP BY w.id;");
            statement.setInt(1, AuthController.getActiveUser().getId());
            statement.setString(2, value.getArticle().get());
            statement.setString(3, value.getWord().get());
            statement.setString(4, value.getTranslation().get());
            statement.setInt(5, value.getWordLang().getId());
            statement.setInt(6, value.getTranslationLang().getId());
            statement.setDate(7, value.getAddedDate());
            statement.setInt(8, value.getWrongAnswerCount());
            statement.setInt(9, value.getCorrectAnswerCount());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            result = resultSet.next();
            resultSet.close();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }
    // "CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(20), service varchar(20), language_id integer);",

    @Override
    public boolean update(Word value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE WORD SET article=?, word=?, word_translation=?, lang_id=?, translation_id=?, whenAdded=?, wrongAnswers=?, correctAnswers=? WHERE id=?;");
            statement.setString(1, value.getArticle().get());
            statement.setString(2, value.getWord().get());
            statement.setString(3, value.getTranslation().get());
            statement.setInt(4, value.getWordLang().getId());
            statement.setInt(5, value.getTranslationLang().getId());
            statement.setDate(6, value.getAddedDate());
            statement.setInt(7, value.getWrongAnswerCount());
            statement.setInt(8, value.getCorrectAnswerCount());
            statement.setInt(9, value.getId());
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
    public boolean delete(Word value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USER_WORD WHERE user_id=? AND word_id=?;");
            preparedStatement.setInt(1, AuthController.getActiveUser().getId());
            preparedStatement.setInt(2, value.getId());
            preparedStatement.executeUpdate();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM WORD WHERE id=?;");
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
