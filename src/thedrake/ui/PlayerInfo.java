package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import thedrake.PlayingSide;

public class PlayerInfo extends VBox {
    private final GameView gameView;
    private final PlayingSide playingSide;
    private final CapturedTroopsView capturedTroopsView;

    public PlayerInfo(GameView gameView, PlayingSide playingSide) {
        super(5);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);

        this.gameView = gameView;
        this.playingSide = playingSide;
        capturedTroopsView = new CapturedTroopsView(gameView, playingSide);

        getChildren().addAll(
                new Text(playingSide.name()),
                new Text("Zajaté jednotky:"),
                capturedTroopsView
        );

        update();
    }

    public void update() {
        Font headingFont = Font.font(
                Font.getDefault().getName(),
                playingSide == gameView.gameState().sideOnTurn()
                        ? FontWeight.BOLD : FontWeight.NORMAL,
                Font.getDefault().getSize() + 3
        );
        ((Text) getChildren().get(0)).setFont(headingFont);
        capturedTroopsView.update();
    }
}
