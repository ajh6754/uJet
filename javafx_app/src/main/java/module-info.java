module ujet {
    requires javafx.controls;
    requires javafx.fxml;

    opens ujet to javafx.fxml;
    exports ujet;
}
