package thedrake.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

public class CardView extends Pane {
    private Troop troop;
    private PlayingSide side;

    private boolean canBePlayed;

    private HandView hand;

    private TileBackgrounds backgrounds = new TileBackgrounds();

    public CardView(Troop troop, PlayingSide side, boolean canBePlayed, HandView hand) {
        this.troop = troop;
        this.side = side;
        this.canBePlayed = canBePlayed;
        this.hand = hand;

        setPrefSize(100, 100);
        setOnMouseClicked(e -> onClick());

        update();
    }

    public void update() {
        setBackground(backgrounds.getTroop(troop, side, TroopFace.AVERS));
    }

    public void onClick(){
        if(canBePlayed) select();
    }

    public void select() {
        hand.setSelectedCard(this);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
    }

    public void unselect() {
        setBorder(null);
    }

}
