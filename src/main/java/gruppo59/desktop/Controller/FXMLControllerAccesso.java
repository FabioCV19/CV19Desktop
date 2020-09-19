package gruppo59.desktop.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import gruppo59.desktop.ApplicationClass.GestioneVisitatori;
import gruppo59.desktop.ApplicationClass.Accesso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;


public class FXMLControllerAccesso {
    private static Stage gestioneVisitatori = null;

    private static GestioneVisitatori gestioneVisitatoriController;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private PasswordField passwd;

    public static GestioneVisitatori getgestioneVisitatoriController() {
        return gestioneVisitatoriController;
    }

    public static void setgestioneVisitatoriController(GestioneVisitatori gestioneVisitatoriController) {
        FXMLControllerAccesso.gestioneVisitatoriController = gestioneVisitatoriController;
    }

    @FXML
    public void accediIsClicked(ActionEvent event) {

        String password= passwd.getText();
        boolean flag=false;
        if(password.equals("admin")){
            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> query = db.collection("Utenti")
                    .whereEqualTo("idUtente","Go3leDhSV1TTPh96uNnQ0H7KNW92")
                    .get();

            QuerySnapshot querySnapshot = null;
            try {
                querySnapshot = query.get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }

            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                if(document.getString("idUtente").equals("Go3leDhSV1TTPh96uNnQ0H7KNW92")){
                    if(document.getBoolean("admin")){
                        JOptionPane.showMessageDialog(null,"Accesso avvenuto con successo");
                        flag=true;
                        Accesso.accediIsClicked( );
                    }else
                        JOptionPane.showMessageDialog(null,"L'account non possiede i permessi");
                    flag=true;
                }
            }
            if(flag==false)
                JOptionPane.showMessageDialog(null,"Attenzione: password errata");
        }else
            JOptionPane.showMessageDialog(null,"Attenzione: password errata");






    }

    @FXML
    void initialize() {

    }




    private static FXMLLoader getFxml(String name) {
        return new FXMLLoader(gruppo59.desktop.ApplicationClass.GestioneVisitatori.class.getResource(
                String.format("%s.fxml", name)));
    }

    private static Stage loadStage(FXMLLoader loader) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        return stage;
    }
}
