module ujet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens ujet to javafx.fxml;
    exports ujet;
}
