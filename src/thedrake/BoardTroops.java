package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable{
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;
    private final boolean guardsPlaced;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
        this.guardsPlaced = false;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards,
            boolean guardsPlaced) {
        this.playingSide=  playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
        this.guardsPlaced = guardsPlaced;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if(troopMap.containsKey(pos))
            return Optional.of(troopMap.get(pos));
        return Optional.empty();
    }

    public PlayingSide playingSide() {
        return this.playingSide;
    }

    public TilePos leaderPosition() {
        return this.leaderPosition;
    }

    public int guards() {
        return this.guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && !guardsPlaced;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target)
            throws IllegalArgumentException {
        if (troopMap.containsKey(target)) throw new IllegalArgumentException();

        TilePos newLeaderPos = leaderPosition;
        if(!isLeaderPlaced()) newLeaderPos = target;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

        boolean areGuardsPlaced = this.guardsPlaced;
        if (isLeaderPlaced() && !areGuardsPlaced && (guards + 1) == 2) {
            areGuardsPlaced = true;
        }
        int newGuards = isPlacingGuards() ? guards + 1 : guards;

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, newGuards, areGuardsPlaced);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target)
        throws IllegalStateException, IllegalArgumentException{
        if(!canMoveTiles()){
            throw new IllegalStateException();
        }
        if(!troopMap.containsKey(origin) || troopMap.containsKey(target)){
            throw new IllegalArgumentException();
        }

        TilePos newLeaderPos = leaderPosition;
        if(origin.equals(leaderPosition)) newLeaderPos = target;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile tmp = newTroopMap.get(origin);
        newTroopMap.remove(origin);
        newTroopMap.put(target, tmp.flipped());

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, guards, guardsPlaced);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards, guardsPlaced);
    }

    public boolean canMoveTiles(){
        return isLeaderPlaced() && !isPlacingGuards();
    }

    public BoardTroops removeTroop(BoardPos target)
        throws IllegalStateException, IllegalArgumentException{
            if(!canMoveTiles()){
                throw new IllegalStateException();
            }
            if(!troopMap.containsKey(target)){
                throw new IllegalArgumentException();
            }

            TilePos newLeaderPos = leaderPosition;
            int newGuards = guards;

            if(target.equals(leaderPosition)) newLeaderPos = TilePos.OFF_BOARD;

            Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
            newTroopMap.remove(target);

            return new BoardTroops(playingSide, newTroopMap, newLeaderPos, newGuards, guardsPlaced);
        }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"side\":");
        playingSide.toJSON(writer);
        writer.print(",\"leaderPosition\":");
        leaderPosition.toJSON(writer);
        writer.print(",\"guards\":" + guards);
        writer.print(",\"troopMap\":{");
        TreeMap<BoardPos, TroopTile> sortedMap = new TreeMap<>(troopMap);

        boolean isFirst = true;
        for (Map.Entry<BoardPos, TroopTile> entry : sortedMap.entrySet()) {
            if (!isFirst) {
                writer.print(",");
            } else {
                isFirst = false;
            }
            entry.getKey().toJSON(writer);
            writer.print(":");
            entry.getValue().toJSON(writer);
        }

        writer.print("}}");

    }
}
