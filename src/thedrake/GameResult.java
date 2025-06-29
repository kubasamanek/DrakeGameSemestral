package thedrake;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY, NO_MOVES;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this.name() + "\"");
    }
}
