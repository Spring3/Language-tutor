package tutor.util;

import tutor.models.Language;

import java.io.File;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class WinOfficeParser implements FileParser {

    public WinOfficeParser(ResourceBundle bundle){
        this.bundle = bundle;
    }

    private ResourceBundle bundle;

    @Override
    public void parse(File file, Language lang) {

    }
}
