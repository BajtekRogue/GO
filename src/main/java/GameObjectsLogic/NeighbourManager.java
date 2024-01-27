package GameObjectsLogic;

import GameObjects.*;


public class NeighbourManager {
    private static Board board;
    private static ArrayOfNeighbours arrayOfNeighbours;

    public NeighbourManager(Board board){
        NeighbourManager.board = board;
        arrayOfNeighbours = new ArrayOfNeighbours(board.getBOARD_SIZE());
    }

    public static ArrayOfNeighbours getArrayOfNeighbours() {
        return arrayOfNeighbours;
    }

    public static void addNeighbours(int x, int y) throws ArrayIndexOutOfBoundsException{
        Stone stone = board.getTile(x, y); //make sure it's not null
        if(stone == null)
            return;
        NeighbourState northState;
        NeighbourState eastState;
        NeighbourState southState;
        NeighbourState westState;
        Stone northStone;
        Stone eastStone;
        Stone southStone;
        Stone westStone;

        try{
            northStone = board.getTile(x, y + 1);
            if(northStone == null)
                northState = NeighbourState.EMPTY;
            else if(northStone.getStoneColor() == stone.getStoneColor())
                northState = NeighbourState.ALLY;
            else
                northState = NeighbourState.ENEMY;
        }catch (ArrayIndexOutOfBoundsException e){
            northState = NeighbourState.WALL;
        }
        try{
            eastStone = board.getTile(x + 1, y);
            if(eastStone == null)
                eastState = NeighbourState.EMPTY;
            else if(eastStone.getStoneColor() == stone.getStoneColor())
                eastState = NeighbourState.ALLY;
            else
                eastState = NeighbourState.ENEMY;
        }catch (ArrayIndexOutOfBoundsException e){
            eastState = NeighbourState.WALL;
        }
        try{
            southStone = board.getTile(x, y - 1);
            if(southStone == null)
                southState = NeighbourState.EMPTY;
            else if(southStone.getStoneColor() == stone.getStoneColor())
                southState = NeighbourState.ALLY;
            else
                southState = NeighbourState.ENEMY;
        }catch (ArrayIndexOutOfBoundsException e){
            southState = NeighbourState.WALL;
        }
        try{
            westStone = board.getTile(x - 1, y );
            if(westStone == null)
                westState = NeighbourState.EMPTY;
            else if(westStone.getStoneColor() == stone.getStoneColor())
                westState = NeighbourState.ALLY;
            else
                westState = NeighbourState.ENEMY;
        }catch (ArrayIndexOutOfBoundsException e){
            westState = NeighbourState.WALL;
        }

        StoneNeighbours stoneNeighbours = new StoneNeighbours(northState, eastState, southState, westState);
        arrayOfNeighbours.setNeighbours(x, y, stoneNeighbours);

    }

    public static void updateNeighbours(int x, int y){
        try{
            addNeighbours(x, y + 1);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            addNeighbours(x + 1, y);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            addNeighbours(x, y - 1);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            addNeighbours(x - 1, y);
        }catch (ArrayIndexOutOfBoundsException ignored){}

    }

    public static void updateAllNeighbours(){
        for(int i = 0; i < arrayOfNeighbours.getArraySize(); i++){
            for(int j = 0; j < arrayOfNeighbours.getArraySize(); j++){
                updateNeighbours(i, j);
            }
        }

    }

}
