package ujet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("welcome"), 640, 480);
        stage.setScene(scene);
        stage.show();

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