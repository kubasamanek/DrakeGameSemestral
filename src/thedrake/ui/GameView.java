package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import thedrake.GameResult;
import thedrake.GameState;
import thedrake.PlayingSide;

public class GameView extends BorderPane {

    private GameState gameState;
    private BoardView boardView;
    private HandView bluePlayerHandView;
    private HandView orangePlayerHandView;
    private CapturedView blueCapturedView;
    private CapturedView orangeCapturedView;
    private ValidMoves validMoves;
    private TheDrakeApp app;

    private GameEndCallback gameEndCallback;

    public GameView(GameState gameState, GameEndCallback gameEndCallback) {
        this.gameState = gameState;
        this.gameEndCallback = gameEndCallback;

        // Create the board view and hand views
        boardView = new BoardView(gameState, this);
        validMoves = new ValidMoves(gameState);
        bluePlayerHandView = new HandView(gameState, PlayingSide.BLUE, validMoves, boardView);
        orangePlayerHandView = new HandView(gameState, PlayingSide.ORANGE, validMoves, boardView);
        blueCapturedView = new CapturedView(gameState.getBlueArmy().captured(), PlayingSide.BLUE);
        orangeCapturedView = new CapturedView(gameState.getOrangeArmy().captured(), PlayingSide.ORANGE);

        setupLayout();
    }

    public void updateGame(){
        gameState = boardView.getGameState();

        if(gameState.getResult() == GameResult.VICTORY) {
            handleGameEnd(GameResult.VICTORY);
            return;
        }

        validMoves = new ValidMoves(gameState);

        if(validMoves.allMoves().isEmpty()){
            handleGameEnd(GameResult.NO_MOVES);
            return;
        }

        bluePlayerHandView = new HandView(gameState, PlayingSide.BLUE, validMoves, boardView);
        orangePlayerHandView = new HandView(gameState, PlayingSide.ORANGE, validMoves, boardView);
        blueCapturedView = new CapturedView(gameState.getBlueArmy().captured(), PlayingSide.BLUE);
        orangeCapturedView = new CapturedView(gameState.getOrangeArmy().captured(), PlayingSide.ORANGE);
        setupLayout();
    }

    public void handleGameEnd(GameResult result){
        String winner = gameState.sideOnTurn() == PlayingSide.BLUE ? "ORANGE" : "BLUE";

        String winningText = result == GameResult.NO_MOVES ? "No moves possible." : "Drake was captured.";
        Label victoryLabel = new Label(winner + " WINS!");
        Label victoryReason = new Label(winningText);
        victoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        Button newGameButton = new Button("New Game");
        Button backToMenuButon = new Button("Back To Menu");
        newGameButton.setOnAction(e -> startNewGame());
        backToMenuButon.setOnAction(e -> {
            try {
                backToMenu();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox victoryBox = new VBox(10, victoryLabel, victoryReason, newGameButton, backToMenuButon);
        victoryBox.setAlignment(Pos.CENTER);

        this.setCenter(victoryBox);
    }

    private void startNewGame(){
        gameEndCallback.startNewGame();
    }

    private void backToMenu() throws Exception{
        gameEndCallback.showMenuScreen();
    }

    private void setupLayout() {
        // Place the board in the center
        this.setCenter(boardView);

        // Place the player decks above and below the board
        this.setTop(orangePlayerHandView);
        this.setBottom(bluePlayerHandView);

        this.setRight(blueCapturedView);
        this.setLeft(orangeCapturedView);
        // Setting padding and alignment if necessary
        BorderPane.setAlignment(bluePlayerHandView, Pos.CENTER);
        BorderPane.setAlignment(orangePlayerHandView, Pos.CENTER);
        bluePlayerHandView.setPadding(new Insets(10));
        orangePlayerHandView.setPadding(new Insets(10));
    }

    // Additional methods as needed, for example to update the game state
}
