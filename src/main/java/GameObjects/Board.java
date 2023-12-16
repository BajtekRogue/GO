package GameObjects;

import MyExceptions.OccupiedTileException;

public class Board {
    private final int boardSize;
    private Stone[][] board;


    public Board(int boardSize){
        this.boardSize = boardSize;
        board = new Stone[boardSize][boardSize];
    }

    public Stone[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Stone getTile(int x, int y) throws ArrayIndexOutOfBoundsException{
        if(0 <= x && x < boardSize && 0 <= y && y < boardSize)
            return board[x][y];
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect board position");
    }

    public void setTile(int x, int y, Stone stone) throws ArrayIndexOutOfBoundsException {
        if(0 <= x && x < boardSize && 0 <= y && y < boardSize)
            board[x][y] = stone;
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect board position");
    }
}
