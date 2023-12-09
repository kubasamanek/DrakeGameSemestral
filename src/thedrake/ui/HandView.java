package thedrake.ui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import thedrake.*;

import java.io.Console;
import java.util.List;

public class HandView extends HBox{
    private final Army playerArmy;

    private final PlayingSide side;

    private final List<Move> validMoves;

    private final boolean isOnTurn;

    private CardView selectedCard;

    private final BoardView boardView;


    public HandView(GameState gameState, PlayingSide side, ValidMoves validMoves, BoardView boardView) {
        if(side == PlayingSide.BLUE)
            this.playerArmy = gameState.getBlueArmy();
        else
            this.playerArmy = gameState.getOrangeArmy();

        this.side = side;
        this.validMoves = validMoves.movesFromStack();
        this.boardView = boardView;

        isOnTurn = side == gameState.sideOnTurn();

        this.setSpacing(10);

        updateHandView();
    }

    public void setSelectedCard(CardView card) {
        if (selectedCard != null) {
            selectedCard.unselect();
        }
        boardView.unselectAll();
        selectedCard = card;
        updateBoardView();
    }

    public void updateBoardView(){
        boardView.showStackMoves(validMoves);
    }

    private void updateHandView() {
        this.getChildren().clear();
        boolean isFirstInStack = true;
        boolean noMoves = validMoves.isEmpty();

        for (Troop troop : playerArmy.stack()) {
            CardView card = new CardView(troop, side, isFirstInStack && isOnTurn && !noMoves, this);
            isFirstInStack = false;
            this.getChildren().add(card);
        }
    }

}
