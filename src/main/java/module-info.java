module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens org.openjfx to javafx.fxml;
    opens org.openjfx.controllers to javafx.fxml, com.google.gson;
    opens org.openjfx.dataModels to javafx.base, com.google.gson;
    opens org.openjfx.controllers.popupControllers to com.google.gson, javafx.fxml;

    exports org.openjfx;
}