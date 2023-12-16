package Main;

import GameObjects.Board;
import GameObjectsLogic.GameMaster;

public class App
{
    public static void main( String[] args )
    {
        GameMaster gameMaster = new GameMaster();
        gameMaster.startGame(9);
    }
}
