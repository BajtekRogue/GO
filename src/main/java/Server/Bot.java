package Server;

import GameObjects.Coordinates;
import GameObjects.StoneColor;

import java.util.Random;

public class Bot {

    private static StoneColor botColor;
    private final int boardSize;

    public Bot(StoneColor botColor, int boardSize) {
        this.botColor = botColor;
        this.boardSize = boardSize;
    }

    public Coordinates makeMove() {
        Coordinates currentMove = new Coordinates(-1, -1);
        Random random = new Random();
        int x = random.nextInt(boardSize);
        int y = random.nextInt(boardSize);
        currentMove.setCoordinates(x, y);
        return currentMove; // if you cannot make a move return (-1, -1)
    }

    public String adjustMoveMessageFormat(Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        return "Move " + x + " " + y;
    }

    public boolean isMoveValid(String message) {
        return message.contains("OK");
    }

    public String finalBotMove(GameMaster gameMaster) {

        Coordinates currentMove = makeMove();
        String botOutput = "";
        if(currentMove.equals(new Coordinates(-1, -1)))
            return gameMaster.makeAction("Pass");

        while (!isMoveValid(botOutput)) {
            botOutput = gameMaster.makeAction(adjustMoveMessageFormat(makeMove()));
            System.out.println("Bot tries to do: " + botOutput);
        }
        System.out.println("Bot final move: " + botOutput);
        return botOutput;
    }
}
