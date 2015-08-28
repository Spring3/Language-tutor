package tutor.util;

import tutor.models.Language;

import java.io.File;

/**
 * Created by Spring on 8/26/2015.
 */
public class BasicParser implements FileParser {
    public BasicParser(){

    }

    public void parse(File file, Language lang, Language translationLang){
        FileParser.super.parse(file, lang, translationLang);
    }
}
