package tutor.dao;

import tutor.util.DbManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tutor.models.User;

/**
 * Created by user on 12.02.2015.
 */
public class UserDAO implements IDAO<User> {
    @Override
    public boolean create(User value) {
        try {
            Connection connection =  DbManager.getInstance().getConnection();
            connection.createStatement().execute("INSERT INTO USERS(username, email, password, register_date, success_rate, seed) VALUES('"+
                    value.getUserName() + "','" + value.getEmail() +"','" + value.getPassword() + "" + value.getSeed() + "','" + value.getDateOfRegistery()+ "'," + value.getSuccess_rate() +"," + value.getSeed() + ");");
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
            Statement statement = connection.createStatement();
            resultUser = readBy(statement, "SELECT * FROM USERS WHERE id='" + id + "';");
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
            Statement statement = connection.createStatement();
            resultUser = readBy(statement, "SELECT * FROM USERS WHERE username='" + userName + "';");
            connection.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return resultUser;
    }

    private User readBy(Statement sqlStatement, String query){
        User resultUser = null;
        try {
            sqlStatement.execute(query);
            ResultSet result = sqlStatement.getResultSet();
            if (result.next() == true)
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
                resultUser.setPassword(Integer.valueOf(password.substring(0, password.lastIndexOf(seed+""))));
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
            connection.createStatement().executeUpdate("" +
                    "UPDATE USERS SET id=" + value.getId() + ", username='" + value.getUserName() + "', email='" + value.getEmail() + "', password='" + value.getPassword() + "" + value.getSeed() + "', register_date='" + value.getDateOfRegistery() + "', success_rate=" + value.getSuccess_rate() + ", seed=" + value.getSeed() + ";");
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
            connection.createStatement().executeUpdate("DELETE FROM USERS WHERE id='" + value.getId() + "';");
            connection.close();
            return true;
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
