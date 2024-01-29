package ClientCommands;

import Server.Client;

public interface Command {
    void execute(Client client, String message);
}

