package Server;

import GameObjects.StoneColor;


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
    private static final List<Player> players = new ArrayList<>();
    private static int nextPlayerId = 1;
    private static int wantsHuman = 0;
    private static boolean humanGame = false;
    private static GameMaster gameMaster;

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running. Waiting for players to connect...");

            while (!humanGame) {
                handleClient();
            }

        } catch (IOException e) {
            System.out.println("Error in the server: " + e.getMessage());
        }
    }

    private static void handleClient() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New player connected!");

        int playerId = nextPlayerId++;
        System.out.println("Assigned ID " + playerId + " to the player.");

        Player newPlayer = new Player(playerId, clientSocket);
        players.add(newPlayer);

        if (!newPlayer.areInitialMessagesSent()) {
            sendMessage(newPlayer.getOutputStream(), "You have successfully connected to the server as player" + playerId);
            sendMessage(newPlayer.getOutputStream(), "PlayerID: " + playerId);
            newPlayer.setInitialMessagesSent();
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
                        broadcastMessage(newPlayer, output);

                        if(output.contains("ENDGAME"))
                            gameMaster.endTheGame();
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Player " + playerId + " disconnected.");
                players.remove(newPlayer);
            }
        }).start();
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
        return (String) in.readObject();
    }

    private static void startHumanGame() throws IOException {

        System.out.println("Game with humans is starting...");
        humanGame = true;
        serverSocket.close();
        gameMaster = new GameMaster(13);

        // Choose a random color for the players
        StoneColor firstPlayerColor = (new Random().nextBoolean()) ? StoneColor.BLACK : StoneColor.WHITE;
        StoneColor secondPlayerColor = (firstPlayerColor == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
        players.get(0).setStoneColor(firstPlayerColor);
        players.get(1).setStoneColor(secondPlayerColor);
        sendMessage(players.get(0).getOutputStream(), firstPlayerColor.toString());
        sendMessage(players.get(1).getOutputStream(), secondPlayerColor.toString());

    }
}
