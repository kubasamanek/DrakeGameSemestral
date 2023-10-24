package thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile{
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face){
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> result = new ArrayList<>();

        for(var a : troop.actions(face)){
            result.addAll(a.movesFrom(pos, side, state));
        }

        return result;
    }

    public PlayingSide side(){
        return this.side;
    }

    public TroopFace face(){
        return this.face;
    }

    public Troop troop(){
        return this.troop;
    }

    public TroopTile flipped(){
        return (face == TroopFace.AVERS)
                ? new TroopTile(troop, side, TroopFace.REVERS)
                : new TroopTile(troop, side, TroopFace.AVERS);

    }
}
