package gruppo59.desktop.ApplicationClass;

import java.io.FileNotFoundException;
import java.io.IOException;

import gruppo59.desktop.util.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class GestioneVisitatori {
    private static Stage Iscritti;
    private static Stage Segnalazioni;
    private static Stage RichiesteCancellazione;
    protected static GestioneVisitatori GestioneVisitatoriController;


    public static void segnalazioniIsClicked() {

        showSegnalazioni();
    }


    public static void iscrittiIsClicked() {
        showIscritti();

    }

    public static void richiesteCancellazioneIsClicked() {

        showRichiesteCancellazione();
    }


    private static void showIscritti() {
        try {

            FXMLLoader loader = getFxml("FXMLIscritti");
            Iscritti = loadStage(loader);

            //  GestioneVisitatoriController = loader.getController();
            Iscritti.setTitle("Utenti iscritti");
            Iscritti.show();

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private static void showRichiesteCancellazione() {
        try {

            FXMLLoader loader = getFxml("FXMLRichiesteCancellazione");
            RichiesteCancellazione = loadStage(loader);

            //  GestioneVisitatoriController = loader.getController();
            RichiesteCancellazione.setTitle("Richieste cancellazione utente");
            RichiesteCancellazione.show();

        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private static void showSegnalazioni() {
        try {

            FXMLLoader loader = getFxml("FXMLSegnalazioni");
            Segnalazioni = loadStage(loader);

            //  GestioneVisitatoriController = loader.getController();
            Segnalazioni.setTitle("Recensioni segnalate");
            Segnalazioni.show();

        } catch (Exception e) {
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

    public static void closeIscritti() {
        if (Iscritti != null) {
            Iscritti.close();
        } else {
            Iscritti = null;
        }
    }

    public static void closeSegnalazioni() {
        if (Segnalazioni != null) {
            Segnalazioni.close();
        } else {
            Segnalazioni = null;
        }
    }



    public static void closeRichiesteCancellazione() {
        if (RichiesteCancellazione != null) {
            RichiesteCancellazione.close();
        } else {

            RichiesteCancellazione = null;
        }
    }
}