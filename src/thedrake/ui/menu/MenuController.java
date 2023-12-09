package thedrake.ui.menu;

import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import thedrake.ui.TheDrakeApp;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        createBounceAnimation();
    }

    // Make the logo bounce
    private void createBounceAnimation() {
        TranslateTransition bounce = new TranslateTransition(Duration.millis(2000), logoImageView);

        bounce.setByY(-20);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(Animation.INDEFINITE);

        bounce.play();
    }

    @FXML
    private void handleStartTwoPlayerOffLineGame() {
        TheDrakeApp appInstance = TheDrakeApp.getInstance();
        TheDrakeApp.startGame((Stage) multiplayerButton.getScene().getWindow(), appInstance);
    }

    @FXML
    private void handleStartTwoPlayerOnlineGame() {
        TheDrakeApp appInstance = TheDrakeApp.getInstance();
        TheDrakeApp.startGame((Stage) onlineButton.getScene().getWindow(), appInstance);
    }

    @FXML
    private void handleStartSinglePlayerGame() {
        TheDrakeApp appInstance = TheDrakeApp.getInstance();
        TheDrakeApp.startGame((Stage) singleplayerButton.getScene().getWindow(), appInstance);
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
