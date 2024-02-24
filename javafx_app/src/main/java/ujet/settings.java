package ujet;

import java.io.IOException;
import javafx.fxml.FXML;

public class settings {

    @FXML
    private void switchToWelcome() throws IOException {
        App.setRoot("welcome");
    }
}