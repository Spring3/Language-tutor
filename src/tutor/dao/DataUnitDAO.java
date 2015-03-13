package tutor.dao;

import tutor.models.DataSource;
import tutor.models.DataUnit;
import tutor.models.Language;
import tutor.util.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17.02.2015.
 */
public class DataUnitDAO implements IDAO<DataUnit> {

    public DataUnitDAO(){

    }

    @Override
    public boolean create(DataUnit value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO DATA_UNIT(word, word_translation, lang_id, data_src_id) VALUES(?,?,?,?)");
            statement.setString(1, value.getWord());
            statement.setString(2, value.getTranslation());
            statement.setInt(3, value.getLang().getId());
            statement.setInt(4, value.getDataSrc().getId());
            boolean result = statement.execute();
            System.out.println("DataUnit: " + value.getWord() + " for user: " + value.getLang().getOwner().getUserName() + " was created.");

            connection.close();
            return result;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
    // "CREATE TABLE IF NOT EXISTS DATA_UNIT(id integer IDENTITY PRIMARY KEY, word varchar(100), word_translation varchar(100), lang_id integer, data_src_id integer);",

    @Override
    public DataUnit read(int id) {
        DataUnit result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_UNIT WHERE id=?;");
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next() == true) {
                result = new DataUnit();
                result.setId(resultSet.getInt(1));
                result.setWord(resultSet.getString(2));
                result.setTranslation(resultSet.getString(3));
                result.setLang(new LanguageDAO().read(resultSet.getInt(4)));
                result.setDataSrc(new DataSourceDAO().read(resultSet.getInt(5)));
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<DataUnit> readAllByLang(Language lang){
        List<DataUnit> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_UNIT WHERE lang_id=?;");
            statement.setInt(1, lang.getId());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultList = new ArrayList<>();
            while(resultSet.next() == true){
                DataUnit result = new DataUnit();
                result.setId(resultSet.getInt(1));
                result.setWord(resultSet.getString(2));
                result.setTranslation(resultSet.getString(3));
                result.setLang(new LanguageDAO().read(resultSet.getInt(4)));
                result.setDataSrc(new DataSourceDAO().read(resultSet.getInt(5)));
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

    public boolean contains(DataSource src, DataUnit value){
        boolean result = false;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_UNIT WHERE data_src_id IN (SELECT id FROM DATA_SRC WHERE language_id = ?) AND word = ? AND word_translation = ?");
            statement.setInt(1, src.getLanguage().getId());
            statement.setString(2, value.getWord());
            statement.setString(3, value.getTranslation());
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }
    // "CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(20), service varchar(20), language_id integer);",

    @Override
    public boolean update(DataUnit value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE DATA_UNIT SET id=?, word=?, word_translation=?, lang_id=?, data_src_id=?;");
            statement.setInt(1, value.getId());
            statement.setString(2, value.getWord());
            statement.setString(3, value.getTranslation());
            statement.setInt(4, value.getLang().getId());
            statement.setInt(5, value.getDataSrc().getId());
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
    public boolean delete(DataUnit value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM DATA_UNIT WHERE id=?;");
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
