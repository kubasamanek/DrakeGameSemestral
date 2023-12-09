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
    private final BoardView boardView;
    private HandView bluePlayerHandView;
    private HandView orangePlayerHandView;
    private CapturedView blueCapturedView;
    private CapturedView orangeCapturedView;
    private ValidMoves validMoves;
    private TheDrakeApp app;
    private final GameEndCallback gameEndCallback;

    public GameView(GameState gameState, GameEndCallback gameEndCallback) {
        this.gameState = gameState;
        this.gameEndCallback = gameEndCallback;

        // Create the board view, hand views and captured views
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

        // Update views
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
        victoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        VBox victoryBox = getEndOptions(winningText, victoryLabel);

        this.setCenter(victoryBox);
    }

    private VBox getEndOptions(String winningText, Label victoryLabel) {
        Label victoryReason = new Label(winningText);

        Button newGameButton = new Button("New Game");
        Button backToMenuButton = new Button("Back To Menu");

        newGameButton.setOnAction(e -> startNewGame());
        backToMenuButton.setOnAction(e -> {
            try {
                backToMenu();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox victoryBox = new VBox(10, victoryLabel, victoryReason, newGameButton, backToMenuButton);
        victoryBox.setAlignment(Pos.CENTER);
        return victoryBox;
    }

    private void startNewGame(){
        gameEndCallback.startNewGame();
    }

    private void backToMenu() throws Exception{
        gameEndCallback.showMenuScreen();
    }

    private void setupLayout() {
        this.setCenter(boardView);
        this.setTop(orangePlayerHandView);
        this.setBottom(bluePlayerHandView);

        this.setRight(blueCapturedView);
        this.setLeft(orangeCapturedView);

        BorderPane.setAlignment(bluePlayerHandView, Pos.CENTER);
        BorderPane.setAlignment(orangePlayerHandView, Pos.CENTER);

        bluePlayerHandView.setPadding(new Insets(10));
        orangePlayerHandView.setPadding(new Insets(10));
    }
}
