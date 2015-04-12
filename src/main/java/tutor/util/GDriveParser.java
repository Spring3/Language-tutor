package tutor.util;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import tutor.dao.WordDAO;
import tutor.models.Language;
import tutor.models.Word;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Created by user on 02.03.2015.
 */
public class GDriveParser extends GDriveFileParser{

    public GDriveParser(ResourceBundle bundle){
        this.bundle = bundle;
    }
    private ResourceBundle bundle;

    public void parse(GDriveManager manager, Language lang) {
        if (manager.getDataSourceType().equals(DataSourceType.GDRIVE_WORKSHEET)) {
            new PlainFileParser(bundle).doParsing(manager.getFileInputStream(), lang);
        } else if (manager.getDataSourceType().equals(DataSourceType.GDRIVE_SPREADSHEET)) {
            parseSpreadsheet(manager, lang);
        }
    }

    private void parseSpreadsheet(GDriveManager manager, Language dataLanguage){
        String downloadURL = manager.getFile().getExportLinks().get("text/csv");
        InputStream csvFileInputStream = null;
        try {
            HttpResponse response = manager.getDrive().getRequestFactory().buildGetRequest(new GenericUrl(downloadURL)).execute();
            csvFileInputStream = response.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvFileInputStream));

        String[] parsedDocLine = null;
        try {
            while ((parsedDocLine = reader.readLine().split(",")) != null) {
                try {
                    Word word = new Word(parsedDocLine[0], parsedDocLine[1], dataLanguage);
                    WordDAO.getInstance().create(word);
                }
                catch (ArrayIndexOutOfBoundsException ex){
                    Word word = new Word(parsedDocLine[0], "", dataLanguage);
                    WordDAO.getInstance().create(word);
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        catch (NullPointerException ex){
            //End of spreadsheet file
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
