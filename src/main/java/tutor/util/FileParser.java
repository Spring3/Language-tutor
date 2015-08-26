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
    default void parse(File file, Language lang){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));

            List<Word> words = new ArrayList<>();
            WordDAO dao = WordDAO.getInstance();

            fileReader.lines().forEach((s) -> words.add(new Word(s.substring(0, s.indexOf('=')).trim(), s.substring(s.indexOf('=') + 1, s.length()).trim(), lang)));
            words.parallelStream().forEach(dao::create);
        }
        catch (Exception ex){
            System.err.println("Failed to parse plain file. Trying .csv parsing strategy...");
            parseCSV(file, lang);
        }
    }

    default void parseCSV(File file, Language lang) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] parsedDocLine;
            while ((parsedDocLine = reader.readLine().split(",")) != null) {
                try {
                    Word word = new Word(parsedDocLine[0], parsedDocLine[1], lang);
                    WordDAO.getInstance().create(word);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    Word word = new Word(parsedDocLine[0], "", lang);
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
