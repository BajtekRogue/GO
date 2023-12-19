package Main;

import GameObjects.ArrayOfNeighbours;
import GameObjects.Board;
import GameObjects.Stone;
import GameObjects.StoneColor;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.NeighbourManager;
import MyExceptions.OccupiedTileException;
import MyExceptions.SuicideException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GoGUI extends Application {

    private static final int BOARD_SIZE = 9;
    private static final int TILE_SIZE = 40;
    private static final double STONE_SIZE = 15;
    private static final double DOT_SIZE = 5;


    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;
    private StoneColor currentPlayer;
    private Canvas canvas;
    private Button[][] buttons;

    public GoGUI() {
        Board board = new Board(BOARD_SIZE);
        this.boardManager = new BoardManager(board);
        ArrayOfNeighbours arrayOfNeighbours = new ArrayOfNeighbours(BOARD_SIZE);
        this.neighbourManager = new NeighbourManager(board, arrayOfNeighbours);
        this.captureManager = new CaptureManager(board, boardManager, neighbourManager);
        this.currentPlayer = StoneColor.BLACK;
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

        // Buttons
        buttons = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int y = BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Button button = createButton(x, y);
                buttons[x][y] = button;
                root.getChildren().add(button);
            }
        }

//        // Centering the board
//        double boardOffsetX = (canvas.getWidth() - BOARD_SIZE * TILE_SIZE) / 2 + 50; // Przesunięcie o 50 pikseli w prawo
//        double boardOffsetY = (canvas.getHeight() - BOARD_SIZE * TILE_SIZE) / 2 + 50; // Przesunięcie o 50 pikseli w dół
//        canvas.setLayoutX(boardOffsetX);
//        canvas.setLayoutY(boardOffsetY);
//
//        // Centering the buttons
//        for (int y = 0; y < BOARD_SIZE; y++) {
//            for (int x = 0; x < BOARD_SIZE; x++) {
//                buttons[x][y].setLayoutX(x * TILE_SIZE - DOT_SIZE + boardOffsetX);
//                buttons[x][y].setLayoutY(y * TILE_SIZE - DOT_SIZE + boardOffsetY);
//            }
//        }

        drawGoBoard();

        Scene scene = new Scene(root, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);
        primaryStage.setScene(scene);

        primaryStage.show();
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
        return button;
    }

    private void handleButtonClick(int x, int y) {
        try {
            boardManager.addStone(x, y, new Stone(currentPlayer));
            neighbourManager.addNeighbours(x, y);
            neighbourManager.updateNeighbours(x, y);
            int capturedStones = captureManager.checkForCapture(x, y);

//            if (capturedStones > 0 && currentPlayer == StoneColor.BLACK) {
//                // Black player gets points for captures
//                // Add your logic here if needed
//            }

            if (capturedStones == 0) {
                captureManager.checkForSuicide(x, y);
            }
            updateStones();
            switchPlayer();
        } catch (OccupiedTileException | SuicideException ex) {
            showAlert(ex.getMessage());
        }
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
                Stone stone = boardManager.getStone(x, y);
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

    private void switchPlayer() {
        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}