package ClientCommands;

import Server.Client;

public class ListOfGamesCommand implements Command {
    @Override
    public void execute(Client client, String message) {
        client.setUpdatedGamesTrue();
        client.setAvailableGames(client.requestAvailableGames(message));
    }
}
