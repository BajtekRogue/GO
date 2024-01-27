package Server;

import GameObjects.StoneColor;
import MyExceptions.KOException;
import MyExceptions.OccupiedTileException;
import MyExceptions.SuicideException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Server {

    private static final int PORT = 1161;
    private static ServerSocket serverSocket;
    private static List<Player> players = new ArrayList<>();
    private static int nextPlayerId = 1;
    private static int wantsHuman = 0;
    private static boolean humanGame = false;
    static GameMaster gameMaster;

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running. Waiting for players to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();  // Wait for a new connection
                System.out.println("New player connected!");

                // Assign the player an ID
                int playerId = nextPlayerId++;
                System.out.println("Assigned ID " + playerId + " to the player.");

                // Create a Player object and add it to the list
                Player newPlayer = new Player(playerId, clientSocket);
                players.add(newPlayer);

                if (!newPlayer.areInitialMessagesSent()) {
                    sendMessage(newPlayer.getOutputStream(), "You have successfully connected to the server as player" + playerId);
                    sendMessage(newPlayer.getOutputStream(), "PlayerID: " + playerId);
                    newPlayer.setInitialMessagesSent();  // Mark initial messages as sent
                }

                // Start a new thread to handle messages from the current player
                new Thread(() -> {
                    try {
                        while (true) {
                            String message = receiveMessage(newPlayer.getInputStream());

                            System.out.println("Received message from Player " + playerId + ": " + message);

                            if (Objects.equals(message, "Human")) {
                                wantsHuman++;
                            }

                            // Check if both players want to play with a human and start human game if it hasn't started yet
                            if (wantsHuman == 2 && !humanGame) {
                                broadcastMessageToAll("GameAccepted");
                                startHumanGame();
                            }
                            if(humanGame){
                                String output = gameMaster.makeAction(message);
                                System.out.println(output);
                                sendMessage(newPlayer.getOutputStream(),output);
                            }
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        // Handle exceptions (e.g., player disconnects)
                        System.out.println("Player " + playerId + " disconnected.");
                        players.remove(newPlayer);
                    } catch (SuicideException | KOException | OccupiedTileException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Error in the server: " + e.getMessage());
        }
    }

    private static void broadcastMessage(Player sender, String message) {
        for (Player player : players) {
            try {
                if (player != sender) {
                    sendMessage(player.getOutputStream(), "Player " + sender.getPlayerID() + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Error broadcasting message to Player " + player.getPlayerID());
            }
        }
    }

    private static void broadcastMessageToAll(String message) {
        for (Player player : players) {
            try {
                sendMessage(player.getOutputStream(), message);
            } catch (IOException e) {
                System.out.println("Error broadcasting message to Player " + player.getPlayerID());
            }
        }
    }

    private static void sendMessage(ObjectOutputStream out, String message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    private static String receiveMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String message = (String) in.readObject();
        return message;
    }

    private static void startHumanGame() throws IOException {
        // Implement logic to start the game with human players
        // You may need to create a Game instance or invoke appropriate methods
        System.out.println("Game with humans is starting...");
        humanGame = true;
        gameMaster = new GameMaster(13);
        // Choose a random color for the players
        StoneColor firstPlayerColor = (new Random().nextBoolean()) ? StoneColor.BLACK : StoneColor.WHITE;
        StoneColor secondPlayerColor = (firstPlayerColor == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;

        // Assign  colors to player
        players.get(0).setStoneColor(firstPlayerColor);
        players.get(1).setStoneColor(secondPlayerColor);

        // Inform the players about their colors
        sendMessage(players.get(0).getOutputStream(), firstPlayerColor.toString());
        sendMessage(players.get(1).getOutputStream(), secondPlayerColor.toString());

    }
}
