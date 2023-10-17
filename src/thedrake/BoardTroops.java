package thedrake;

import java.util.*;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide=  playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
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
        return leaderPosition != TilePos.OFF_BOARD
                && guards != 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target)
            throws IllegalArgumentException {
        if (troopMap.containsKey(target)) throw new IllegalArgumentException();
        
        TilePos newLeaderPos = leaderPosition;
        if(!isLeaderPlaced()) newLeaderPos = target;

        int newGuards = isPlacingGuards()
                        ? guards + 1
                        : guards;

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, newGuards);
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

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, guards);
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

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
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
            else newGuards -= 1;

            Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
            newTroopMap.remove(target);

            return new BoardTroops(playingSide, newTroopMap, newLeaderPos, newGuards);
        }
}
