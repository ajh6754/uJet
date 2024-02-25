package ujet;

import static java.lang.Thread.interrupted;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.assemblyai.api.RealtimeTranscriber;
import com.assemblyai.api.resources.realtime.types.FinalTranscript;
import com.assemblyai.api.resources.realtime.types.PartialTranscript;

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

    public TextArea textArea = new TextArea();

    Thread thread = new Thread(() -> {
        try {
            RealtimeTranscriber realtimeTranscriber = RealtimeTranscriber.builder()
                    .apiKey("cebb373d43634ade8fa2a0b9bee83808")
                    .sampleRate(16_000)
                    .onSessionBegins(sessionBegins -> System.out.println(
                            "Session opened with ID: " + sessionBegins.getSessionId()))
                    .onPartialTranscript(transcript -> handlePartialTranscript(transcript))
                    .onFinalTranscript(transcript -> handleFinalTranscript(transcript))
                    .onError(err -> System.out.println("Error: " + err.getMessage()))
                    .endUtteranceSilenceThreshold(700)
                    .build();

            System.out.println("Connecting to real-time transcript service");
            realtimeTranscriber.connect();

            // This is where the magic happens
            System.out.println("Starting recording");
            AudioFormat format = new AudioFormat(16_000, 16, 1, true, false);
            // `line` is your microphone
            TargetDataLine line = AudioSystem.getTargetDataLine(format);
            line.open(format);
            byte[] data = new byte[line.getBufferSize()];
            line.start();
            while (!interrupted()) {
                // Read the next chunk of data from the TargetDataLine.
                line.read(data, 0, data.length);
                realtimeTranscriber.sendAudio(data);
            }

            System.out.println("Stopping recording");
            line.close();

            System.out.println("Closing real-time transcript connection");
            realtimeTranscriber.close();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    });

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
            // Set the stage style to transparent and hide bar at top
            startStage.initStyle(StageStyle.TRANSPARENT);
            
            // Create a TextArea for the changing text
            
            // Make it non-editable
            textArea.setEditable(false);

            //textArea.setOpacity(0.5);
            // Set the TextArea background to transparent and the text to be black
            textArea.setStyle("-fx-text-fill: black; -fx-font-size: 20;");
            textArea.appendText("This is a conversation\n" + "Very interesting :D");
            thread.start();

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
                        thread.interrupt();
                        System.out.println("CLOSING");
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
        
    public String text = "";
    public String current_sentence = "";

    public void handlePartialTranscript(PartialTranscript transcript) {
        if (!transcript.getText().isEmpty()) {
            System.out.println("Partial: " + transcript.getText());
            current_sentence = transcript.getText();
            textArea.setText(text + " " + current_sentence);
        }
    }

    public void handleFinalTranscript(FinalTranscript transcript) {
        System.out.println("Final: " + transcript.getText());
        text = text + " " + transcript.getText();
        textArea.setText(text);
        current_sentence = "";
    }
}