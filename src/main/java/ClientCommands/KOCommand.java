package ClientCommands;

import Server.Client;

public class KOCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.getGoGUI().showAlert("Cannot commit KO");
    }

}
