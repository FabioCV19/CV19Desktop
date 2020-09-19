import gruppo59.desktop.Controller.FXMLControllerIscritti;

import static org.junit.Assert.*;

public class TestingFXMLControllerIscritti {

    FXMLControllerIscritti Controller;

    public void getPermessiEU(){

        Controller = new FXMLControllerIscritti();
        assertEquals(Controller.getUserPermessi(""), "Sospeso");

    }

    public void getPermessiDU(){

        Controller = new FXMLControllerIscritti();
        assertEquals(Controller.getUserPermessi(""), "Attivato");
    }
}
