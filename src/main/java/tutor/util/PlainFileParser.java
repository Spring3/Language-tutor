package tutor.util;

import tutor.models.Language;

import java.io.*;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class PlainFileParser implements FileParser {

    public PlainFileParser(ResourceBundle bundle){
        this.bundle = bundle;
    }

    private ResourceBundle bundle;

    @Override
    public void parse(File file, Language lang) {
        try {
            doParsing(new FileInputStream(file), lang);
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

}
