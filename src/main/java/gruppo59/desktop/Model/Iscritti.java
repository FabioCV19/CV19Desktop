package gruppo59.desktop.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

public class Iscritti {
    private SimpleStringProperty Nome;
    private SimpleStringProperty Cognome;
    private SimpleStringProperty Nickname;
    private SimpleStringProperty Permessi;

    public Iscritti(String nome, String cognome, String nickname, String permessi){
        Nome = new SimpleStringProperty(nome);
        Cognome = new SimpleStringProperty(cognome);
        Nickname = new SimpleStringProperty(nickname);
        Permessi = new SimpleStringProperty(permessi);
    }

    public String getPermessi() {
        return Permessi.get();
    }

    public SimpleStringProperty permessiProperty() {
        return Permessi;
    }

    public void setPermessi(String permessi) {
        this.Permessi.set(permessi);
    }

    public String getNome() {
        return Nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome.set(nome);
    }

    public String getCognome() {
        return Cognome.get();
    }

    public SimpleStringProperty cognomeProperty() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        this.Cognome.set(cognome);
    }

    public String getNickname() {
        return Nickname.get();
    }

    public SimpleStringProperty nicknameroperty() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        this.Nickname.set(nickname);
    }
}
