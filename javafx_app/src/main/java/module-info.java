module ujet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.unsupported;
    //requires com.sun.jna.platform;

    opens ujet to javafx.fxml;
    exports ujet;
}
