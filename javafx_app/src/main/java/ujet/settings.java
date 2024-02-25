package ujet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class settings {

    // VARIABLES

    private Scene settings_scene;

    private settings settings;

    private double txtFontSize;
    private Color txtColor;

    public void loadFromFile(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            txtFontSize = Double.parseDouble(properties.getProperty("fontSize"));
            txtColor = Color.web(properties.getProperty("txtColor"));
        }
    }

    public void saveToFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("txtFontSize", Double.toString(txtFontSize));
        properties.setProperty("txtColor", toRgbString(txtColor));
        try (FileOutputStream out = new FileOutputStream(file)) {
            properties.store(out, null);
        }
    }

    private String toRgbString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    private void setFontSize(double newSize){
        txtFontSize = newSize;
    }

    private void setTxtColor(Color rgbColor){
        txtColor = rgbColor;
    }

    @FXML
    private TextField txtFontField;

    @FXML
    private TextField txtColorField;

    // FUNCTIONS

    @FXML
    private void adjustFontSize() throws IOException {
        // Get font size from the TextField input
        String fontSizeStr = txtFontField.getText();
        try {
            double newSize = Double.parseDouble(fontSizeStr);
            settings.setFontSize(newSize);
            settings.saveToFile(new File("settings.properties"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid font size input");
        }
    }

    @FXML
    private void adjustTxtColor() throws IOException {
        // Get color from the TextField input
        String colorStr = txtColorField.getText();
        try {            
            // Parse the color, assuming input is in the format "#RRGGBB" or other valid CSS color formats
            Color newColor = Color.web(colorStr);
            settings.setTxtColor(newColor);
            settings.saveToFile(new File("settings.properties"));
        } catch (IllegalArgumentException e) {
            // Handle invalid color input (e.g., show an error message)
            System.err.println("Invalid color input");
        }
    }

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