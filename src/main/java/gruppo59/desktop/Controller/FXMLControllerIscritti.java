package gruppo59.desktop.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import gruppo59.desktop.ApplicationClass.GestioneVisitatori;
import gruppo59.desktop.Model.Iscritti;
import gruppo59.desktop.util.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import javax.swing.*;

public class FXMLControllerIscritti implements Initializable {

    private Firestore database = FirestoreClient.getFirestore();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Iscritti selectedUser;
    private int selectedRow = -1;
    private List<String> id_utenti;
    private static Stage gestioneVisitatori;



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Iscritti> tableIscritti;

    @FXML
    private TableColumn<?, ?> username;

    @FXML
    private TableColumn<?, ?> cognome;

    @FXML
    private TableColumn<?, ?> nome;

    @FXML
    private TableColumn<?,?> permessi;


    public void attivaIsClicked(javafx.event.ActionEvent actionEvent) {

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(null,"Nessun utente selezionato");
            return;
        }

        String id_utente = id_utenti.get(selectedRow);


        //Metodo firebase richiesta sospensione account

        try {
            if(!mAuth.getUser(id_utente).isDisabled()){
                JOptionPane.showMessageDialog(null,"L'utente è già Attivato");
            }else{
                UserRecord.UpdateRequest request= new UserRecord.UpdateRequest(id_utente).setDisabled(false);
                mAuth.updateUser(request);
                selectedUser.setPermessi("Attivato");
                tableIscritti.getItems().set(selectedRow, selectedUser);
                JOptionPane.showMessageDialog(null,"L'utente è stato Attivato");

            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        selectedRow = -1;
    }




    public void sospendiIsClicked(javafx.event.ActionEvent actionEvent) {

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(null,"nessun utente selezionato");
            return;
        }

        String id_utente = id_utenti.get(selectedRow);


    //Metodo firebase richiesta sospensione account

        try {
            if(mAuth.getUser(id_utente).isDisabled()){
                JOptionPane.showMessageDialog(null,"L'utente è già sospeso");
            }else{
                UserRecord.UpdateRequest request= new UserRecord.UpdateRequest(id_utente).setDisabled(true);
                mAuth.updateUser(request);
                selectedUser.setPermessi("Sospeso");
                tableIscritti.getItems().set(selectedRow, selectedUser);
                JOptionPane.showMessageDialog(null,"L'utente è stato sospeso");


            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        selectedRow = -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ApiFuture<QuerySnapshot> query = database.collection("Utenti").get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        username.setCellValueFactory(new PropertyValueFactory<>("Nickname"));
        cognome.setCellValueFactory(new PropertyValueFactory<>("Cognome"));
        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        permessi.setCellValueFactory(new PropertyValueFactory<>("Permessi"));

        ObservableList<Iscritti> observableList = FXCollections.observableArrayList();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        id_utenti = new LinkedList<>();
        String permessi;

        for (QueryDocumentSnapshot document : documents) {
            id_utenti.add(document.getString("idUtente"));
            permessi = getUserPermessi(document.getString("idUtente"));
            observableList.add(new Iscritti(document.getString("nome"),document.getString("cognome"),document.getString("nickname"),permessi));
        }
        tableIscritti.setItems(observableList);
        tableIscritti.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedUser = tableIscritti.getSelectionModel().getSelectedItem();
                selectedRow = tableIscritti.getSelectionModel().getSelectedIndex();
            }
        });
    }

    public String getUserPermessi(String idUtente) {
        String permessi = " ";
        try {
            if(!mAuth.getUser(idUtente).isDisabled()){
                permessi = "Attivato";
            }else{
                permessi = "Sospeso";
            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        return permessi;
    }


    public void indietroIsClicked() {
        try {

            FXMLLoader loader = getFxml("FXMLGestioneVisitatori");
            gestioneVisitatori= loadStage(loader);
            gestioneVisitatori.setTitle("Gestione Visitatori");
            gestioneVisitatori.show();
            GestioneVisitatori.closeIscritti();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    private static FXMLLoader getFxml(String name) throws FileNotFoundException {
        return new FXMLLoader(new Resource(String.format("fxml/%s.fxml", name)).toURL());
    }

    private static Stage loadStage(FXMLLoader loader) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        return stage;
    }

}
