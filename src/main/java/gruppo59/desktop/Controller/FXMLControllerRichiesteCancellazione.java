package gruppo59.desktop.Controller;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import gruppo59.desktop.ApplicationClass.GestioneVisitatori;
import gruppo59.desktop.Model.RichiesteCancellazione;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import gruppo59.desktop.util.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class FXMLControllerRichiesteCancellazione implements Initializable {

    private int selectedRow = -1;
    private List<String> id_users;
    private List<String> DocumentRichiesteCancellazione;
    private Firestore database = FirestoreClient.getFirestore();
    private RichiesteCancellazione richiesta;
    private static Stage gestioneVisitatori;





    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<RichiesteCancellazione> tableRichiestecancellazione;

    @FXML
    private TableColumn<?, ?> email;

    @FXML
    private TableColumn<?, ?> username;

    @FXML
    private TableColumn<?, ?> motivo;

    @FXML
    void confermaIsClicked(ActionEvent event) {
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(null,"nessuna riga selezionata");
            return;
        }

        String id_utente = id_users.get(selectedRow);
        String id_richiesta = DocumentRichiesteCancellazione.get(selectedRow);
        List<String> struttureupdate = new LinkedList<>();

        CollectionReference recensioni = database.collection("Recensione");
        CollectionReference richieste = database.collection("Cancellazioni");
        ApiFuture<QuerySnapshot> query = recensioni.get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }


        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        for (QueryDocumentSnapshot document: documents) {
            if(document.getString("idAutore").equals(id_utente)){
                if(!struttureupdate.contains(document.getString("struttura"))){
                    struttureupdate.add(document.getString("struttura"));
                }
                recensioni.document(document.getId()).delete();
            }
        }

        try {
            FirebaseAuth.getInstance().deleteUser(id_utente);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }


        deleteUserReporting(id_utente);
        deleteDocument(id_utente);
        richieste.document(id_richiesta).delete();
        for (String struttura: struttureupdate) {
            updateVotoStruttura(struttura);
        }
        DocumentRichiesteCancellazione.remove(id_richiesta);
        tableRichiestecancellazione.getItems().remove(richiesta);
        richiesta = null;
        selectedRow = -1;
        JOptionPane.showMessageDialog(null,"Utente eliminato");

    }

    private void updateVotoStruttura(String strutturaupdate) {
        int voto = 0,count = 0,totale = 0;
        double media;
        String valutazione;
        ApiFuture<QuerySnapshot> query = database.collection("Recensione")
                .whereEqualTo("struttura",strutturaupdate)
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
            valutazione = String.valueOf(document.get("voto"));
            voto = Integer.parseInt(valutazione);
            totale = totale + voto;
            count++;
        }
        if(count > 0){
            media = totale/count;
        }else{
            media = 0.0;
        }

        database.collection("Strutture").document(strutturaupdate).update("valutazione",media);
    }

    private void deleteUserReporting(String id_utente) {
        CollectionReference segnalazioni = database.collection("Segnalazioni");
        ApiFuture<QuerySnapshot> query = segnalazioni.get();

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
            if(document.getString("idAutore").equals(id_utente)){
                segnalazioni.document(document.getId()).delete();
            }
        }
    }


    private void deleteDocument(String id_utente) {
        CollectionReference utenti = database.collection("Utenti");
        ApiFuture<QuerySnapshot> query = utenti.get();

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
            if(document.getString("idUtente").equals(id_utente)){
                utenti.document(document.getId()).delete();
                break;
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        ApiFuture<QuerySnapshot> query = database.collection("Cancellazioni").get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        username.setCellValueFactory(new PropertyValueFactory<>("Nickname"));
        motivo.setCellValueFactory(new PropertyValueFactory<>("Motivazione"));
        email.setCellValueFactory(new PropertyValueFactory<>("Email"));

        ObservableList<RichiesteCancellazione> observableList = FXCollections.observableArrayList();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        id_users = new LinkedList<>();
        DocumentRichiesteCancellazione = new LinkedList<>();
        for (QueryDocumentSnapshot document : documents) {
            id_users.add(document.getString("idUtente"));
            DocumentRichiesteCancellazione.add(document.getId());
            observableList.add(new RichiesteCancellazione(document.getString("email"), document.getString("motivazione"), document.getString("nickname")));
        }
        tableRichiestecancellazione.setItems(observableList);
        tableRichiestecancellazione.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedRow = tableRichiestecancellazione.getSelectionModel().getSelectedIndex();
                richiesta = tableRichiestecancellazione.getSelectionModel().getSelectedItem();

            }
        });
    }


    public void indietroIsClicked() {
        try {

            FXMLLoader loader = getFxml("FXMLGestioneVisitatori");
            gestioneVisitatori= loadStage(loader);


            gestioneVisitatori.setTitle("Gestione Visitatori");
            gestioneVisitatori.show();
            GestioneVisitatori.closeRichiesteCancellazione();

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

