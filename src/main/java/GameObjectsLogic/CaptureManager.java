package GameObjectsLogic;

import GameObjects.Board;
import GameObjects.NeighbourState;
import GameObjects.StoneNeighbours;
import MyExceptions.SuicideException;


import java.util.ArrayList;
import java.util.List;

public class CaptureManager {

    private Board board;
    private BoardManager boardManager;
    private NeighbourManager neighbourManager;

    public CaptureManager(Board board, BoardManager boardManager, NeighbourManager neighbourManager){
        this.board = board;
        this.boardManager = boardManager;
        this.neighbourManager = neighbourManager;
    }

    public int removeCapturedStones(List<MyPair> stonesToBeRemoved) throws SuicideException {
        for (MyPair coordinates: stonesToBeRemoved){
            boardManager.removeStone(coordinates.getX(), coordinates.getY());
            System.out.println("Removed stone : (" + coordinates.getX() + "," + coordinates.getY() + ")");
        }
        neighbourManager.updateAllNeighbours();
        if(!stonesToBeRemoved.isEmpty()){
        try {
            // Sleep for 2 seconds (2000 milliseconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle the interruption if needed
            e.printStackTrace();
        }}
        return stonesToBeRemoved.size();

    }
    public int checkForCapture(int x, int y) throws SuicideException{
        List<MyPair> stonesToBeCaptured;
        StoneNeighbours lastPlacedStoneNeighbours = neighbourManager.getArrayOfNeighbours().getNeighbours(x, y);
        int totalRemovedStones = 0;

        if(lastPlacedStoneNeighbours.getNorth() == NeighbourState.ENEMY){
            stonesToBeCaptured = lookForChain(x, y + 1);
            if(stonesToBeCaptured != null)
                totalRemovedStones += removeCapturedStones(stonesToBeCaptured);

        }
        if(lastPlacedStoneNeighbours.getEast() == NeighbourState.ENEMY){
            stonesToBeCaptured = lookForChain(x + 1, y);
            if(stonesToBeCaptured != null)
                totalRemovedStones += removeCapturedStones(stonesToBeCaptured);

        }
        if(lastPlacedStoneNeighbours.getSouth() == NeighbourState.ENEMY){
            stonesToBeCaptured = lookForChain(x, y - 1);
            if(stonesToBeCaptured != null)
                totalRemovedStones += removeCapturedStones(stonesToBeCaptured);
        }
        if(lastPlacedStoneNeighbours.getWest() == NeighbourState.ENEMY){
            stonesToBeCaptured = lookForChain(x - 1, y);
            if(stonesToBeCaptured != null)
                totalRemovedStones += removeCapturedStones(stonesToBeCaptured);
        }
        return totalRemovedStones;
    }

    public List<MyPair> lookForChain(int x, int y) {
        List<MyPair> stonesToBeCaptured = new ArrayList<>();
        boolean[][] visited = new boolean[board.getBoardSize()][board.getBoardSize()];

        // Perform DFS to search for stones to be captured
        if(depthFirstSearch(x, y, stonesToBeCaptured, visited))
            return null;

        return stonesToBeCaptured;
    }

    private boolean depthFirstSearch(int x, int y, List<MyPair> stonesToBeCaptured, boolean[][] visited) {
        // Check if the current position is within bounds and not visited
        if (x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || visited[x][y])
            return false;

        visited[x][y] = true;
        StoneNeighbours currentNeighbours = neighbourManager.getArrayOfNeighbours().getNeighbours(x, y);
        if (currentNeighbours == null)
            return false;

        // Check if the current stone has breaths and connections
        if (currentNeighbours.getNumberOfBreaths() > 0)
            return true; // Stop the search
        if(currentNeighbours.getNumberOfConnections() == 0){
            stonesToBeCaptured.add(new MyPair(x, y));
            return false;
        }
        // Check each direction
        if (currentNeighbours.getNorth() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(new MyPair(x, y));
            if (depthFirstSearch(x, y + 1, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getEast() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(new MyPair(x, y));
            if (depthFirstSearch(x + 1, y, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getSouth() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(new MyPair(x, y));
            if (depthFirstSearch(x, y - 1, stonesToBeCaptured, visited))
                return true;
        }
        if (currentNeighbours.getWest() == NeighbourState.ALLY) {
            stonesToBeCaptured.add(new MyPair(x, y));
            if (depthFirstSearch(x - 1, y, stonesToBeCaptured, visited))
                return true;
        }

        // If this point is reached, the current stone is not part of the capturing chain
        return false;
    }

    public void checkForSuicide(int x, int y) throws SuicideException{
        List<MyPair> stonesToBeCaptured = lookForChain(x, y);
        if(stonesToBeCaptured != null){
            boardManager.removeStone(x, y);
            neighbourManager.updateNeighbours(x, y);
            throw new SuicideException("Cannot commit suicide!");
        }
    }

}
