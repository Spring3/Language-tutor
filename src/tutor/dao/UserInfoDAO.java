package tutor.dao;

import tutor.models.User;
import tutor.models.UserInfo;
import tutor.util.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 03.03.2015.
 */
public class UserInfoDAO implements IDAO<UserInfo> {
    @Override
    public boolean create(UserInfo value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO USER_INFO(google_code, user_id) VALUES (?,?);");
            statement.setString(1,value.getGoogle_code());
            statement.setInt(2, value.getOwner().getId());
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
    public UserInfo read(int id) {
        UserInfo result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_INFO WHERE id=?;");
            statement.setInt(1, id);
            statement.execute();
            result = read(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public UserInfo readByUser(User user){
        UserInfo result = null;
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER_INFO WHERE user_id=?;");
            statement.setInt(1, user.getId());
            statement.execute();
            result = read(statement);
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    private UserInfo read(PreparedStatement statement){
        UserInfo result = null;
        try {
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next() == true) {
                result = new UserInfo();
                result.setId(resultSet.getInt(1));
                result.setGoogle_code(resultSet.getString(2));
                result.setOwner(new UserDAO().read(resultSet.getInt(3)));
            }
            resultSet.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean update(UserInfo value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE USER_INFO SET id=?, google_code=?, user_id=?;");
            statement.setInt(1, value.getId());
            statement.setString(2, value.getGoogle_code());
            statement.setInt(3, value.getOwner().getId());
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
    public boolean delete(UserInfo value) {
        try{
            Connection connection = DbManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USER_INFO WHERE id=?;");
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
