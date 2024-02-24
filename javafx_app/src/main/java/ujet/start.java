package ujet;

import java.io.IOException;
import javafx.fxml.FXML;

public class start {
    
    @FXML
    private void switchToWelcome() throws IOException {
        App.setRoot("welcome");
    }
}
