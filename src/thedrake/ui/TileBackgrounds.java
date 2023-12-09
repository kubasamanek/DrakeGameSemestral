package thedrake.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

public class TileBackgrounds {

    public static final Background EMPTY_BG = new Background(
            new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null));
    private final Background mountainBg;

    public TileBackgrounds() {
        Image img = new Image(getClass().getResourceAsStream("/assets/mountain.png"));
        this.mountainBg = new Background(
                new BackgroundImage(img, null, null, null, null));
    }

    public Background get(Tile tile) {
        if (tile.hasTroop()) {
            TroopTile armyTile = ((TroopTile) tile);
            return getTroop(armyTile.troop(), armyTile.side(), armyTile.face());
        }

        if (tile == BoardTile.MOUNTAIN) {
            return mountainBg;
        }

        return EMPTY_BG;
    }

    public Background getTroop(Troop info, PlayingSide side, TroopFace face, int size) {
        TroopImageSet images = new TroopImageSet(info.name());
        Image img = images.get(side, face);

        BackgroundSize bgSize = new BackgroundSize(size, size, false, false, false, false);

        // Create a BackgroundImage with the new size
        BackgroundImage bgImage = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                bgSize);

        return new Background(bgImage);
    }

    public Background getTroop(Troop info, PlayingSide side, TroopFace face) {
        TroopImageSet images = new TroopImageSet(info.name());
        BackgroundImage bgImage = new BackgroundImage(
                images.get(side, face), null, null, null, null);

        return new Background(bgImage);
    }



}
