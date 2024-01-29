package ClientCommands;

import GameObjects.StoneColor;
import Server.Client;

public class WhiteCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.setPlayerColor(StoneColor.WHITE);
        client.setMyTurn(false);
        System.out.println("You are playing " + message);
        client.getGoGUI().setGUITitle(StoneColor.WHITE);
    }
}
