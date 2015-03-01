package tutor.dao;

import tutor.models.DataSource;
import tutor.models.Language;
import tutor.models.User;
import tutor.util.DbManager;

import java.sql.*;
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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO DATA_SRC(link, type, service, language_id) VALUES(?,?,?,?);");
            statement.setString(1, value.getLink());
            statement.setString(2, value.getType());
            statement.setString(3, value.getService());
            statement.setInt(4, value.getLanguage().getId());
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
    public DataSource read(int id) {
        DataSource result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_SRC WHERE id=?;");
            statement.setInt(1, id);
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_SRC WHERE language_id=?;");
            statement.setInt(1, lang.getId());
            resultList = readAllBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    public List<DataSource> readAllByOwner(User user){
        List<DataSource> resultList = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA_SRC WHERE language_id IN (SELECT id FROM LANGUAGES WHERE user_id=?);");
            statement.setInt(1, user.getId());
            resultList = readAllBy(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultList;
    }

    private List<DataSource> readAllBy(PreparedStatement statement){
        List<DataSource> resultList = null;
        try {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultList = new ArrayList<>();
            while (resultSet.next() == true) {
                DataSource result = new DataSource();
                result.setId(resultSet.getInt(1));
                result.setLink(resultSet.getString(2));
                result.setType(resultSet.getString(3));
                result.setService(resultSet.getString(4));
                result.setLanguage(new LanguageDAO().read(resultSet.getInt(5)));
                resultList.add(result);
            }
            resultSet.close();
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
            PreparedStatement statement = connection.prepareStatement("UPDATE DATA_SRC SET id=?, link=?, type=?, service=?, language_id=?;");
            statement.setInt(1, value.getId());
            statement.setString(2, value.getLink());
            statement.setString(3, value.getType());
            statement.setString(4, value.getService());
            statement.setInt(5, value.getLanguage().getId());
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
    public boolean delete(DataSource value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM DATA_SRC WHERE id=?;");
            statement.setInt(1, value.getId());
            statement.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //"CREATE TABLE IF NOT EXISTS DATA_SRC(id integer IDENTITY PRIMARY KEY, link varchar(100), type varchar(50), service varchar(50), language_id integer);",
}
