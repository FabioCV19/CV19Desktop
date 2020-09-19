package gruppo59.desktop.Controller;






/*ATTENZIONE: Dato che il costruttore della classe FXMLIscrittiController fa riferimento al database
,il quale deve essere aperto all'interno della app desktop, tale test NON è eseguibile.

Al fine di eseguire il test Black Box del seguente metodo sono state individuate le seguenti
classi di equizalenza:
    Parametro idUtente:
        -CE2: Utente abilitato("Abilitato")
        -CE2: Utente sospeso("Sospeso")

 Per poter tener conto di queste classi di equivalenza sono stati definiti altrettanti casi di test
        getUserStatusTestUtenteSospeso()
        getUserStatusTestUtenteNonSospeso()

  Per il testing White Box invece si tenga in considerazione il seguente grafo del flusso di controllo
  (vedere foto) per questo non sono necessari altri casi di test poichè quelli definiti per la Black Box
  costituiscono una Node ed una Branch Coverage per tale grafo



*/

public class FXMLControllerTestIscritti {

    FXMLControllerIscritti Controller;



    public void getUserStatusTestUtenteSospeso() {
        Controller = new FXMLControllerIscritti();

    }


    public void getUserStatusTestUtenteNonSospeso() {
        Controller = new FXMLControllerIscritti();
        
    }
}