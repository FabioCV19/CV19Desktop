package gruppo59.desktop.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import gruppo59.desktop.ApplicationClass.GestioneVisitatori;
import gruppo59.desktop.Model.Segnalazioni;
import gruppo59.desktop.util.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;

public class FXMLControllerSegnalazioni implements Initializable {

    private Firestore database = FirestoreClient.getFirestore();
    private int selecetedRow =-1;
    List<String> IdSengnalazioni;
    List<String> IdRecensioni;
    private Segnalazioni segnalazione;
    private static Stage gestioneVisitatori;



    @FXML
    private javafx.scene.control.Hyperlink indietro;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Elimina;

    @FXML
    private Button Annulla;

    @FXML
    private TableView<Segnalazioni> tablesegnalazioni;

    @FXML
    private TableColumn<?, ?> username;

    @FXML
    private TableColumn<?, ?> struttura;

    @FXML
    private TableColumn<?, ?> recensione;

    @FXML
    void annullaIsClicked(ActionEvent event) {
        if(selecetedRow == -1){
            JOptionPane.showMessageDialog(null,"nessuna riga selezionata");
            return;
        }
        String id_segnalazione = IdSengnalazioni.get(selecetedRow);
        CollectionReference segnalazioni = database.collection("Segnalazioni");
        segnalazioni.document(id_segnalazione).delete();



        tablesegnalazioni.getItems().remove(segnalazione);
        IdSengnalazioni.remove(id_segnalazione);
        selecetedRow = -1;
        segnalazione = null;
        JOptionPane.showMessageDialog(null,"Segnalazione eliminata");
    }

    @FXML
    void eliminaIsClicked(ActionEvent event) {
        if(selecetedRow == -1){
            JOptionPane.showMessageDialog(null,"nessuna riga selezionata");
            return;
        }
        String id_segnalazione = IdSengnalazioni.get(selecetedRow);
        String recensione = IdRecensioni.get(selecetedRow);
        String strutturaupdate = null;

        CollectionReference recensioni = database.collection("Recensione");
        CollectionReference segnalazioni = database.collection("Segnalazioni");
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
            if(document.getId().equals(recensione)){
                strutturaupdate = document.getString("struttura");
                recensioni.document(document.getId()).delete();
                break;
            }
        }

        
        if(strutturaupdate != null){
            updateVotoStruttura(strutturaupdate);
        }
        deleteReporting(recensione);
        tablesegnalazioni.getItems().remove(segnalazione);
        IdRecensioni.remove(recensione);
        IdSengnalazioni.remove(id_segnalazione);
        segnalazione = null;
        selecetedRow = -1;
        JOptionPane.showMessageDialog(null,"Recensione eliminata");






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

    private void deleteReporting(String recensione) {
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


        for (QueryDocumentSnapshot document: documents) {
            if(document.getString("recensione").equals(recensione)){
                segnalazioni.document(document.getId()).delete();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Firestore database = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> query_segnalazioni = database.collection("Segnalazioni").get();
        ApiFuture<QuerySnapshot> query_strutture = database.collection("Strutture").get();

        QuerySnapshot querySnapshot_segnalazioni = null;
        QuerySnapshot querySnapshot_strutture = null;
        try {
            querySnapshot_segnalazioni = query_segnalazioni.get();
            querySnapshot_strutture = query_strutture.get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

        username.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        struttura.setCellValueFactory(new PropertyValueFactory<>("struttura"));
        recensione.setCellValueFactory(new PropertyValueFactory<>("testo"));

        ObservableList<Segnalazioni> observableList = FXCollections.observableArrayList();

        List<QueryDocumentSnapshot> documents_segnalazioni = querySnapshot_segnalazioni.getDocuments();
        List<QueryDocumentSnapshot> documents_strutture;
        IdSengnalazioni = new LinkedList<>();
        IdRecensioni = new LinkedList<>();
        Segnalazioni da_inserire;

        for (QueryDocumentSnapshot document : documents_segnalazioni) {
            documents_strutture = querySnapshot_strutture.getDocuments();
            for (QueryDocumentSnapshot document_strutture: documents_strutture) {
                if(document_strutture.getId().equals(document.getString("struttura"))){
                    da_inserire = new Segnalazioni(document.getString("nickname"),document_strutture.getString("nome"),document.getString("testo"));
                    if(!contains(observableList,da_inserire)){
                        IdSengnalazioni.add(document.getId());
                        IdRecensioni.add(document.getString("recensione"));
                        observableList.add(da_inserire);
                    }


                    break;
                }

            }

        }

        tablesegnalazioni.setItems(observableList);
        tablesegnalazioni.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selecetedRow = tablesegnalazioni.getSelectionModel().getSelectedIndex();
                segnalazione = tablesegnalazioni.getSelectionModel().getSelectedItem();

            }
        });
    }

    private boolean contains(ObservableList<Segnalazioni> observableList, Segnalazioni da_inserire) {

        for (Segnalazioni inserite: observableList) {
            if(inserite.equals(da_inserire)){
                return true;

            }
        }

        return false;
    }

    public void indietroIsClicked() {
        try {

            FXMLLoader loader = getFxml("FXMLGestioneVisitatori");
            gestioneVisitatori= loadStage(loader);
            gestioneVisitatori.setTitle("Gestione Visitatori");
            gestioneVisitatori.show();
            GestioneVisitatori.closeSegnalazioni();

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



