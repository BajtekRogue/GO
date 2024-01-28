package GameObjectsLogic;

import GameObjects.Board;
import GameObjects.Stone;

public class BoardManager {

    private static Board board;
    public BoardManager(Board board){
        BoardManager.board = board;
    }
    public Board getBoard() {
        return board;
    }

    public static Stone getStone(int x, int y){
        return board.getTile(x, y);
    }

    public static void addStone(int x, int y, Stone stone) throws ArrayIndexOutOfBoundsException {
        board.setTile(x, y, stone);
    }

    public static void removeStone(int x, int y) throws ArrayIndexOutOfBoundsException{
        board.setTile(x, y, null);
    }

    public static boolean isTileFree(int x, int y) throws ArrayIndexOutOfBoundsException{
        Stone stone = board.getTile(x, y);
        return stone == null;
    }

    public static void resetBoard() {
        for(int j = board.getBoardSize() - 1; j >= 0; j--) {
            for (int i = 0; i < board.getBoardSize(); i++) {
                board.setTile(i, j, null);
            }
        }
    }
}
