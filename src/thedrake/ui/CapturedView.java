package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import thedrake.PlayingSide;
import thedrake.Troop;

import java.util.List;

import static javafx.scene.layout.BorderWidths.*;

public class CapturedView extends VBox {
    private final PlayingSide color;
    private final List<Troop> capturedTroops;
    private Label label;

    public CapturedView(List<Troop> captured, PlayingSide side){
        this.color = side;
        this.capturedTroops = captured;

        this.setSpacing(1);
        this.setPadding(new Insets(10));

        this.setLabel();

        updateCapturedView();

    }

    private void setLabel(){
        this.label = new Label(this.color == PlayingSide.BLUE ? "Blue captured:" : "Orange Captured:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(this.color == PlayingSide.BLUE ? Color.DEEPSKYBLUE : Color.ORANGE);
        label.setPadding(new Insets(5, 10, 5, 10));
        label.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                DEFAULT)));

    }

    private void updateCapturedView(){
        PlayingSide oppositeSide = color == PlayingSide.BLUE ? PlayingSide.ORANGE : PlayingSide.BLUE;

        this.getChildren().clear();
        this.getChildren().add(this.label);

        for (Troop troop : capturedTroops) {
            CapturedCard card = new CapturedCard(troop, oppositeSide);
            HBox hbox = new HBox(card);
            hbox.setAlignment(Pos.CENTER); // To center it horizontally
            this.getChildren().add(hbox);
        }
    }
}
