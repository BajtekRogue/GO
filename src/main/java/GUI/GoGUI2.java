package GUI;

import GameObjects.Coordinates;
import GameObjects.Stone;
import GameObjects.StoneColor;
import GameObjectsLogic.*;
import MyExceptions.KOException;
import MyExceptions.OccupiedTileException;
import MyExceptions.SuicideException;
import Server.GameMaster;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class GoGUI2 extends Application {

    private static final int BOARD_SIZE = 13;
    private static final int TILE_SIZE = 40;
    private static final double STONE_SIZE = 15;
    private static final double DOT_SIZE = 5;
    private int WHITE_POINTS = 0;
    private int BLACK_POINTS = 0;
    private int consecutivePasses = 0;
    private Label turnLabel;
    private Label pointsLabel;


    private StoneColor currentPlayer;
    private Canvas canvas;
    private Button[][] buttons;
    public GoGUI2(StoneColor whichPlayer) {
        // gameMaster creates all managers to avoid high-coupling
        this.currentPlayer = whichPlayer;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Go Game");
        primaryStage.setResizable(false);
        Pane root = new Pane();
        root.setPadding(new Insets(10));

        // Board
        canvas = new Canvas((BOARD_SIZE) * TILE_SIZE, (BOARD_SIZE + 1) * TILE_SIZE);
        root.getChildren().add(canvas);

        // Separator Line
        Pane separatorLine = new Pane();
        separatorLine.setLayoutX((BOARD_SIZE) * TILE_SIZE-3);
        separatorLine.setMinWidth(3);
        separatorLine.setMinHeight(1000);
        separatorLine.setStyle("-fx-background-color: BLACK;");

        // Right side information
        Pane infoPane = new Pane();
        infoPane.setLayoutX((BOARD_SIZE) * TILE_SIZE);
        infoPane.setMinWidth(300);
        infoPane.setMinHeight(1000);
        infoPane.setStyle("-fx-background-color: rgb(196,161,118);");

        // Player Turn Label
        turnLabel = new Label("Player Turn: BLACK");
        turnLabel.setLayoutY(200);
        turnLabel.setLayoutX(75);
        turnLabel.setStyle("-fx-font-size: 50;");
        turnLabel.setStyle("-fx-font-weight: bold;");
        turnLabel.setTextFill(Color.BLACK);

        pointsLabel = new Label("Black Points: 0   White Points: 0");
        pointsLabel.setLayoutY(230);
        pointsLabel.setLayoutX(75);
        pointsLabel.setStyle("-fx-font-size: 40;");
        pointsLabel.setStyle("-fx-font-weight: bold;");
        pointsLabel.setTextFill(Color.BLACK);


        // Pass Button
        Button passButton = new Button("Pass Turn");
        passButton.setLayoutY(260);
        passButton.setLayoutX(75);
        passButton.setOnAction(e -> handlePassButtonClick());

        // FF Button
        Button surrenderButton = new Button("Forfeit");
        surrenderButton.setLayoutY(290);
        surrenderButton.setLayoutX(75);
        surrenderButton.setOnAction(e -> handleSurrenderButtonClick());

        infoPane.getChildren().addAll(turnLabel, pointsLabel, passButton, surrenderButton);
        root.getChildren().addAll(separatorLine, infoPane);

        // Buttons
        buttons = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int y = BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Button button = createButton(x, y);
                buttons[x][y] = button;
                root.getChildren().add(button);
            }
        }

        drawGoBoard();

        Scene scene = new Scene(root, (BOARD_SIZE + 1) * TILE_SIZE + 220, BOARD_SIZE * TILE_SIZE);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    private void updateInfo() {
        // Update player turn label
        turnLabel.setText("Player Turn: " + currentPlayer.toString());
        turnLabel.setTextFill((currentPlayer == StoneColor.BLACK) ? Color.BLACK : Color.WHITE);

        // Update points label
        pointsLabel.setText("Black Points: " + BLACK_POINTS + "   White Points: " + WHITE_POINTS);
    }

    private Button createButton(int x, int y) {
        Button button = new Button();
        button.setMinSize(STONE_SIZE * 2, STONE_SIZE * 2);
        button.setMaxSize(STONE_SIZE * 2, STONE_SIZE * 2);
        // when the program starts the board is empty
        button.setStyle("-fx-background-color: transparent;");
        // + 20 to add margins
        button.setLayoutX(x * TILE_SIZE - STONE_SIZE + 20);
        // + 20 to add margins
        button.setLayoutY(y * TILE_SIZE - STONE_SIZE + 20);
        button.setOnAction(e -> handleButtonClick(x, y));
        // Add highlighting effect on mouse enter for empty buttons
        button.setOnMouseEntered(event -> {
            // add highlighting only if move is possible due to turn
            if (BoardManager.getStone(x, y) == null) {
                StoneColor stoneColor = currentPlayer;
                Color highlightColor = (stoneColor == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                highlightColor = highlightColor.deriveColor(0, 1, 1, 0.5);
                button.setStyle("-fx-background-color: " + highlightColor.toString().replace("0x", "#") + ";");
            }
        });

        // Remove highlighting effect on mouse exit
        button.setOnMouseExited(event -> {
            Stone stone = BoardManager.getStone(x, y);
            if (stone != null) {
                Color stoneColor = (stone.getStoneColor() == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                button.setStyle("-fx-background-color: " + stoneColor.toString().replace("0x", "#") + ";");
            } else {
                button.setStyle("-fx-background-color: transparent;");
            }
        });
        return button;
    }

    private void handleButtonClick(int x, int y){
        //send x,y to the server
    }
//    private void handleButtonClick(int x, int y) {
//        try {
//            BoardManager.addStone(x, y, new Stone(currentPlayer));
//            NeighbourManager.addNeighbours(x, y);
//            NeighbourManager.updateNeighbours(x, y);
//            List<Coordinates> capturedStones = CaptureManager.checkForCapture(x, y);
//
//            // if KO then don't capture stones
//            ExceptionManager.checkForKO(x, y);
//            int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);
//            if (numberOfCapturedStones > 0) {
//                if(currentPlayer == StoneColor.BLACK){
//                    BLACK_POINTS += numberOfCapturedStones;
//                } else {
//                    WHITE_POINTS += numberOfCapturedStones;
//                }
//            }
//            if (numberOfCapturedStones == 0) {
//                ExceptionManager.checkForSuicide(x, y);
//            }
//
//            if(numberOfCapturedStones == 1)
//                ExceptionManager.setKO_coordinates(capturedStones.get(0));
//            else
//                ExceptionManager.restKO_coordinates();
//
//            updateStones();
//            switchPlayer();
//            updateInfo();
//            consecutivePasses = 0;
//        } catch (OccupiedTileException | SuicideException | KOException ex) {
//            showAlert(ex.getMessage());
//        }
//    }
    private void handleSurrenderButtonClick() {
        //send FF to the server
//        StoneColor winner = (currentPlayer == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
//        showWinnerDialog(winner);
    }
    private void handlePassButtonClick() {
        //send pass to the server
//        switchPlayer();
//        updateInfo();
//        consecutivePasses++;
//        if (consecutivePasses >= 2) {
//            endGame();
//        }
    }

    private void showWinnerDialog(StoneColor winner) {
        String message =  winner.toString() + " won the game!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();

        //send those to the server
        // After showing the result we ask the player for next game
        if (askForNewGame()) {
            // Yes - we reset the game
            resetGame();
        } else {
            // No - we close the app
            System.exit(0);
        }
    }
    private void showDrawDialog() {
        String message =  "Draw!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();

        //send it to the server
        // After showing the result we ask the player for next game
        if (askForNewGame()) {
            // Yes - we reset the game
            resetGame();
        } else {
            // No - we close the app
            System.exit(0);
        }
    }

    //use server
//    private void endGame() {
//        if(BLACK_POINTS>WHITE_POINTS) {
//            showWinnerDialog(StoneColor.BLACK);
//        } else if (WHITE_POINTS>BLACK_POINTS) {
//            showWinnerDialog(StoneColor.WHITE);
//        } else{
//            showDrawDialog();
//        }
//    }

    private boolean askForNewGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Play again?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
        //send to the server
    }

    //move it to the server/game master
    private void resetGame() {
        // Reset points
        BLACK_POINTS = 0;
        WHITE_POINTS = 0;

        // Reset board
        BoardManager.resetBoard();
        updateStones();

        // Reset player
        currentPlayer = StoneColor.BLACK;
        // Reset consecutive passes
        consecutivePasses = 0;

        // Update
        updateInfo();
    }

    private void drawGoBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BURLYWOOD);
        // Filling the background
        gc.fillRect(0, 0, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        // Creating the grid
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            for (int j = 0; j < BOARD_SIZE - 1; j++) {
                // + 20 to add margins
                double x = j * TILE_SIZE + 20;
                // + 20 to add margins
                double y = i * TILE_SIZE + 20;
                gc.strokeRect(x, y, TILE_SIZE, TILE_SIZE);

                if (i > 0) {
                    gc.strokeLine(x, y, x + TILE_SIZE, y);
                }

                if (j > 0) {
                    gc.strokeLine(x, y, x, y + TILE_SIZE);
                }
            }
        }
        //Decorative dots
        // 19x19
        gc.setFill(Color.BLACK);
        if (BOARD_SIZE == 19) {
            for (int i = 3; i < BOARD_SIZE - 1; i += 6) {
                for (int j = 3; j < BOARD_SIZE - 1; j += 6) {
                    double x = j * TILE_SIZE - DOT_SIZE / 2 + 20;
                    double y = i * TILE_SIZE - DOT_SIZE / 2 + 20;
                    gc.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
            }
        } else {
            // 9x9 and 13x13
            for (int i = 2; i < BOARD_SIZE - 1; i += 4) {
                for (int j = 2; j < BOARD_SIZE - 1; j += 4) {
                    double x = j * TILE_SIZE - DOT_SIZE / 2 + 20;
                    double y = i * TILE_SIZE - DOT_SIZE / 2 + 20;
                    gc.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
            }
        }

    }

    private void updateStones() {
        for (int y = BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Stone stone = BoardManager.getStone(x, y);
                if (stone != null) {
                    Color stoneColor = (stone.getStoneColor() == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                    buttons[x][y].setStyle("-fx-background-color: " + stoneColor.toString().replace("0x", "#") + ";");
                } else {
                    // if the stone is null then there is no stone placed at (x,y) so button is transparent
                    buttons[x][y].setStyle("-fx-background-color: transparent;");
                }
            }
        }
    }

//    private void switchPlayer() {
//        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
//    }
//
//    public static void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
//        alert.showAndWait();
//    }
}
