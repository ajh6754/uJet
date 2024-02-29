package ujet;

import static java.lang.Thread.interrupted;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import okhttp3.internal.http2.Settings;
import javafx.stage.Screen;

import javafx.scene.input.KeyCode;

public class welcome {

    private static Object lock = new Object();

    public TextArea textArea = new TextArea();

    public CheckBox checkbox = new CheckBox();

    public boolean running = true;

    public boolean scrolling = false;

    public Stage startStage;

    private settings settings = new settings();

    private double xOffset = 0;
    private double yOffset = 0;

    public String text = "";
    public String current_sentence = "";

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
            System.out.println("Starting recording!");
            AudioFormat format = new AudioFormat(16_000, 16, 1, true, false);
            // `line` is your microphone
            TargetDataLine line = AudioSystem.getTargetDataLine(format);
            line.open(format);
            byte[] data = new byte[line.getBufferSize()];
            line.start();
            while (!interrupted() && running) {
                // Read the next chunk of data from the TargetDataLine.
                line.read(data, 0, data.length);
                synchronized(lock)
                {
                    realtimeTranscriber.sendAudio(data);
                }
                    
            }

            System.out.println("Stopping recording");
            line.close();

            System.out.println("Closing real-time transcript connection");
            realtimeTranscriber.close();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    });

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void switchToStart() throws IOException {
        if (startStage == null) {

            // get the list of input sinks via pact1, interpret what applications are outputting audio
            ProcessBuilder get_sinks = new ProcessBuilder("pactl", "list", "sink-inputs");
            // Start the process
            Process process = get_sinks.start();

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // the int id and app_name of the sink to save
            int sink_id = 0;
            String app_name = "";

            // initialize the app list

            settings.loadSizeFromFile(settings.getFontsField());
            double fontSize = settings.getFontSize();
            System.out.println(fontSize);

            settings.loadColorFromFile(settings.getColorField());
            Color txtColor = settings.getTxtColor();
            String txtColorStr = settings.toRgbString(txtColor);
            System.out.println(txtColorStr);

            // Create a new Stage
            Stage startStage = new Stage();
            // Set the stage style to transparent and hide bar at top
            startStage.initStyle(StageStyle.TRANSPARENT);
            
            textArea.setWrapText(true);
            // Make it non-editable
            textArea.setEditable(false);

            //textArea.setOpacity(0.5);
            // Set the TextArea background to transparent and the text to be black
            textArea.setStyle("-fx-text-fill:" + txtColorStr + "; -fx-font-size:" + fontSize + ";");
            textArea.appendText("This is a conversation\n" + "Very interesting :D");
            /* 
            thread.start();
            
            startStage.setOnCloseRequest(event -> {
                thread.interrupt(); // Interrupt the thread
                System.out.println("Closing");
            });


            // Create a ScrollPane and add the TextArea to it
            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setContent(textArea);
            
            // Create a VBox and add the ScrollPane to it
            VBox vbox = new VBox();
            vbox.getChildren().addAll(scrollPane, checkbox);
            
            // Create a new Scene for the Stage
            Scene scene = new Scene(vbox, 1000, 150);
            //scene.setFill(null);
            
            checkbox.setLayoutX(10);
            checkbox.setLayoutY(10);

            checkbox.setOnAction(event -> {
                scrolling = true;
            });

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
                        running = false;
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

            // for now, just get it to recognize chrome and whatnot, don't care about anything else.
            
            /**
             * 
             */
            /* 
            while ((line = reader.readLine()) != null) {
                // for every "Sink Input", get the id
                if(line.contains("Sink Input #"))
                    sink_id = Integer.parseInt(line.split("#")[1].trim());

                // for every "application.name" fire a thread that creates a loopback
                if(line.contains("application.name"))
                {
                    // get the name of the app
                    app_name = line.split("=")[1].trim();

                    System.out.println(app_name);

                    // create a defensive copy of the id
                    final int personal_id = sink_id;
                    // create a defensive copy of app_name
                    final String personal_app_name = app_name;

                    // fire the thread off, 
                    /* 
                    Thread computer = new Thread(() -> {
                        Random random = new Random();
                        int r = random.nextInt();

                        // Use the personal app name inside the thread

                        // whenever audio detected, try to copy the stuff from the other threads, but get the data

                        // create the new loopback
                        ProcessBuilder get_loopback = new ProcessBuilder("pactl", "load-module", ""+personal_id);
                        // Start the process
                        try {
                            Process create_loopback = get_loopback.start();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        // create the eternal while loop or something idk

                        // this while loop will work infinitely getting bytes

                        while(true)
                        {
                            // lock so that the commands are ok
                            // the audio buffer
                            byte[] buffer = new byte[4096];
                            int bytes_read = 0;
                            //parec --format=s16le --rate=44100 --channels=2 --device=loopback_device_name
                            ProcessBuilder bytesGetter = new ProcessBuilder("parec", "--format=s16le", "--rate=44100", "--channels=2", "--device=bluez_output.7C_96_D2_91_12_71.1.monitor");//personal_app_name);
                            try {
                                Process getting_bytes = bytesGetter.start();
                                InputStream inputStream = getting_bytes.getInputStream();
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

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
                                System.out.println("Starting recording!");
                                AudioFormat format = new AudioFormat(16_000, 16, 1, true, false);

                                //byte[] data = new byte[4096];

                                // read bytes until over
                                while((bytes_read = inputStream.read(buffer)) != -1)
                                {
                                    outputStream.write(buffer, 0, bytes_read);
                                    synchronized(lock)
                                    {
                                        realtimeTranscriber.sendAudio(buffer);
                                    }

                                    // flush the output stream
                                    outputStream.flush();

                                    // if i were to send these bytes, i'd need to send the name/id as well for easy differentiation
                                }
                                // wait for parex to end
                                process.waitFor();
                                
                                //inputStream.close();
                                //outputStream.close();
                                realtimeTranscriber.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        
                    });
                    computer.start(); // Start the thread
                    
                }
            }*/

        }
    }

    public void handlePartialTranscript(PartialTranscript transcript) {
        if (!transcript.getText().isEmpty()) {
            System.out.println("Partial: " + transcript.getText());
            current_sentence = transcript.getText();
            textArea.setText(text + " " + current_sentence);


            if (scrolling){
                textArea.selectPositionCaret(textArea.getLength());
            }

        }
    }

    public void handleFinalTranscript(FinalTranscript transcript) {
        System.out.println("Final: " + transcript.getText());
        text = text + " " + transcript.getText();
        textArea.setText(text);
        current_sentence = "";

        if (scrolling){
            textArea.selectPositionCaret(textArea.getLength());
        }

    }

    @FXML
    // First, get the different audio sources.
    // Then, make it switch to a different pane that captions.
    private void testAudio()
    {
        // at this point, the server is already activated- just need to use C functions to get what we need.

        // OK, so initializes the audio sources to get stuff from- call this in a thread.
        Thread initialize = new Thread(() ->
        {
            PulseAudioLibrary.AUDIO_LIBRARY.initialize_sources();
        });
        initialize.start();

        System.out.println("Finished");

        // TODO - later, change it so that both the pipes and your speakers get the audio,but for now get it isolated.
        
        // When obtaining audio from the pipe, check the last time it was updated. If it's the same, then skip.

        // Make sure you DELETE the pipe in tmp...
    }
}
