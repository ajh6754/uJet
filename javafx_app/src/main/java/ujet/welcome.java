package ujet;

import java.io.IOException;
import javafx.fxml.FXML;

public class welcome {

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void switchToTranscription() throws IOException {
        App.setRoot("settings");
    }
}
