package thedrake.ui.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import thedrake.ui.TheDrakeApp;

public class MenuController {

    @FXML
    private Button singleplayerButton;

    @FXML
    private Button multiplayerButton;

    @FXML
    private Button onlineButton;

    @FXML
    private Button exitButton;


    @FXML
    private void handleStartTwoPlayerOffLineGame() {
        // Load the game screen
        TheDrakeApp.startGame((Stage) multiplayerButton.getScene().getWindow());
    }

    @FXML
    private void handleStartTwoPlayerOnlineGame() {
        // Load the game screen
        TheDrakeApp.startGame((Stage) onlineButton.getScene().getWindow());
    }

    @FXML
    private void handleStartSinglePlayerGame() {
        // Load the game screen
        TheDrakeApp.startGame((Stage) singleplayerButton.getScene().getWindow());
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
