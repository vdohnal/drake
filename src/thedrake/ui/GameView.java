package thedrake.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import thedrake.*;

import java.util.List;
import java.util.Optional;

public class GameView extends BorderPane implements TileViewContext {
    private GameState gameState;
    private ValidMoves validMoves;
    private TileView selected;

    private final BoardView boardView;
    private final TroopStackView blueStack;
    private final TroopStackView orangeStack;
    private final PlayerInfo bluePlayerInfo;
    private final PlayerInfo orangePlayerInfo;

    public GameView() {
        this(createSampleGameState());
    }

    public GameView(GameState gameState) {
        this.gameState = gameState;
        validMoves = new ValidMoves(gameState);
        selected = null;
        boardView = new BoardView(this);
        blueStack = new TroopStackView(this, PlayingSide.BLUE);
        orangeStack = new TroopStackView(this, PlayingSide.ORANGE);
        bluePlayerInfo = new PlayerInfo(this, PlayingSide.BLUE);
        orangePlayerInfo = new PlayerInfo(this, PlayingSide.ORANGE);

        setCenter(boardView);
        setTop(orangeStack);
        setBottom(blueStack);
        setLeft(bluePlayerInfo);
        setRight(orangePlayerInfo);
    }

    public GameState gameState() {
        return gameState;
    }

    private void update() {
        boardView.updateTiles();
        blueStack.update();
        orangeStack.update();
        bluePlayerInfo.update();
        orangePlayerInfo.update();
    }

    private void showVictoryDialog() {
        String winner = gameState.sideOnTurn() == PlayingSide.BLUE
                ? "orange" : "blue";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konec hry");
        alert.setHeaderText("Hráč " + winner + " vyhrál!");
        alert.setContentText("Jak si přejete pokračovat?");

        ButtonType newGameBtn = new ButtonType("Nová hra");
        ButtonType backToMenuBtn = new ButtonType("Zpět do menu");

        alert.getButtonTypes().setAll(newGameBtn, backToMenuBtn);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == newGameBtn){
            gameState = createSampleGameState();
            validMoves = new ValidMoves(gameState);
            selected = null;
            update();
        } else {
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(Drake.getMenu());
            stage.sizeToScene();
        }
    }

    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView)
            selected.unselect();

        selected = tileView;
        boardView.clearMoves();

        if (tileView.position() != null) {
            boardView.showMoves(validMoves.boardMoves(tileView.position()));
            return;
        }

        List<Troop> stack = gameState.armyOnTurn().stack();
        TroopTile tile = (TroopTile) tileView.tile();

        if (!stack.isEmpty() && gameState.sideOnTurn() == tile.side() && stack.get(0) == tile.troop())
            boardView.showMoves(validMoves.movesFromStack());
    }

    @Override
    public void executeMove(Move move) {
        if (selected != null)
            selected.unselect();
        selected = null;
        boardView.clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        update();

        if (gameState.result() == GameResult.VICTORY || validMoves.allMoves().isEmpty())
            showVictoryDialog();
    }
}
