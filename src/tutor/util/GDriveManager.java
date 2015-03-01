package tutor.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import tutor.controllers.AuthController;
import tutor.dao.DataSourceDAO;
import tutor.models.DataSource;
import tutor.models.Language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by user on 26.02.2015.
 */
public class GDriveManager {

    private static String CLIENT_ID="250336174195-rfp97d44k25g3mda6gjutis6fm67klfn.apps.googleusercontent.com";
    private static String CLIENT_SECRET="rFlo6WHJWB17loAxxbwx9oIh";

    private static String REDIRECT_URI = "https://www.example.com/oauth2callback";

    private static GDriveManager instance;
    private static String code;
    private static GoogleAuthorizationCodeFlow flow;

    private GDriveManager(){

    }

    public static GDriveManager getInstance(){
        if (instance == null){
            synchronized (GDriveManager.class){
                if (instance == null){
                    instance = new GDriveManager();
                }
            }
        }
        return instance;
    }

    public boolean gotCode(){
        return code != null && !("".equals(code));
    }

    public InputStream getFileInputStream(String fileURL, Language dataLanguage) {
        InputStream inputStream = null;
        boolean isDoc = false;
        try {
            GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
            Drive gDrive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();
            String fileId = null;
            try {
                fileId = fileURL.substring(fileURL.indexOf("=") + 1, fileURL.indexOf("&"));
            }
            catch (StringIndexOutOfBoundsException ex){
                fileId = fileURL.substring(fileURL.indexOf("d/") +2, fileURL.indexOf("/edit"));
            }
            File file = gDrive.files().get(fileId).execute();
            DataSource dataSource = null;
            if ("application/vnd.google-apps.spreadsheet".equals(file.getMimeType())) {
                dataSource = new DataSource(fileURL, DataSourceType.GDRIVE_SPREADSHEET, Service.SERVICE_GDRIVE, dataLanguage);
                isDoc = false;
            }
            else if ("application/vnd.google-apps.document".equals(file.getMimeType())) {
                dataSource = new DataSource(fileURL, DataSourceType.GDRIVE_WORKSHEET, Service.SERVICE_GDRIVE, dataLanguage);
                isDoc = true;
            }
            final DataSource srcForEqualCheck = dataSource;
            boolean hasDuplicates = new DataSourceDAO().readAllByOwner(AuthController.getActiveUser()).stream().anyMatch((src) -> src.equals(srcForEqualCheck));
            if (hasDuplicates) {
                dataSource = new DataSourceDAO().readAllByOwner(AuthController.getActiveUser()).stream().filter((src) -> src.getLanguage().equals(srcForEqualCheck.getLanguage()) && src.getType().equals(srcForEqualCheck.getType()) && src.getLink().equals(srcForEqualCheck.getLink()) && src.getService().equals(srcForEqualCheck.getService())).findFirst().get();
            } else {
                new DataSourceDAO().create(dataSource);
                dataSource = new DataSourceDAO().readAllByOwner(AuthController.getActiveUser()).stream().filter((src) -> src.getLanguage().equals(srcForEqualCheck.getLanguage()) && src.getType().equals(srcForEqualCheck.getType()) && src.getLink().equals(srcForEqualCheck.getLink()) && src.getService().equals(srcForEqualCheck.getService())).findFirst().get();
            }
            String downloadURL;
            if (isDoc){
                downloadURL = file.getExportLinks().get("text/plain");
            }
            else
            {
                downloadURL = file.getExportLinks().get("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            if (downloadURL != null) {
                if (downloadURL.length() > 0) {
                    HttpResponse httpResponse = gDrive.getRequestFactory().buildGetRequest(new GenericUrl(downloadURL)).execute();
                    inputStream = httpResponse.getContent();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getFlowURL(){
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();

        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }
}
