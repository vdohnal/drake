package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import thedrake.PlayingSide;
import thedrake.Troop;

import java.util.List;
import java.util.stream.Collectors;

public class CapturedTroopsView extends VBox {
    private GameView gameView;
    private PlayingSide playingSide;

    public CapturedTroopsView(GameView gameView, PlayingSide playingSide) {
        super(5);
        setAlignment(Pos.TOP_CENTER);

        this.gameView = gameView;
        this.playingSide = playingSide;

        update();
    }

    public void update() {
        List<Troop> captured = gameView.gameState().army(playingSide).captured();
        List<Text> troopNames = captured.stream()
                .map(troop -> new Text(troop.name()))
                .collect(Collectors.toList());

        getChildren().setAll(troopNames);
    }
}
