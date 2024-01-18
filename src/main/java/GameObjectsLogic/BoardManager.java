package GameObjectsLogic;

import GameObjects.Board;
import GameObjects.Stone;
import GameObjects.StoneColor;
import MyExceptions.OccupiedTileException;

public class BoardManager {

    private static Board board;
    public BoardManager(Board board){
        this.board = board;
    }
    public Board getBoard() {
        return board;
    }

    public static Stone getStone(int x, int y){
        return board.getTile(x, y);
    }

    public static void addStone(int x, int y, Stone stone) throws ArrayIndexOutOfBoundsException,
            OccupiedTileException {

        if(!isTileFree(x, y))
            throw new OccupiedTileException("Tile (" + x + "," + y  + ") is already occupied");
        board.setTile(x, y, stone);
    }

    public static void removeStone(int x, int y) throws ArrayIndexOutOfBoundsException{
        board.setTile(x, y, null);
    }

    public static boolean isTileFree(int x, int y) throws ArrayIndexOutOfBoundsException{
        Stone stone = board.getTile(x, y);
        if(stone == null)
            return true;
        else
            return false;
    }

    public static void resetBoard() {
        for(int j = board.getBOARD_SIZE() - 1; j >= 0; j--) {
            for (int i = 0; i < board.getBOARD_SIZE(); i++) {
                board.setTile(i, j, null);
            }
        }
    }
}
