package GameObjects;

public class Board {
    private final int BOARD_SIZE;
    private final Stone[][] board;


    public Board(int boardSize){
        this.BOARD_SIZE = boardSize;
        board = new Stone[boardSize][boardSize];
    }

    public Stone[][] getBoard() {
        return board;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public Stone getTile(int x, int y) throws ArrayIndexOutOfBoundsException{
        if(0 <= x && x < BOARD_SIZE && 0 <= y && y < BOARD_SIZE)
            return board[x][y];
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect board position");
    }

    public void setTile(int x, int y, Stone stone) throws ArrayIndexOutOfBoundsException {
        if(0 <= x && x < BOARD_SIZE && 0 <= y && y < BOARD_SIZE)
            board[x][y] = stone;
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect board position");
    }
}
