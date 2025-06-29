package thedrake;

import java.io.PrintWriter;
import java.util.Optional;

public class GameState implements JSONSerializable{
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {
        if(armyOnTurn().boardTroops().at(pos).isPresent())
            return armyOnTurn().boardTroops().at(pos).get();
        if(armyNotOnTurn().boardTroops().at(pos).isPresent())
            return armyNotOnTurn().boardTroops().at(pos).get();
        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        return result == GameResult.IN_PLAY
                && origin != TilePos.OFF_BOARD
                && armyOnTurn().boardTroops().at(origin).isPresent()
                && !armyOnTurn().boardTroops().isPlacingGuards();
    }

    private boolean canStepTo(TilePos target) {
        return result == GameResult.IN_PLAY
                && target != TilePos.OFF_BOARD
                && tileAt(target).canStepOn();
    }

    private boolean canCaptureOn(TilePos target) {
        return result == GameResult.IN_PLAY
                && target != TilePos.OFF_BOARD
                && armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if(result != GameResult.IN_PLAY
                || target == TilePos.OFF_BOARD
                || armyOnTurn().stack().isEmpty()
                || !tileAt(target).canStepOn()
                || tileAt(target).hasTroop())
            return false;

        // Prvni faze - pokladani vudce
        // Polozit muzeme jen do prvni rady
        if(!armyOnTurn().boardTroops().isLeaderPlaced()){
            if(sideOnTurn == PlayingSide.BLUE)
                return target.row() == 1;
            else
                return target.row() == board.dimension();
        }
        // Druha faze hry - pokladani strazi
        // Polozit muzeme jen vedle leadera
        else if(armyOnTurn().boardTroops().isPlacingGuards()){
            return board.at(target).canStepOn() && armyOnTurn().boardTroops().leaderPosition().neighbours().contains(target);
        }
        // Posledni faze hry
        // Polozit muzeme na pole ktere sousedi s nejakou jeho jednotkou
        else{
            for(var t : armyOnTurn().boardTroops().troopPositions()){
                if(target.isNextTo(t)) return true;
            }
            return false;
        }
    }


    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    public Army getBlueArmy() {
        return blueArmy;
    }

    public Army getOrangeArmy() {
        return orangeArmy;
    }

    public GameResult getResult() { return result; }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.write("{\"result\":");
        result.toJSON(writer);
        writer.print(",\"board\":");
        board.toJSON(writer);
        writer.print(",\"blueArmy\":");
        blueArmy.toJSON(writer);
        writer.print(",\"orangeArmy\":");
        orangeArmy.toJSON(writer);
        writer.print("}");
    }
}
