package tutor.util;

import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Spring on 8/26/2015.
 */
public class BasicParser implements FileParser {
    public BasicParser(){

    }

    private boolean isSuccessfull;
    private int totalWordsAmount;
    private int addedWordsAmount;
    private int ignoredWordsAmount;

    public void parse(File file, Language lang, Language translationLang){
        isSuccessfull = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                String[] parsedDocLine = fileLine.split(",");
                Word word;
                if (parsedDocLine.length  == 2 || parsedDocLine.length == 3) {
                    if (parsedDocLine.length == 3) {  //if contains the article column
                        word = new Word(parsedDocLine[0], parsedDocLine[1], parsedDocLine[2], lang, translationLang);
                    } else {
                        word = new Word(parsedDocLine[0], parsedDocLine[1], lang, translationLang);
                    }
                    if (WordDAO.getInstance().create(word)) {
                        addedWordsAmount++;
                        isSuccessfull = true;
                    } else
                        ignoredWordsAmount++;
                    totalWordsAmount++;
                }
            }
        }
        catch (Exception ex){
            System.err.println("Failed to parse .csv file.");
            ex.printStackTrace();
        }
    }

    public int getTotalWordsAmount(){
        return totalWordsAmount;
    }

    public int getAddedWordsAmount(){
        return addedWordsAmount;
    }

    public int getIgnoredWordsAmount(){
        return ignoredWordsAmount;
    }

    public boolean isSuccessfull(){
        return isSuccessfull;
    }
}
