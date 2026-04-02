package eus.ehu.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateControllerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/ehu/presentation/CreatePost.fxml"));
        AnchorPane root = loader.load();
        
        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("Create Post - PickAuto");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
