package Server;

import Factories.CoordinatesFactory;
import GameObjects.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageDecoder {
    public static Coordinates stonesToBeAddedFromStringToCoordinates(String message) {

        String[] tokens = message.split("\\s+");
        int xIndex = -1;
        int yIndex = -1;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("OK")) {
                if (i + 2 < tokens.length) {
                    xIndex = i + 2;
                    yIndex = i + 3;
                    break;
                }
            }
        }

        if (xIndex < tokens.length && yIndex < tokens.length) {
            try {
                int x = Integer.parseInt(tokens[xIndex]);
                int y = Integer.parseInt(tokens[yIndex]);
                return CoordinatesFactory.createCoordinates(x, y);
            } catch (NumberFormatException ignored) {
            }
        }

        return CoordinatesFactory.createCoordinates(-1, -1);
    }

    public static List<Coordinates> stonesToBeRemovedFromStringToCoordinates(String message) {
        List<Coordinates> coordinatesList = new ArrayList<>();
        String[] tokens = message.split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("REMOVED")) {
                int remainingTokens = tokens.length - i - 1;
                if (remainingTokens >= 2 && remainingTokens % 2 == 0) {
                    for (int j = 0; j < remainingTokens; j += 2) {
                        try {
                            int x = Integer.parseInt(tokens[i + j + 1]);
                            int y = Integer.parseInt(tokens[i + j + 2]);
                            coordinatesList.add(CoordinatesFactory.createCoordinates(x, y));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        return coordinatesList;
    }

    public static int extractBlackPoints(String message) {

        String[] tokens = message.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("Black") && i + 2 < tokens.length) {
                try {
                    return Integer.parseInt(tokens[i + 2]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public static int extractWhitePoints(String message) {
        String[] tokens = message.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("White") && i + 2 < tokens.length) {
                try {
                    return Integer.parseInt(tokens[i + 2]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
    public static String removeFirstWord(String input) {
        return input.trim().replaceFirst("\\S+\\s*", "");
    }
    public static ArrayList<String> parseGames(String input) {
        ArrayList<String> gamesList = new ArrayList<>();

        if (input.startsWith("GAMES: ")) {
            String gamesString = input.substring("GAMES: ".length());

            String[] gamesArray = gamesString.split(" ");

            gamesList.addAll(Arrays.asList(gamesArray));
        }

        return gamesList;
    }

    public static String extractSelectedGame(String input) {
        String[] parts = input.split(" ");
        if (parts.length >= 2) {
            return parts[1];
        }

        return null;
    }

    public static int extractMoveNumber(String input) {
        String[] parts = input.split(" ");
        if (parts.length >= 3) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }
}
