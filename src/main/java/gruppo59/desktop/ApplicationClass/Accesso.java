package gruppo59.desktop.ApplicationClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import gruppo59.desktop.util.Resource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class Accesso extends Application {
    private static Stage Accesso = null;
    private static Stage gestioneVisitatori;
    protected static GestioneVisitatori GestioneVisitatoriController;
    private static Stage temp = gestioneVisitatori;


    public static void accediIsClicked() {
        showGestione();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FileInputStream serviceAccount = null;
        System.out.println("Present Project Directory : " + System.getProperty("user.dir"));

        try {
            serviceAccount = new FileInputStream("ServiceKey.json");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "File di configurazione non trovato");
        }


        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("consigliaviaggi2019-ad0e1.firebaseio.com\"")
                    .build();
        } catch (IOException ex) {

        }

        FirebaseApp.initializeApp(options);


        showAccesso();
    }


    public static void closeAccesso() {
        Accesso.close();
        Accesso = null;
    }


    public static void showAccesso() throws IOException {
        try {
            FXMLLoader root = getFxml("FXMLAccesso");
            Accesso = loadStage(root);
            Accesso.setTitle("Login");
            Accesso.show();

        } catch (IOException e) {
            System.out.println("Errore login");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static FXMLLoader getFxml(String name) throws FileNotFoundException {
        return new FXMLLoader(new Resource(String.format("fxml/%s.fxml", name)).toURL());
    }

    private static Stage loadStage(FXMLLoader loader) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        return stage;
    }

    private static void showGestione() {

        try {

            FXMLLoader loader = getFxml("FXMLGestioneVisitatori");
            gestioneVisitatori = loadStage(loader);

            closeAccesso();
            gestioneVisitatori.setTitle("Gestione Visitatori");
            gestioneVisitatori.show();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}