package tutor.dao;

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
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM DATA_UNIT WHERE id=" + id + ";");
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
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM DATA_UNIT WHERE lang_id=" + lang.getId() + ";";
            statement.execute(sqlQuery);
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

    @Override
    public boolean update(DataUnit value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE DATA_UNIT SET id=" + value.getId() + ", word='" + value.getWord() + "', word_translation='" + value.getTranslation() + "', lang_id=" + value.getLang().getId() + ", data_src_id=" + value.getDataSrc().getId() + ";");
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
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM DATA_UNIT WHERE id=" + value.getId() + ";");
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
