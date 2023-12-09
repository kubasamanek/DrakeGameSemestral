package thedrake.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

public class CardView extends Pane {
    private final Troop troop;
    private final PlayingSide side;
    private final boolean canBePlayed;
    private final HandView hand;
    private final TileBackgrounds backgrounds = new TileBackgrounds();

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
