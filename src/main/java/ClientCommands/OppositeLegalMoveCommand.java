package ClientCommands;

import Server.Client;

public class OppositeLegalMoveCommand implements Command {
    @Override
    public void execute(Client client, String message) {
        client.switchTurn();
        client.getGoGUI().refreshGUIAfterReverseArrow(message);
    }
}
