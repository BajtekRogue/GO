package Server;

import GameObjects.Board;
import GameObjects.Coordinates;
import GameObjects.Stone;
import GameObjects.StoneColor;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.ExceptionManager;
import GameObjectsLogic.NeighbourManager;
import MyExceptions.KOException;
import MyExceptions.OccupiedTileException;
import MyExceptions.SuicideException;

import java.io.IOException;
import java.util.List;

public class GameMaster {
    private final Board board;
    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;
    private boolean isGameOngoing;
    private StoneColor currentPlayer = StoneColor.BLACK;
    private int BLACK_POINTS;
    private int WHITE_POINTS;


    public GameMaster(int boardSize){
        this.board = new Board(boardSize);
        this.boardManager = new BoardManager(board);
        this.neighbourManager = new NeighbourManager(board);
        this.captureManager = new CaptureManager(board);
    }

    public void handleGameBetweenTwoPlayers() throws IOException, ClassNotFoundException {

        isGameOngoing = true;
        String currentMove;

//        while(isGameOngoing){
//
//            System.out.println("Player " + currentPlayer.toString() + " move");
//            Server.sendMessage(currentPlayer, "Your move");
//            currentMove = Server.receiveMessage(currentPlayer);
//            boolean isMoveOK = isMoveValid(currentMove);
//
//            while(!isMoveOK) {
//                Server.sendMessage(currentPlayer, "Your move was incorrect");
//                currentMove = Server.receiveMessage(currentPlayer);
//                isMoveOK = isMoveValid(currentMove);
//            }
//
//            System.out.println("Player " + currentPlayer.toString() + " made a move: " + currentMove);
//            switchPlayer();
//        }
    }
    public String makeAction(String message) throws SuicideException, KOException, OccupiedTileException {
        String[] parts = message.split(" ");
        char messageType = parts[0].charAt(0);
        int x;
        int y;
        switch (messageType) {
            case 'M':
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                String result = handleMove(x,y);
                String[] resultparts = message.split(" ");
                switchPlayer();
                System.out.println("Odpowiedź: (" + x + "," + y + ") " + result);
                return (result); // Informacja o zajętym polu

            case 'P':
                switchPlayer();
                System.out.println("Pass");
                return "Pass";
            case 'S':
                // dodaj obsługę innych typów wiadomości
                break;
            default:
                System.out.println("Nieznany typ wiadomości: " + messageType);
                break;
        }
        return "Tego nie powinno być widać (GameMaster makeAction)";
    }

    private String handleMove(int x, int y){
        if (!BoardManager.isTileFree(x, y)) {
            return "OCCUPIED";
        }


        // Stores the move
        StringBuilder result = new StringBuilder("OK MOVE ").append(x).append(" ").append(y);

        BoardManager.addStone(x, y, new Stone(currentPlayer));
        NeighbourManager.addNeighbours(x, y);
        NeighbourManager.updateNeighbours(x, y);
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(x, y);

        // If KO return
        if (ExceptionManager.checkForKO(x, y)) {
            return "KO";
        }

        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);

        // If stone placed finish the result (TEMPLATE: "OK MOVE" + placed stone + " REMOVED " + all removed stones
        if (numberOfCapturedStones > 0) {
            switchPlayer();
            result.append(" REMOVED ");
            for (Coordinates capturedStone : capturedStones) {
                result.append(capturedStone.getX()).append(" ").append(capturedStone.getY()).append(" ");
            }

            if (currentPlayer == StoneColor.BLACK) {
                BLACK_POINTS += numberOfCapturedStones;
            } else {
                WHITE_POINTS += numberOfCapturedStones;
            }
        }

        // If nothing is captured check for suicide
        if (numberOfCapturedStones == 0 && ExceptionManager.checkForSuicide(x, y)) {
            return "SUICIDE";
        }

        return result.toString();
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