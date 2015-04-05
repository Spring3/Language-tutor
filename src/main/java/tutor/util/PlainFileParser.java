package tutor.util;

import tutor.models.Language;

import java.io.*;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class PlainFileParser extends AbstractParser implements FileParser {

    public PlainFileParser(ResourceBundle bundle){
        super.bundle = bundle;
    }

    @Override
    public void parse(File file, ContentType contentType, Language lang) {
        try {
            doParsing(new FileInputStream(file), contentType, lang);
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void parse(InputStream stream, ContentType contentType, Language lang) {
        doParsing(stream, contentType, lang);
    }
}
