package thedrake.ui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    public void startLocalGame(ActionEvent event) {
        Node target = (Node) event.getTarget();
        Stage stage = (Stage) target.getScene().getWindow();
        stage.setScene(new Scene(new GameView()));
    }

    public void closeStage(ActionEvent event) {
        Node target = (Node) event.getTarget();
        Stage stage = (Stage) target.getScene().getWindow();
        stage.close();
    }
}
