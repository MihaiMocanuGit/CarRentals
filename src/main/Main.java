package main;

import GUI.GUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application{
    public static void main ( String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        GUIController controller = new GUIController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI.fxml"));
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();




    }


}
