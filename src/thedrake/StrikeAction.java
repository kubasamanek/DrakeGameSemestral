package thedrake;

import java.util.ArrayList;
import java.util.List;

public class StrikeAction extends TroopAction {
    public StrikeAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public StrikeAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        // Attack but don't move
        if (state.canCapture(origin, target))
            result.add(new CaptureOnly(origin, (BoardPos) target));

        return result;

    }
}
