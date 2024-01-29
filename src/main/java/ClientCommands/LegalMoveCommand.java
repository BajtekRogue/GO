package ClientCommands;

import Server.Client;

public class LegalMoveCommand implements Command{
    @Override
    public void execute(Client client, String message) {
        client.switchTurn();
        client.getGoGUI().refreshGUIAfterSuccessfulMove(message);
    }
}
