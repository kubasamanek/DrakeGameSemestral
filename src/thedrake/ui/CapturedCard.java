package thedrake.ui;

import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import thedrake.PlayingSide;
import thedrake.Tile;
import thedrake.Troop;
import thedrake.TroopFace;

public class CapturedCard extends Pane {
    private final Troop troop;
    private final PlayingSide side;
    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final int size;

    public CapturedCard(Troop troop, PlayingSide side){
        size = 50;
        this.troop = troop;
        this.side = side;
        setPrefSize(50, 50);
        update();
    }

    public void update() {
        setBackground(backgrounds.getTroop(troop, side, TroopFace.AVERS, 50));
    }

}
