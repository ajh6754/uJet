package ujet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import com.sun.jna.Pointer;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    //private static double txtFontSize = 12.0;
    //private static Color txtColor = Color.BLACK;
    

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("welcome"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("uJet");
        stage.show();

        // start pulseaudio, cool beans
        
        ProcessBuilder proc = new ProcessBuilder("pulseaudio", "--start");
        Process pulseaudio = proc.start();

        // shutdown hook to call pulseaudio --kill when this app terminates, async
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // try statement for error checking
            try 
            {
                // create and starts the process 
                ProcessBuilder pb = new ProcessBuilder("pulseaudio", "--kill");
                Process process = pb.start();

                // record the exit code
                int exitCode = process.waitFor();
                if (exitCode == 0) 
                {
                    System.out.println("PulseAudio killed successfully");
                } 
                else 
                {
                    System.err.println("Failed to kill PulseAudio. Exit code: " + exitCode);
                }
            } catch (Exception e) {
                System.err.println("Error killing PulseAudio: " + e.getMessage());
            }
        }));
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    protected static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }

}