package thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Drake extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("The Drake");
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.setScene(getMenu());
        stage.sizeToScene();
        stage.show();
    }

    public static Scene getMenu() {
        Scene scene = null;

        try {
            Parent root = FXMLLoader.load(Drake.class.getResource("layout.fxml"));
            scene = new Scene(root, 800, 600);
            scene.getStylesheets().add("style.css");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scene;
    }
}
