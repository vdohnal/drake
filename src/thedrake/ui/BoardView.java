package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import thedrake.BoardPos;
import thedrake.GameState;
import thedrake.Move;
import thedrake.PositionFactory;

import java.util.List;

public class BoardView extends GridPane {
    private GameView gameView;

    public BoardView(GameView gameView) {
        this.gameView = gameView;

        GameState gameState = gameView.gameState();
        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                add(new TileView(boardPos, gameState.tileAt(boardPos), gameView), x, y);
            }
        }

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    public void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameView.gameState().tileAt(tileView.position()));
            tileView.update();
        }
    }

    public void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    public void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }
}
