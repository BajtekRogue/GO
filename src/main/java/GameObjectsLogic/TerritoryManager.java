package GameObjectsLogic;


import GameObjects.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TerritoryManager {

    private static Board board;

    public TerritoryManager(Board board) {
        TerritoryManager.board = board;
    }

    public static List<Coordinates> lookForTerritory(StoneColor playerColor) {
        List<Coordinates> totalTerritory = new ArrayList<>();

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                List<Coordinates> territory = new ArrayList<>();
                boolean[][] visited = new boolean[board.getBoardSize()][board.getBoardSize()];
                if (!depthFirstSearch(playerColor, i, j, territory, visited))
                    totalTerritory.addAll(territory);

            }
        }

        HashSet<Coordinates> setOfTerritory = new HashSet<>(totalTerritory);
        return new ArrayList<>(setOfTerritory);
    }

    private static boolean depthFirstSearch(StoneColor playerColor, int x, int y,
                                            List<Coordinates> territory, boolean[][] visited) {
        // Check if the current position is within bounds and not visited
        if (x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || visited[x][y])
            return false;

        visited[x][y] = true;
        Stone stone = board.getTile(x, y);

        if (stone == null)
            territory.add(new Coordinates(x, y));
        else
            return playerColor != stone.getStoneColor();




        // Check each direction
        return depthFirstSearch(playerColor, x, y + 1, territory, visited) ||
                depthFirstSearch(playerColor, x + 1, y, territory, visited) ||
                depthFirstSearch(playerColor, x, y - 1, territory, visited) ||
                depthFirstSearch(playerColor, x - 1, y, territory, visited);
    }
}


