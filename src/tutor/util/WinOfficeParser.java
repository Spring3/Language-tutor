package tutor.util;

import tutor.models.Language;

import java.io.File;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class WinOfficeParser extends AbstractParser implements FileParser {

    public WinOfficeParser(ResourceBundle bundle){
        super.bundle = bundle;
    }

    @Override
    public void parse(File file, ContentType contentType, Language lang) {

    }

    @Override
    public void parse(InputStream stream, ContentType contentType, Language lang) {

    }
}
