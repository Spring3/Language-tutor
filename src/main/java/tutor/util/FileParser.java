package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    void parse(File file, Language lang);

    default void doParsing(InputStream inputStream, Language dataLanguage){
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));

            List<Word> words = new ArrayList<>();
            WordDAO dao = WordDAO.getInstance();

            fileReader.lines().forEach((s) -> words.add(new Word(s.substring(0, s.indexOf('=')).trim(), s.substring(s.indexOf('=') + 1, s.length()).trim(), dataLanguage)));
            words.parallelStream().forEach(dao::create);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
