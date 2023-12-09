package thedrake.ui;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

import static javafx.scene.layout.BorderWidths.DEFAULT;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selected;

    private GameView gameView;

    public BoardView(GameState gameState, GameView gameView) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        setBorderToPlayingSide();

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView)
            selected.unselect();


        selected = tileView;

        if(validMoves.boardMoves(tileView.position()).isEmpty())
            selected.unselect();

        gameView.updateGame();
        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void executeMove(Move move) {
        if(selected == null){
            gameState = gameState.placeFromStack(move.target());
            clearMoves();
            validMoves = new ValidMoves(gameState);
            updateTiles();
            gameView.updateGame();
            return;
        }

        selected.unselect();
        selected = null;
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();
        gameView.updateGame();
    }

    public void unselectAll(){
        if(selected != null){
            selected.unselect();
        }
    }

    private void updateTiles() {
        setBorderToPlayingSide();
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }

    private void setBorderToPlayingSide(){
        this.setBorder(new Border(new BorderStroke(
                gameState.sideOnTurn() == PlayingSide.BLUE ? Color.DEEPSKYBLUE : Color.ORANGE,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                new BorderWidths(5))));
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    public void showStackMoves(List<Move> moveList){
        clearMoves();
        showMoves(moveList);
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }


    public GameState getGameState() {
        return gameState;
    }
}
