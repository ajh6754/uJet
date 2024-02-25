package ujet;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Screen;

import javafx.scene.input.KeyCode;

public class welcome {

    private Stage startStage = null;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void switchToStart() throws IOException {
        if (startStage == null) {
            // Create a new Stage
            Stage startStage = new Stage();
            startStage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent and hide bar at top
            
            // Create a TextArea for the changing text
            TextArea textArea = new TextArea();
            textArea.setEditable(false); // Make it non-editable

            //textArea.setOpacity(0.5);
            // Set the TextArea background to transparent and the text to be black
            textArea.setStyle("-fx-text-fill: black; -fx-font-size: 20;");
            textArea.appendText("This is a conversation\n" + "Very interesting :D");

            // Create a ScrollPane and add the TextArea to it
            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setContent(textArea);
            
            // Create a VBox and add the ScrollPane to it
            VBox vbox = new VBox(scrollPane);
            VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
            
            // Create a new Scene for the Stage
            Scene scene = new Scene(vbox, 1000, 150);
            //scene.setFill(null);

            // Handle dragging
            vbox.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            vbox.setOnMouseDragged(event -> {
                startStage.setX(event.getScreenX() - xOffset);
                startStage.setY(event.getScreenY() - yOffset);
            });
            
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

            // set location of transcribe box
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
            startStage.setY(screenHeight - scene.getHeight());

            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            startStage.setX((screenWidth - scene.getWidth())/2);

            startStage.setScene(scene);
            startStage.show();

            //startStage.setOpacity(0.7);

            // Close the welcome page
            ((Stage) App.getScene().getWindow()).hide();
        }
    }
}
