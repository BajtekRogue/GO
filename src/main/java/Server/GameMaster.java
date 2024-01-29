package Server;

import Bot.Bot;
import Factories.StoneFactory;
import GameObjects.*;
import GameObjectsLogic.*;


import java.util.List;

public class GameMaster {
    private StoneColor currentPlayer = StoneColor.BLACK;
    private int blackPoints = 0;
    private int whitePoints = 0;
    private int consecutivePasses = 0;
    private Bot bot;

    public GameMaster(int boardSize) {
        Board board = new Board(boardSize);
        BoardManager boardManager = new BoardManager(board);
        NeighbourManager neighbourManager = new NeighbourManager(board);
        CaptureManager captureManager = new CaptureManager(board);
        TerritoryManager territoryManager = new TerritoryManager(board);
    }

    public void setBot(StoneColor stoneColor, int boardSize){
        bot = new Bot(stoneColor, boardSize);
    }

    public Bot getBot(){
        return bot;
    }

    public String makeAction(String message){
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
                    return endTheGame("ENDGAME");

                return "PASS";
            case "Surrender":
                System.out.println("SURRENDER");
                return endTheGame("SURRENDER");
            default:
                System.out.println("Unknown message type: " + messageType);
                return "Unknown message type";
        }
    }

    public String handleMove(int x, int y) {

        if (!BoardManager.isTileFree(x, y))
            return "OCCUPIED";

        // Stores the move
        StringBuilder result = new StringBuilder("OK MOVE ").append(x).append(" ").append(y);

        BoardManager.addStone(x, y, StoneFactory.createStoneInColor(currentPlayer));
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

    public String endTheGame(String typeOfEnd) {
        List<Coordinates> territoryWhite = TerritoryManager.lookForTerritory(StoneColor.WHITE);
        List<Coordinates> territoryBlack = TerritoryManager.lookForTerritory(StoneColor.BLACK);
        whitePoints += territoryWhite.size();
        blackPoints += territoryBlack.size();
        System.out.println("The game has ended!");
        System.out.println("Black points: " + blackPoints);
        System.out.println("White points: " + whitePoints);
        return typeOfEnd + " Black points: " + blackPoints + " White points: " + whitePoints;
    }

}