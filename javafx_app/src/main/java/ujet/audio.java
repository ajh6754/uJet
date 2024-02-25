package ujet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

//import org.jitsi.*;

// if we can somehow access the software mixer, we should be able to have access to the apps sending info to the mixer?

public class audio {
    // VARIABLES

    // loopback list is a bunch of loopbacks

    // lock for writing to stdout
    private static Object myLock = new Object();


    // realistically, loopbacks could potentially be used, the overhead just sucks

    // you would need to both play the sound and route it to the API, we can try but that could slow things down

    // the way you would achieve it is by just creating a loopback on all input sinks. We don't care about customizibility yet, just get it working.

    // Print that Google Chrome is saying stuff, but the latency related crap might be hard...


    // starts the PulseAudio Daemon for intercepting audio channels
    public void initialize() throws IOException, InterruptedException
    {
        
    }

    @FXML
    private void getInfo()
    {

    }

    @FXML
    private void toggleMic(){

    }

    @FXML
    private void toggleDiscord(){

    }

    @FXML
    private void toggleChrome(){

    }

    @FXML
    private CheckBox microphoneAudio;
    
    @FXML
    private CheckBox chromeAudio;

    @FXML
    private CheckBox discordAudio;

    @FXML
    private Button save;

    // Initialize your controller
    @FXML
    public void initialize() {
        save.setOnAction(event -> {
            saveSettings();
        });
    }

    @FXML
    private void saveSettings(){
        // Get a reference to the stage
        Stage stage = (Stage) save.getScene().getWindow();
        // Close it
        stage.close();
    }
}

