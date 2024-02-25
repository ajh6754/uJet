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

    private Scene settings_scene;

    private double txtFontSize;
    private Color txtColor;

    private File fontsFile = new File("font.properties");
    private File colorFile = new File("color.properties");

    public void loadSizeFromFile(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            txtFontSize = Double.parseDouble(properties.getProperty("txtFontSize"));
        }
    }
    public void loadColorFromFile(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
            txtColor = Color.web(properties.getProperty("txtColor"));
        }
    }
    
    public void saveFontToFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("txtFontSize", Double.toString(txtFontSize));
        try (FileOutputStream out = new FileOutputStream(file)) {
            properties.store(out, null);
        }
    }

    public void saveColorToFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("txtColor", toRgbString(txtColor));
        try (FileOutputStream out = new FileOutputStream(file)) {
            properties.store(out, null);
        }
    }

    public String toRgbString(Color color) {
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

    public File getFontsField() {
        return fontsFile;
    }

    public File getColorField() {
        return colorFile;
    }

    public double getFontSize() {
        return txtFontSize;
    }

    public Color getTxtColor() {
        return txtColor;
    }

    @FXML
    private TextField txtFontField;

    @FXML
    private TextField txtColorField;

    @FXML
    private void adjustFontSize() throws IOException {
        // Get font size from the TextField input
        String fontSizeStr = txtFontField.getText();
        try {
            double newSize = Double.parseDouble(fontSizeStr);
            setFontSize(newSize);
            saveFontToFile(fontsFile);
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
            setTxtColor(newColor);
            saveColorToFile(colorFile);
        } catch (IllegalArgumentException e) {
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