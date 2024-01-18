package GameObjectsLogic;

import GameObjects.Board;
import GameObjects.Stone;
import GameObjects.StoneColor;
import MyExceptions.OccupiedTileException;

public class BoardManager {

    private Board board;
    public BoardManager(Board board){
        this.board = board;
    }
    public Board getBoard() {
        return board;
    }

    public Stone getStone(int x, int y){
        return board.getTile(x, y);
    }

    public void addStone(int x, int y, Stone stone) throws ArrayIndexOutOfBoundsException, OccupiedTileException {

        if(!isTileFree(x, y))
            throw new OccupiedTileException("Tile (" + x + "," + y  + ") is already occupied");
        board.setTile(x, y, stone);
    }

    public void removeStone(int x, int y) throws ArrayIndexOutOfBoundsException{
        board.setTile(x, y, null);
    }

    public void printBoard(){
        //clears the terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for(int j = board.getBoardSize() - 1; j >= 0; j--){
            for(int i = 0; i < board.getBoardSize(); i++){

                Stone stone = board.getTile(i, j);
                if(stone == null)
                    System.out.print(" 0 ");
                else if(stone.getStoneColor() == StoneColor.WHITE)
                    System.out.print(" + ");
                else if (stone.getStoneColor() == StoneColor.BLACK)
                    System.out.print(" - ");

            }
            System.out.println();
        }
    }

    public boolean isTileFree(int x, int y) throws ArrayIndexOutOfBoundsException{
        Stone stone = board.getTile(x, y);
        if(stone == null)
            return true;
        else
            return false;
    }

    public void resetBoard() {
        for(int j = board.getBoardSize() - 1; j >= 0; j--) {
            for (int i = 0; i < board.getBoardSize(); i++) {
                board.setTile(i, j, null);
            }
        }
    }
}
