package tutor.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by user on 10.02.2015.
 */
public class DbManager {
    public Connection getConnection(){
        try{
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:~/langTutor", "langtutor", "hearthstone");
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
