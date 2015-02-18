package tutor.dao;

import tutor.models.DataSource;
import tutor.models.Language;
import tutor.util.DbManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17.02.2015.
 */
public class DataSourceDAO implements IDAO<DataSource> {

    @Override
    public boolean create(DataSource value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            String sqlQuery = "INSERT INTO DATA_SRC(link, type, service, language_id) VALUES('" + value.getLink() + "', '"  + value.getType() + "', '" + value.getService() + "', " + value.getLanguage().getId() + ");";
            boolean result = connection.createStatement().execute(sqlQuery);
            System.out.println("DATA SOURCE: " + value.getLink() + " was created");
            connection.close();
            return result;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public DataSource read(int id) {
        DataSource result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM DATA_SRC WHERE id=" + id + ";");
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next() == true){
                result = new DataSource();
                result.setId(resultSet.getInt(1));
                result.setLink(resultSet.getString(2));
                result.setType(resultSet.getString(3));
                result.setService(resultSet.getString(4));
                result.setLanguage(new LanguageDAO().read(resultSet.getInt(5)));
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public List<DataSource> readAllByLanguage(Language lang){
        List<DataSource> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM DATA_SRC WHERE language_id=" + lang.getId() + ";";
            statement.execute(sqlQuery);
            ResultSet resultSet = statement.getResultSet();
            resultList = new ArrayList<>();
            while(resultSet.next() == true){
                DataSource result = new DataSource();
                result.setId(resultSet.getInt(1));
                result.setLink(resultSet.getString(2));
                result.setType(resultSet.getString(3));
                result.setService(resultSet.getString(4));
                result.setLanguage(new LanguageDAO().read(resultSet.getInt(5)));
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
    public boolean update(DataSource value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE DATA_SRC SET id=" + value.getId() + ", link='" + value.getLink() + "', type='" + value.getType() + "', service='" + value.getService() + "', language_id=" + value.getLanguage().getId() + ";");
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(DataSource value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM DATA_SRC WHERE id= " + value.getId() + ";");
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //"CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(50), service varchar(50), language_id integer);",
}
