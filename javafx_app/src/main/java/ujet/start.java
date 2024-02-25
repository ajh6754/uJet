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

public class start {
    
    @FXML
    private void switchToWelcome() throws IOException {
        App.setRoot("welcome");
    }

    // NOTE: for testing purposes, simply print out the name of the app that has audio playing. 
    // We can deal with the logistics of the AI detecting speech later.

}
