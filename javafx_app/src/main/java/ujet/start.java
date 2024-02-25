package ujet;

import java.io.IOException;
import javafx.fxml.FXML;

public class start {
    
    @FXML
    private void switchToWelcome() throws IOException {
        App.setRoot("welcome");
    }

    // NOTE: for testing purposes, simply print out the name of the app that has audio playing. 
    // We can deal with the logistics of the AI detecting speech later.

}
