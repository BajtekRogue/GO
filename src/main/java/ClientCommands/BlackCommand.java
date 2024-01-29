package ClientCommands;

import GameObjects.StoneColor;
import Server.Client;

public class BlackCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.setPlayerColor(StoneColor.BLACK);
        client.setMyTurn(true);
        System.out.println("You are playing " + message);
        client.getGoGUI().toggleActivatePassButton();
        client.getGoGUI().toggleFFPassButton();
        client.getGoGUI().setGUITitle(StoneColor.BLACK);
    }
}
