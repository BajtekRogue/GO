package Server;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT, player_color TEXT, move TEXT)";
    private static String currentGameName;
    public static void initializeDatabase(int boardSize) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            currentGameName = getCurrentTableName(boardSize);
            String createTableQuery = String.format(CREATE_TABLE_QUERY, currentGameName);
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            System.out.println("SQL error");
        }
    }


    public static void saveMove(String gameName, String playerColor, String move) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            String insertMoveQuery = String.format("INSERT INTO %s (player_color, move) VALUES ('%s', '%s')", gameName, playerColor, move);
            statement.executeUpdate(insertMoveQuery);
        } catch (SQLException e) {
            System.out.println("SQL error");
        }
    }
    public static String getAllGames() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            String selectGamesQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_sequence'";
            ResultSet resultSet = statement.executeQuery(selectGamesQuery);

            StringBuilder gamesStringBuilder = new StringBuilder();
            while (resultSet.next()) {
                String gameName = resultSet.getString("name");
                gamesStringBuilder.append(gameName).append(" ");
            }

            return gamesStringBuilder.toString();
        } catch (SQLException e) {
            System.out.println("SQL error");
            return null;
        }
    }
//    public static ResultSet getAllMoves() {
//        try (Connection connection = DriverManager.getConnection(DB_URL);
//             Statement statement = connection.createStatement()) {
//            String tableName = getCurrentTableName();
//            String selectMovesQuery = "SELECT player_color, move FROM " + tableName + " ORDER BY id";
//            return statement.executeQuery(selectMovesQuery);
//        } catch (SQLException e) {
//            System.out.println("SQL error");
//            return null;
//        }
//    }
    public static String getOneMove(String gameName, int moveID) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            String selectMovesQuery = "SELECT move FROM " + gameName + " WHERE id LIKE " + moveID;
            ResultSet resultSet = statement.executeQuery(selectMovesQuery);

            if (resultSet.next()) {
                return resultSet.getString("move");
            } else {
                return null;  // No results
            }
        } catch (SQLException e) {
            System.out.println("SQL error");
            return null;
        }
    }
    public static String getCurrentGameName(){return currentGameName;}
    private static String getCurrentTableName(int boardSize) {
        return "game_" + dateFormatter.format(new Date()) + "_" + boardSize;
    }

    public static String getNegatedOneMove(String selectedGame, int moveNumber) {
        return "NOT " + getOneMove(selectedGame,moveNumber);
    }
}
