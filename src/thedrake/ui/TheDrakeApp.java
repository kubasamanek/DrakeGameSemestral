package thedrake.ui;

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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        GameState gameState = createStartingGameStateWithRandomMountains();
        GameView gameView = new GameView(gameState, appInstance);
        Scene scene = new Scene(gameView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("The Drake");
        primaryStage.show();
    }

    public static TheDrakeApp getInstance() {
        return instance;
    }

    private static GameState createStartingGameState() {
        Board board = new Board(4);
        return new StandardDrakeSetup().startState(board);
    }

    public static GameState createStartingGameStateWithRandomMountains() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();

        Random random = new Random();
        int numberOfMountains = 2 + random.nextInt(3);

        Set<Board.TileAt> mountainTiles = new HashSet<>();
        while (mountainTiles.size() < numberOfMountains) {
            int x = random.nextInt(4);
            int y = random.nextInt(4);

            mountainTiles.add(new Board.TileAt(positionFactory.pos(x, y), BoardTile.MOUNTAIN));
        }

        for (Board.TileAt tile : mountainTiles) {
            board = board.withTiles(tile);
        }

        return new StandardDrakeSetup().startState(board);
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
