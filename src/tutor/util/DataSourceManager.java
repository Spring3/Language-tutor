package tutor.util;

import java.io.File;

/**
 * Created by user on 17.02.2015.
 */
public class DataSourceManager {

    private DataSourceManager(){

    }

    private static DataSourceManager instance;

    public DataSourceManager getInstance(){
        if (instance == null){
            synchronized (DataSourceManager.class){
                if (instance == null){
                    instance = new DataSourceManager();
                }
            }
        }
        return instance;
    }


    public void process(File dataFile){

    }
}
