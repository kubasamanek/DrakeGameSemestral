package thedrake;

public class Board {
    private final int dimension;
    private final BoardTile[][] board;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        board = new BoardTile[dimension][dimension];

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                board[i][j] = BoardTile.EMPTY;
            }
        }
    }

    // Rozměr hrací desky
    public int dimension() {
        return dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return board[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(this.dimension);
        for(int i = 0; i < this.dimension; i++) {
            System.arraycopy(this.board[i], 0, newBoard.board[i], 0, this.dimension);
        }

        for(TileAt tileAt : ats) {
            newBoard.board[tileAt.pos.i()][tileAt.pos.j()] = tileAt.tile;
        }

        return newBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

