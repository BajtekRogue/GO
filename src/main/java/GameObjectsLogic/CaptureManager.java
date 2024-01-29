package GameObjectsLogic;

import Factories.CoordinatesFactory;
import GameObjects.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CaptureManager {

    private static Board board;

    public CaptureManager(Board board){
        CaptureManager.board = board;
    }

    public static int removeCapturedStones(List<Coordinates> stonesToBeRemoved){

        for (Coordinates coordinates: stonesToBeRemoved)
            BoardManager.removeStone(coordinates.getX(), coordinates.getY());

        NeighbourManager.updateAllNeighbours();
        return stonesToBeRemoved.size();
    }
    public static List<Coordinates> checkForCapture(int x, int y){
        List<Coordinates> stonesToBeCaptured = new ArrayList<>();
        List<Coordinates> stonesToBeCapturedNorth;
        List<Coordinates> stonesToBeCapturedEast;
        List<Coordinates> stonesToBeCapturedSouth;
        List<Coordinates> stonesToBeCapturedWest;
        StoneNeighbours lastPlacedStoneNeighbours = NeighbourManager.getArrayOfNeighbours().getNeighbours(x, y);

        if(lastPlacedStoneNeighbours.getNorth() == NeighbourState.ENEMY){
            stonesToBeCapturedNorth = lookForChain(x, y + 1);
            if(stonesToBeCapturedNorth != null)
                stonesToBeCaptured.addAll(stonesToBeCapturedNorth);

        }
        if(lastPlacedStoneNeighbours.getEast() == NeighbourState.ENEMY){
            stonesToBeCapturedEast = lookForChain(x + 1, y);
            if(stonesToBeCapturedEast != null)
                stonesToBeCaptured.addAll(stonesToBeCapturedEast);

        }
        if(lastPlacedStoneNeighbours.getSouth() == NeighbourState.ENEMY){
            stonesToBeCapturedSouth = lookForChain(x, y - 1);
            if(stonesToBeCapturedSouth != null)
                stonesToBeCaptured.addAll(stonesToBeCapturedSouth);
        }
        if(lastPlacedStoneNeighbours.getWest() == NeighbourState.ENEMY){
            stonesToBeCapturedWest = lookForChain(x - 1, y);
            if(stonesToBeCapturedWest != null)
                stonesToBeCaptured.addAll(stonesToBeCapturedWest);
        }

        HashSet<Coordinates> SetOfStonesToBeRemoved = new HashSet<>(stonesToBeCaptured);
        return new ArrayList<>(SetOfStonesToBeRemoved);
    }

    public static List<Coordinates> lookForChain(int x, int y) {
        List<Coordinates> stonesToBeCaptured = new ArrayList<>();
        boolean[][] visited = new boolean[board.getBoardSize()][board.getBoardSize()];

        // Perform DFS to search for stones to be captured
        if(depthFirstSearch(x, y, stonesToBeCaptured, visited))
            return null;

        return stonesToBeCaptured;
    }

    private static boolean depthFirstSearch(int x, int y, List<Coordinates> stonesToBeCaptured, boolean[][] visited) {
        // Check if the current position is within bounds and not visited
        if (x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || visited[x][y])
            return false;

        visited[x][y] = true;
        StoneNeighbours currentNeighbours = NeighbourManager.getArrayOfNeighbours().getNeighbours(x, y);
        if (currentNeighbours == null)
            return false;

        // Check if the current stone has breaths and connections
        if (currentNeighbours.getNumberOfBreaths() > 0)
            return true; // Stop the search
        if(currentNeighbours.getNumberOfConnections() == 0){
            stonesToBeCaptured.add(CoordinatesFactory.createCoordinates(x, y));
            return false;
        }
        // Check each direction
        if (currentNeighbours.getNorth() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(CoordinatesFactory.createCoordinates(x, y));
            if (depthFirstSearch(x, y + 1, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getEast() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(CoordinatesFactory.createCoordinates(x, y));
            if (depthFirstSearch(x + 1, y, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getSouth() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(CoordinatesFactory.createCoordinates(x, y));
            if (depthFirstSearch(x, y - 1, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getWest() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(CoordinatesFactory.createCoordinates(x, y));
            return depthFirstSearch(x - 1, y, stonesToBeCaptured, visited);
        }

        // If this point is reached, the current stone is not part of the capturing chain
        return false;
    }



}
