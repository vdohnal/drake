package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import thedrake.PlayingSide;
import thedrake.TroopFace;
import thedrake.TroopTile;

import java.util.List;
import java.util.stream.Collectors;

public class TroopStackView extends HBox {
    private GameView gameView;
    private PlayingSide playingSide;

    public TroopStackView(GameView gameView, PlayingSide playingSide) {
        super(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);

        this.gameView = gameView;
        this.playingSide = playingSide;

        update();
    }

    public void update() {
        List<TileView> tiles = gameView.gameState().army(playingSide).stack().stream()
                .map(troop -> new TileView(null,
                        new TroopTile(troop, playingSide, TroopFace.AVERS),
                        gameView)
                ).collect(Collectors.toList());

        getChildren().setAll(tiles);
    }
}
