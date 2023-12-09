package thedrake.ui;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thedrake.Board;
import thedrake.BoardTile;
import thedrake.GameState;
import thedrake.PositionFactory;
import thedrake.StandardDrakeSetup;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class TheDrakeApp extends Application implements GameEndCallback {

    private Stage primaryStage;
    private static TheDrakeApp instance;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        instance = this;
        showMenuScreen(primaryStage);
    }

    private void showMenuScreen(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("menu/menu_screen.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("The Drake Menu");
        primaryStage.show();
    }

    public static void startGame(Stage primaryStage, TheDrakeApp appInstance) {
        GameState gameState = createSampleGameState();
        GameView gameView = new GameView(gameState, appInstance);
        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("The Drake");
        primaryStage.show();
    }

    // Public static method to get the instance
    public static TheDrakeApp getInstance() {
        return instance;
    }

    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
            //.placeFromStack(positionFactory.pos(0, 0))
            //.placeFromStack(positionFactory.pos(3, 3))
            //.placeFromStack(positionFactory.pos(0, 1))
            //.placeFromStack(positionFactory.pos(3, 2))
            //.placeFromStack(positionFactory.pos(1, 0))
            //.placeFromStack(positionFactory.pos(2, 3));
    }

    @Override
    public void startNewGame() {
        startGame(this.primaryStage, this);
    }

    @Override
    public void showMenuScreen() throws Exception {
        showMenuScreen(this.primaryStage);
    }
}
