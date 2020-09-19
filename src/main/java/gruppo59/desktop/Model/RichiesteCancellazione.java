package gruppo59.desktop.Model;

import javafx.beans.property.SimpleStringProperty;

public class RichiesteCancellazione  {

    private SimpleStringProperty Email;
    private SimpleStringProperty Motivazione;
    private SimpleStringProperty Nickname;

    public RichiesteCancellazione(String email, String motivazione, String nickname) {
        Email = new SimpleStringProperty(email);
        Motivazione =new SimpleStringProperty(motivazione);
        Nickname = new SimpleStringProperty(nickname);
    }

    public String getEmail() {
        return Email.get();
    }

    public SimpleStringProperty emailProperty() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email.set(email);
    }

    public String getMotivazione() {
        return Motivazione.get();
    }

    public SimpleStringProperty motivazioneProperty() {
        return Motivazione;
    }

    public void setMotivazione(String motivazione) {
        this.Motivazione.set(motivazione);
    }

    public String getNickname() {
        return Nickname.get();
    }

    public SimpleStringProperty nicknameProperty() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        this.Nickname.set(nickname);
    }
}
