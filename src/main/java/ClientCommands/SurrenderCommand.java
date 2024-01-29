package ClientCommands;

import Server.Client;

public class SurrenderCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.getGoGUI().showSurrenderDialog();
    }
}
