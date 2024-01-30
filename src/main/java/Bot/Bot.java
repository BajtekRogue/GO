package Bot;

import GameObjects.Coordinates;
import Factories.CoordinatesFactory;
import GameObjects.StoneColor;
import Server.GameMaster;

public class Bot {

    private final StoneColor botColor;
    private final MoveStrategy moveStrategy;

    public Bot(StoneColor botColor, int boardSize) {
        this.botColor = botColor;
        //select MoveStrategy here
        //this.moveStrategy = new RandomMoveStrategy(boardSize);
        this.moveStrategy = new MyMoveStrategy(boardSize, botColor);
    }

    public StoneColor getBotColor() {
        return botColor;
    }

    public Coordinates makeMove() {
        return moveStrategy.makeMove();
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
        Coordinates currentMove;
        String botOutput;

        // Ensure that the loop is executed at least once
        do {
            currentMove = makeMove();
            // if bot tries to move at (-1,-1) it means he's passing
            if (currentMove.equals(CoordinatesFactory.createCoordinates(-1, -1))) {
                moveStrategy.resetTriesCounter();
                return gameMaster.makeAction("Pass");
            }

            botOutput = gameMaster.makeAction(adjustMoveMessageFormat(currentMove));
            moveStrategy.incrementTriesCounter();

            while (!isMoveValid(botOutput)) {
                currentMove = makeMove();

                if (currentMove.equals(CoordinatesFactory.createCoordinates(-1, -1))) {
                    moveStrategy.resetTriesCounter();
                    return gameMaster.makeAction("Pass");
                }

                botOutput = gameMaster.makeAction(adjustMoveMessageFormat(currentMove));
                System.out.println("Bot tries to do: " + botOutput);
                moveStrategy.incrementTriesCounter();
            }

            moveStrategy.resetTriesCounter();
            System.out.println("Bot final move: " + botOutput);
        } while (!isMoveValid(botOutput));

        return botOutput;
    }
}
