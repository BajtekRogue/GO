package Bot;

import GameObjects.Coordinates;

public interface MoveStrategy {

    Coordinates makeMove();
    void incrementTriesCounter();
    void resetTriesCounter();


}
