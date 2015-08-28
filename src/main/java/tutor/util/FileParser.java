package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import javax.sql.rowset.serial.SerialRef;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 02.03.2015.
 */
public interface FileParser {
    default void parse(File file, Language lang, Language translationLang){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] parsedDocLine;
            while ((parsedDocLine = reader.readLine().split(",")) != null) {
                try {
                    Word word = new Word(parsedDocLine[0], parsedDocLine[1], parsedDocLine[2], lang, translationLang);
                    WordDAO.getInstance().create(word);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    Word word = new Word(parsedDocLine[0], parsedDocLine[1], "", lang, translationLang);
                    WordDAO.getInstance().create(word);
                    return;
                }
            }
        }
        catch (Exception ex){
            System.err.println("Failed to parse .csv file.");
        }
    }
}
