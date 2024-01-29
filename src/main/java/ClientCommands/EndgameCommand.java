package ClientCommands;

import Server.Client;

public class EndgameCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.getGoGUI().endGame(message);
    }
}
