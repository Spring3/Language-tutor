package tutor.dao;

import tutor.util.DbManager;

import java.sql.*;

import tutor.models.User;

/**
 * Created by user on 12.02.2015.
 */
public class UserDAO implements IDAO<User> {

    private static UserDAO instance;

    private UserDAO(){

    }

    public static UserDAO getInstance(){
        if (instance == null){
            synchronized (UserDAO.class){
                instance = new UserDAO();
            }
        }
        return instance;
    }

    @Override
    public boolean create(User value) {
        try {
            Connection connection =  DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO USERS(username, email, password, register_date, success_rate, seed, lang_id) VALUES(?,?,?,?,?,?,?);");
            statement.setString(1, value.getUserName());
            statement.setString(2, value.getEmail());
            statement.setString(3, value.getPassword() + "" + value.getSeed());
            statement.setTimestamp(4, value.getDateOfRegistery());
            statement.setFloat(5, value.getSuccess_rate());
            statement.setInt(6, value.getSeed());
            statement.setInt(7, value.getNativeLanguage().getId());
            statement.execute();
            System.out.println("User: " + value.getUserName() + " was successfully added");
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public User read(int id) {
        User resultUser = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE id=?;");
            statement.setInt(1, id);
            resultUser = readBy(statement);
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return resultUser;
    }

    public User readByUserName(String userName){
        User resultUser = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE username=?;");
            statement.setString(1, userName);
            resultUser = readBy(statement);
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return resultUser;
    }

    private User readBy(PreparedStatement sqlStatement){
        User resultUser = null;
        try {
            sqlStatement.execute();
            ResultSet result = sqlStatement.getResultSet();
            if (result.next())
            {
                resultUser = new User();
                resultUser.setId(result.getInt(1));
                resultUser.setUserName(result.getString(2));
                resultUser.setEmail(result.getString(3));
                String password = result.getString(4);
                resultUser.setDateOfRegistery(result.getTimestamp(5));
                resultUser.setSuccess_rate(result.getFloat(6));
                int seed = result.getInt(7);
                resultUser.setSeed(seed);
                resultUser.setNativeLanguage(LanguageDAO.getInstance().read(result.getInt(8)));
                String passSubString = password.substring(0, password.lastIndexOf(seed+""));
                resultUser.setPassword(Integer.valueOf(passSubString));
            }
            result.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return resultUser;
    }

    @Override
    public boolean update(User value) {
        try {
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE USERS SET username=?, email=?, password=?, register_date=?, success_rate=?, seed=?, lang_id=? WHERE id=?;");
            statement.setString(1, value.getUserName());
            statement.setString(2, value.getEmail());
            statement.setString(3, value.getPassword() + "" + value.getSeed());
            statement.setTimestamp(4, value.getDateOfRegistery());
            statement.setFloat(5, value.getSuccess_rate());
            statement.setInt(6, value.getSeed());
            statement.setInt(7, value.getNativeLanguage().getId());
            statement.setInt(8, value.getId());
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
    public boolean delete(User value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USERS WHERE id=?;");
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
