package ujet;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class settings {

    // VARIABLES

    private Scene settings_scene;

    // FUNCTIONS

    @FXML
    private void switchToWelcome() throws IOException {
        App.setRoot("welcome");
    }

    @FXML
    private void openAudioWindow() throws IOException {
        // creates new window
        Stage stage = new Stage();
        stage.setTitle("Filter Audio");
        stage.setScene(new Scene(App.loadFXML("audio"), 450, 450));
        stage.show();
    }
}