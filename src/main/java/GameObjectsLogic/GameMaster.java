package GameObjectsLogic;

import GameObjects.Board;

public class GameMaster {
    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;

    public GameMaster(int boardSize){
        Board board = new Board(boardSize);
        this.boardManager = new BoardManager(board);
        this.neighbourManager = new NeighbourManager(board);
        this.captureManager = new CaptureManager(board);
    }
}
