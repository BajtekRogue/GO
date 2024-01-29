package ClientCommands;

import Server.Client;

public class OccupiedCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.getGoGUI().showAlert("This tile is already occupied");
    }
}
