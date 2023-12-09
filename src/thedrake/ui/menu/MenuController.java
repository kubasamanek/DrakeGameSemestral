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
    private StackPane cubeContainer;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        //cubeContainer.getChildren().add(createRotatingCube());
        createBounceAnimation();
    }

    private void createBounceAnimation() {
        TranslateTransition bounce = new TranslateTransition(Duration.millis(2000), logoImageView);
        bounce.setByY(-20);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);

        bounce.setCycleCount(Animation.INDEFINITE);

        bounce.play();
    }

    private SubScene createRotatingCube() {
        // Create a simple cube
        Box cube = new Box(150, 150, 150); // You can adjust the size
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLUE); // Adjust color as needed
        cube.setMaterial(material);

        // Rotate the cube
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), cube);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.play();

        // Group to hold the cube
        Group group = new Group();
        group.getChildren().add(cube);

        // Create a SubScene
        SubScene subScene = new SubScene(group, 150, 150, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.TRANSPARENT);

        // Set up a camera
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-200); // Move the camera back to view the cube
        camera.setTranslateY(-50);  // Adjust the vertical position if needed
        camera.setTranslateX(-100);  // Adjust the horizontal position if needed

        subScene.setCamera(camera);

        return subScene;
    }

    @FXML
    private void handleStartTwoPlayerOffLineGame() {
        TheDrakeApp appInstance = TheDrakeApp.getInstance();
        appInstance.startGame((Stage) multiplayerButton.getScene().getWindow(), appInstance);
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
