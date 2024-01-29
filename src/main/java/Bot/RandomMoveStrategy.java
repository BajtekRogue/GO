package Bot;

import GameObjects.Coordinates;
import Factories.CoordinatesFactory;

import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {
    private final int boardSize;
    private final Random random = new Random();
    private int consecativeTriesCounter = 0;

    public RandomMoveStrategy(int boardSize) {
        this.boardSize = boardSize;
    }

    @Override
    public Coordinates makeMove() {
        if (consecativeTriesCounter == boardSize * boardSize) {
            return CoordinatesFactory.createCoordinates(-1, -1);
        }
        int x = random.nextInt(boardSize);
        int y = random.nextInt(boardSize);
        System.out.println("Bot is at tries: " + consecativeTriesCounter);
        return CoordinatesFactory.createCoordinates(x, y);
    }

    @Override
    public void incrementTriesCounter() {
        consecativeTriesCounter++;
    }

    @Override
    public void resetTriesCounter() {
        consecativeTriesCounter = 0;
    }
}