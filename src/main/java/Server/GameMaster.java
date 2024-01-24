package Server;

import GameObjects.Board;
import GameObjects.StoneColor;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.NeighbourManager;

import java.io.IOException;

public class GameMaster {
    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;
    private boolean isGameOngoing;
    private StoneColor currentPlayer = StoneColor.BLACK;


    public GameMaster(int boardSize){
        Board board = new Board(boardSize);
        this.boardManager = new BoardManager(board);
        this.neighbourManager = new NeighbourManager(board);
        this.captureManager = new CaptureManager(board);
    }

    public void handleGameBetweenTwoPlayers() throws IOException, ClassNotFoundException {

        isGameOngoing = true;
        String currentMove;

        while(isGameOngoing){

            System.out.println("Player " + currentPlayer.toString() + " move");
            Server.sendNotification(currentPlayer, "Your move");
            currentMove = Server.receiveNotification(currentPlayer);
            boolean isMoveOK = isMoveValid(currentMove);

            while(!isMoveOK) {
                Server.sendNotification(currentPlayer, "Your move was incorrect");
                currentMove = Server.receiveNotification(currentPlayer);
                isMoveOK = isMoveValid(currentMove);
            }

            System.out.println("Player " + currentPlayer.toString() + " made a move: " + currentMove);
            switchPlayer();
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
    }

    public boolean isMoveValid(String currentMove){

        int x;
        int y;
        try{
            x = Integer.parseInt(currentMove);
        }catch (NumberFormatException e){
            x = 0;
        }

        if(x > 0 && x < 7)
            return true;
        else
            return false;
    }
}