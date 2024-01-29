package ClientCommands;

import Server.Client;

public class GameAcceptedCommand implements Command {
    @Override
    public void execute(Client client, String message) {
        client.setPlayerReady();
        System.out.println("The other player also wants to play!");
    }
}