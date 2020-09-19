package gruppo59.desktop.Controller;

import gruppo59.desktop.ApplicationClass.GestioneVisitatori;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gruppo59.desktop.ApplicationClass.Accesso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class FXMLControllerGestioneVisitatori {


    @FXML
    private javafx.scene.control.Hyperlink logout;

    @FXML
    private javafx.scene.control.Button Iscritti;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private javafx.scene.control.Button Segnalazioni;

    @FXML
    private javafx.scene.control.Button RichiesteCancellazione;

    @FXML
    void clickRichiesteCancellazione(ActionEvent event) {
        GestioneVisitatori.richiesteCancellazioneIsClicked();
        Stage stage = (Stage) RichiesteCancellazione.getScene().getWindow();
        stage.close();
    }

    @FXML
    void clickIscritti(ActionEvent event) {
        GestioneVisitatori.iscrittiIsClicked();
        Stage stage = (Stage) Iscritti.getScene().getWindow();
        stage.close();
    }


    @FXML
    void clickSegnalazioni(ActionEvent event) {
        GestioneVisitatori.segnalazioniIsClicked();
        Stage stage = (Stage) Segnalazioni.getScene().getWindow();
        stage.close();

    }

    @FXML
    void logOutIsClicked() throws IOException {
        Accesso.showAccesso();
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
    }

}
