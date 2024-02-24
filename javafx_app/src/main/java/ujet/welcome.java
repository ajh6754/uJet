package ujet;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class welcome {

    private Stage startStage = null; // Add this line

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void switchToStart() throws IOException {
        if (startStage == null) {
            // Create a new Stage
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent

            // Create a new Scene for the Stage
            Scene scene = new Scene(new VBox(), 400, 300);

            stage.setScene(scene);
            stage.show();

            stage.setOpacity(0.7);

            // Close the welcome page
            ((Stage) App.getScene().getWindow()).close();
        }
    }
}
