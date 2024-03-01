module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens GUI to javafx.fxml;
    opens main to javafx.fxml;

    exports GUI;
    exports main;
}