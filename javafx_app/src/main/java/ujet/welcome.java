package ujet;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.input.KeyCode;

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
            Stage startStage = new Stage();
            startStage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent

            // Create a new Scene for the Stage
            Scene scene = new Scene(new VBox(), 400, 300);
            
            // Set a key press event handler
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown() && event.getCode() == KeyCode.Q) {
                    if (startStage != null && startStage.isShowing()) {
                        startStage.close();
                    }
                    try {
                        App.setRoot("welcome"); // Switch back to the welcome page
                        ((Stage) App.getScene().getWindow()).show(); // Show the main application window
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            startStage.setScene(scene);
            startStage.show();

            startStage.setOpacity(0.7);

            // Close the welcome page
            ((Stage) App.getScene().getWindow()).hide();
        }
    }
}
