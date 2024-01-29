package ClientCommands;

import GameObjects.StoneColor;
import Server.Client;

public class KOCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.getGoGUI().showAlert("Cannot commit KO");
    }

}
