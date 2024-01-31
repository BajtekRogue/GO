package Server;

import GameObjects.StoneColor;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static final int PORT = 1151;
    private static ServerSocket serverSocket;
    private static final List<Player> players = new ArrayList<>();
    private static int nextPlayerId = 1;
    private static int wantsHuman = 0;
    private static boolean humanGame = false;
    private static boolean botGame = false;
    private static GameMaster gameMaster;
    private static int boardSize = 0;
    private static String gameName;
    private static boolean dbGame;

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

        } catch (IOException | InterruptedException e) {
            System.out.println("Error in the server: " + e.getMessage());
        }
    }

    private static void handleClient() throws IOException, InterruptedException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New player connected!");

        int playerId = nextPlayerId++;
        System.out.println("Assigned ID " + playerId + " to the player.");

        Player newPlayer = new Player(playerId, clientSocket);
        players.add(newPlayer);

        if (!newPlayer.areInitialMessagesSent()) {
            sendMessage(newPlayer.getOutputStream(), "You have successfully connected to the server as player" + playerId);
            if(playerId == 1){
                sendMessage(newPlayer.getOutputStream(), "PlayerID: " + playerId + " " + boardSize);
            }
            else{
                while( boardSize == 0){
                    Thread.sleep(1);
                }
                sendMessage(newPlayer.getOutputStream(), "PlayerID: " + playerId + " " + boardSize);
            }

            newPlayer.setInitialMessagesSent();
        }
        // Start a new thread to handle messages from the current player
        new Thread(() -> handlePlayerMessages(newPlayer, playerId)).start();

    }

    private static void handlePlayerMessages(Player player, int playerId) {

        try {
            while (true) {
                String message = receiveMessage(player.getInputStream());

                System.out.println("Received message from Player " + playerId + ": " + message);

                if (message.contains("Human")) {
                    if(playerId==1) {
                        boardSize = MessageDecoder.getBoardSize(message);
                    }
                    wantsHuman++;

                }

                if (message.contains("Bot")) {
                    boardSize = MessageDecoder.getBoardSize(message);
                    broadcastMessageToAll("GameAccepted");
                    startBotGame();
                }
                if (Objects.equals(message, "DBGames")) {
                    startDBGames(player, message);
                }

                // Check if both players want to play with a human and start human game if it hasn't started yet
                if (wantsHuman == 2 && !humanGame) {
                    broadcastMessageToAll("GameAccepted");
                    startHumanGame();
                }

                if (humanGame) {
                    handleHumanGame(player, message);
                }

                if (botGame) {
                    handleBotGame(player, message);
                }
                if (dbGame) {
                    handleDBGame(player, message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + playerId + " disconnected.");
            broadcastMessageToAll("Player " + playerId + " disconnected.");
            players.remove(player);
            if(players.isEmpty()){
                closeServer();
            }
        }
    }

    private static void handleDBGame(Player player, String message) throws IOException {
        if (message.contains("LOAD")) {
            String selectedGame = MessageDecoder.extractSelectedGame(message);
            int moveNumber = MessageDecoder.extractMoveNumber(message);
            sendMessage(player.getOutputStream(), DatabaseManager.getOneMove(selectedGame, moveNumber));
        }
        if (message.contains("DLOAD")) {
            String selectedGame = MessageDecoder.extractSelectedGame(message);
            int moveNumber = MessageDecoder.extractMoveNumber(message);
            sendMessage(player.getOutputStream(), DatabaseManager.getNegatedOneMove(selectedGame, moveNumber));
        }
    }


    private static void handleHumanGame(Player player, String message) throws IOException, ClassNotFoundException {
        String output = gameMaster.makeAction(message);
        System.out.println(output);
        sendMessage(player.getOutputStream(), output);
        broadcastMessage(player, output);
        if (output.contains("MOVE") || output.contains("PASS") || output.contains("ENDGAME") || output.contains("SURRENDER")) {
            DatabaseManager.saveMove(gameName, player.getStoneColor().toString(), output);
        }
        // Close the server when the game finishes
        if (output.contains("ENDGAME") || output.contains("SURRENDER")) {
            closeServer();
        }
    }

    private static void handleBotGame(Player player, String message) throws IOException, ClassNotFoundException {
        String output = gameMaster.makeAction(message);
        System.out.println(output);
        sendMessage(player.getOutputStream(), output);
        if (output.contains("MOVE") || output.contains("PASS") || output.contains("ENDGAME") || output.contains("SURRENDER")) {
            DatabaseManager.saveMove(gameName, player.getStoneColor().toString(), output);
        }
        if (output.equals("Unknown message type") && player.getStoneColor() == StoneColor.BLACK) {
            sendMessage(player.getOutputStream(), output);
        } else if (output.equals("Unknown message type") && player.getStoneColor() == StoneColor.WHITE) {
            try {
                Thread.sleep(1000);
                System.out.println("Waiting for bot to make move");
            } catch (InterruptedException ignored) {
            }

            String botOutput = gameMaster.getBot().finalBotMove(gameMaster);
            DatabaseManager.saveMove(gameName, player.getStoneColor().toString(), botOutput);
            System.out.println("Bot move: " + botOutput);
            sendMessage(player.getOutputStream(), botOutput);
        } else {
            try {
                Thread.sleep(1000);
                System.out.println("Waiting for bot to make move");
            } catch (InterruptedException ignored) {
            }

            if (output.contains("OK") || output.contains("PASS")) {
                String botOutput = gameMaster.getBot().finalBotMove(gameMaster);
                DatabaseManager.saveMove(gameName, gameMaster.getBot().getBotColor().toString(), botOutput);
                System.out.println("Bot move: " + botOutput);
                sendMessage(player.getOutputStream(), botOutput);
            }
        }

        // Close the server when the game finishes
        if (output.contains("ENDGAME") || output.contains("SURRENDER")) {
            closeServer();
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
        return (String) in.readObject();
    }

    private static void startHumanGame() throws IOException {

        System.out.println("Game with humans is starting...");
        humanGame = true;
        gameMaster = new GameMaster(boardSize);
        DatabaseManager.initializeDatabase(boardSize);
        gameName = DatabaseManager.getCurrentGameName();
        System.out.println(gameName);
        // Choose a random color for the players
        StoneColor firstPlayerColor = (new Random().nextBoolean()) ? StoneColor.BLACK : StoneColor.WHITE;
        StoneColor secondPlayerColor = (firstPlayerColor == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
        players.get(0).setStoneColor(firstPlayerColor);
        players.get(1).setStoneColor(secondPlayerColor);
        sendMessage(players.get(0).getOutputStream(), firstPlayerColor.toString());
        sendMessage(players.get(1).getOutputStream(), secondPlayerColor.toString());

    }

    private static void startBotGame() throws IOException {

        System.out.println("Game with bot is starting...");
        botGame = true;
        gameMaster = new GameMaster(boardSize);
        DatabaseManager.initializeDatabase(boardSize);
        gameName = DatabaseManager.getCurrentGameName();
        System.out.println(gameName);
        // Choose a random color for the players
        StoneColor firstPlayerColor = (new Random().nextBoolean()) ? StoneColor.BLACK : StoneColor.WHITE;
        StoneColor secondPlayerColor = (firstPlayerColor == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
        players.get(0).setStoneColor(firstPlayerColor);
        sendMessage(players.get(0).getOutputStream(), firstPlayerColor.toString());
        gameMaster.setBot(secondPlayerColor, boardSize);

    }

    private static void startDBGames(Player player, String message) throws IOException {
        System.out.println("Somebody is viewing previous games...");
        dbGame = true;
        String allGames = DatabaseManager.getAllGames();
        sendMessage(player.getOutputStream(), "GAMES: " + allGames);
    }

    public static void closeServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            for (Player player : players) {
                if (player.getSocket() != null && !player.getSocket().isClosed()) {
                    player.getSocket().close();
                }
            }
            System.out.println("Server has been closed");

        } catch (IOException | ConcurrentModificationException e) {
            System.out.println("Error while disconnecting: ");
        }
    }
}
