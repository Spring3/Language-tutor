package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Word;
import tutor.models.Language;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public abstract class AbstractParser implements FileParser{
    public ResourceBundle bundle;

    @Override
    public abstract void parse(File file, ContentType contentType, Language lang);

    @Override
    public abstract void parse(InputStream stream, ContentType contentType, Language lang);

    void doParsing(InputStream inputStream, ContentType contentType, Language dataLanguage){
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));

            List<Word> words = new ArrayList<>();
            WordDAO dao = WordDAO.getInstance();
            if (contentType == ContentType.WORDS_ONLY) {
                fileReader.lines().forEach(line -> words.add(new Word(line.trim(), "", dataLanguage)));
                words.parallelStream().filter(dataUnit -> !dao.contains(dataUnit)).forEach(dao::create);

            } else if (contentType == ContentType.TRANSLATION_ONLY) {
                fileReader.lines().forEach(line -> words.add(new Word("", line.trim(), dataLanguage)));
                words.parallelStream().filter(dataUnit -> !dao.contains(dataUnit)).forEach(dao::create);

            } else if (contentType == ContentType.WORDS_TRANSLATION) {
                fileReader.lines().forEach((s) -> words.add(new Word(s.substring(0, s.indexOf('=')).trim(), s.substring(s.indexOf('=') + 1, s.length()).trim(), dataLanguage)));
                words.parallelStream().filter(dataUnit -> !dao.contains(dataUnit)).forEach(dao::create);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
