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

import java.util.List;

public class GameMaster {
    private final Board board;
    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;
    private boolean isGameOngoing;
    private StoneColor currentPlayer = StoneColor.BLACK;
    private int blackPoints = 0;
    private int whitePoints = 0;
    private int consecutivePasses = 0;


    public GameMaster(int boardSize) {
        this.board = new Board(boardSize);
        this.boardManager = new BoardManager(board);
        this.neighbourManager = new NeighbourManager(board);
        this.captureManager = new CaptureManager(board);
    }

    public String makeAction(String message) throws SuicideException, KOException, OccupiedTileException {
        String[] parts = message.split(" ");
        String messageType = parts[0];
        int x;
        int y;
        switch (messageType) {
            case "Move":
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                String result = handleMove(x, y);

                // if the move is valid switch players and rest passes strike
                if (result.contains("OK")) {
                    switchPlayer();
                    consecutivePasses = 0;
                }

                System.out.println("Feedback: (" + x + "," + y + ") " + result);
                return (result);

            case "Pass":
                switchPlayer();
                System.out.println("PASS");

                consecutivePasses++;
                // if 2 passes in a row end the game
                if (consecutivePasses >= 2)
                    endTheGame();

                return "PASS";
            case "Surrender":
                endTheGame();
                System.out.println("SURRENDER");
                break;
            default:
                System.out.println("Unknown message type: " + messageType);
                break;
        }
        return "It shouldn't be here (GameMaster makeAction)";
    }

    private String handleMove(int x, int y) {

        if (!BoardManager.isTileFree(x, y))
            return "OCCUPIED";

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
            result.append(" REMOVED ");
            for (Coordinates capturedStone : capturedStones) {
                result.append(capturedStone.getX()).append(" ").append(capturedStone.getY()).append(" ");
            }

            if (currentPlayer == StoneColor.BLACK) {
                blackPoints += numberOfCapturedStones;
            } else {
                whitePoints += numberOfCapturedStones;
            }
        }

        // If 1 stone captured save it in order to check for KO later
        if (numberOfCapturedStones == 1)
            ExceptionManager.setKO_coordinates(capturedStones.get(0));
        else
            ExceptionManager.restKO_coordinates();

        // If nothing is captured check for suicide
        if (numberOfCapturedStones == 0 && ExceptionManager.checkForSuicide(x, y)) {
            return "SUICIDE";
        }

        return result.toString();
    }


    private void switchPlayer() {
        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
    }

    private void endTheGame() {
        System.out.println("The game has ended!");
        System.out.println("Black points: " + blackPoints);
        System.out.println("White points: " + whitePoints);
        //add
    }

}