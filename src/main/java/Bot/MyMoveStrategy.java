package Bot;

import Factories.CoordinatesFactory;
import GameObjects.*;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.NeighbourManager;

import java.util.*;

public class MyMoveStrategy implements MoveStrategy {
    private final int boardSize;
    private final Random random = new Random();
    private int consecativeTriesCounter = 0;
    private final StoneColor botColor;
    private boolean hasTarget = false;
    private Coordinates target;
    private boolean visited[][];
    private List<Coordinates> historyOfTargets = new ArrayList<>();

    public MyMoveStrategy(int boardSize, StoneColor botColor) {
        this.boardSize = boardSize;
        this.botColor = botColor;
        this.visited = new boolean[boardSize][boardSize];
    }

    @Override
    public Coordinates makeMove() {
        if (consecativeTriesCounter == boardSize * boardSize) {
            resetVisited();
            hasTarget = false;
            target = null;
            return CoordinatesFactory.createCoordinates(-1, -1);
        }
        if (hasTarget && BoardManager.isTileFree(target.getX(), target.getY())) {
            return resetAlgorithm();
        }
        if (!hasTarget || didTargetRepeat(target)) {
            return resetAlgorithm();
        }

        System.out.println("TARGET: (" + target.getX() + " , " + target.getY() + ")");
        StoneNeighbours targetNeighbours = NeighbourManager.getArrayOfNeighbours().getNeighbours(target.getX(),
                target.getY());

        if (targetNeighbours.getNorth() == NeighbourState.EMPTY && !visited[target.getX()][target.getY() + 1]) {
            visited[target.getX()][target.getY() + 1] = true;
            return CoordinatesFactory.createCoordinates(target.getX(), target.getY() + 1);
        } else if (targetNeighbours.getEast() == NeighbourState.EMPTY && !visited[target.getX() + 1][target.getY()]) {
            visited[target.getX() + 1][target.getY()] = true;
            return CoordinatesFactory.createCoordinates(target.getX() + 1, target.getY());
        } else if (targetNeighbours.getSouth() == NeighbourState.EMPTY && !visited[target.getX()][target.getY() - 1]) {
            visited[target.getX()][target.getY() - 1] = true;
            return CoordinatesFactory.createCoordinates(target.getX(), target.getY() - 1);
        } else if (targetNeighbours.getWest() == NeighbourState.EMPTY && !visited[target.getX() - 1][target.getY()]) {
            visited[target.getX() - 1][target.getY()] = true;
            return CoordinatesFactory.createCoordinates(target.getX() - 1, target.getY());
        }

        if (targetNeighbours.getNorth() == NeighbourState.ALLY && !visited[target.getX()][target.getY() + 1]) {
            visited[target.getX()][target.getY() + 1] = true;
            target = CoordinatesFactory.createCoordinates(target.getX(), target.getY() + 1);
            historyOfTargets.add(CoordinatesFactory.createCoordinates(target.getX(), target.getY() + 1));
            return makeMove();
        } else if (targetNeighbours.getEast() == NeighbourState.ALLY && !visited[target.getX() + 1][target.getY()]) {
            visited[target.getX() + 1][target.getY()] = true;
            target = CoordinatesFactory.createCoordinates(target.getX() + 1, target.getY());
            historyOfTargets.add(CoordinatesFactory.createCoordinates(target.getX() + 1, target.getY()));
            return makeMove();
        } else if (targetNeighbours.getSouth() == NeighbourState.ALLY && !visited[target.getX()][target.getY() - 1]) {
            visited[target.getX()][target.getY() - 1] = true;
            target = CoordinatesFactory.createCoordinates(target.getX(), target.getY() - 1);
            historyOfTargets.add(CoordinatesFactory.createCoordinates(target.getX(), target.getY() - 1));
            return makeMove();
        } else if (targetNeighbours.getWest() == NeighbourState.ALLY && !visited[target.getX() - 1][target.getY()]) {
            visited[target.getX() - 1][target.getY()] = true;
            target = CoordinatesFactory.createCoordinates(target.getX() - 1, target.getY());
            historyOfTargets.add(CoordinatesFactory.createCoordinates(target.getX() - 1, target.getY()));
            return makeMove();
        }

        return resetAlgorithm();
    }

    @Override
    public void incrementTriesCounter() {
        consecativeTriesCounter++;
    }

    @Override
    public void resetTriesCounter() {
        consecativeTriesCounter = 0;
    }

    public Coordinates findRandomEnemyStone() {
        List<Coordinates> coordinatesList = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Stone stone = BoardManager.getStone(i, j);
                if (stone != null && stone.getStoneColor() != botColor) {
                    coordinatesList.add(CoordinatesFactory.createCoordinates(i,j));
                }
            }
        }

        if (coordinatesList.size() == 0) {
            // if no enemy stones have been found return (-1,-1)
            return CoordinatesFactory.createCoordinates(-1, -1);
        }
        return coordinatesList.get(random.nextInt(coordinatesList.size()));
    }

    public Coordinates findRandomEmptyPlace() {
        List<Coordinates> coordinatesList = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Stone stone = BoardManager.getStone(i, j);
                if (stone == null) {
                    coordinatesList.add(CoordinatesFactory.createCoordinates(i, j));
                }
            }
        }

        if (coordinatesList.size() == 0) {
            // if no empty place has been found return (-1,-1)
            return CoordinatesFactory.createCoordinates(-1, -1);
        }
        return coordinatesList.get(random.nextInt(coordinatesList.size()));
    }

    public void resetVisited() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                visited[i][j] = false;
            }
        }
    }

    public Coordinates resetAlgorithm() {
        target = findRandomEnemyStone();
        if (target.equals(CoordinatesFactory.createCoordinates(-1, -1))) {
            return findRandomEmptyPlace();
        }
        hasTarget = true;
        resetVisited();
        visited[target.getX()][target.getY()] = true;
        incrementTriesCounter();
        historyOfTargets = new ArrayList<>();
        historyOfTargets.add(target);
        return makeMove();
    }

    public boolean didTargetRepeat(Coordinates currentTarget) {
        return Collections.frequency(historyOfTargets, currentTarget) >= 2;
    }
}
