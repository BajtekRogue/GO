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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {

    private final BoardManager boardManager;
    private final NeighbourManager neighbourManager;
    private final CaptureManager captureManager;
    private BoardButton[][] buttons;
    private StoneColor currentPlayer;

    public GameGUI(BoardManager boardManager, NeighbourManager neighbourManager, CaptureManager captureManager) {
        this.boardManager = boardManager;
        this.currentPlayer = StoneColor.WHITE;
        this.neighbourManager = neighbourManager;
        this.captureManager = captureManager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("GO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(boardManager.getBoard().getBoardSize(), boardManager.getBoard().getBoardSize()));

        buttons = new BoardButton[boardManager.getBoard().getBoardSize()][boardManager.getBoard().getBoardSize()];

        for (int y = boardManager.getBoard().getBoardSize() - 1; y >= 0; y--) {
            for (int x = 0; x < boardManager.getBoard().getBoardSize(); x++) {
                buttons[x][y] = new BoardButton(x, y);
                buttons[x][y].setPreferredSize(new Dimension(50, 50));
                buttons[x][y].addActionListener(new ButtonClickListener(x, y));
                add(buttons[x][y]);
            }
        }

        pack();
        setVisible(true);
    }

    private class BoardButton extends JButton {
        private final int x;
        private final int y;

        public BoardButton(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            Stone stone = boardManager.getStone(x, y);
            if (stone != null) {
                g.setColor(stone.getStoneColor() == StoneColor.WHITE ? Color.WHITE : Color.BLACK);
                g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int x;
        private final int y;
        private int blackPoints = 0;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                boardManager.addStone(x, y, new Stone(currentPlayer));
                neighbourManager.addNeighbours(x, y);
                neighbourManager.updateNeighbours(x, y);
                int capturedStones = captureManager.checkForCapture(x, y);

                if(capturedStones > 0 && currentPlayer == StoneColor.WHITE)
                    ;
                else if(capturedStones > 0 && currentPlayer == StoneColor.BLACK)
                    blackPoints += capturedStones;

                if(capturedStones == 0)
                    captureManager.checkForSuicide(x, y);
                updateGUI();
                switchPlayer();
            } catch (OccupiedTileException | SuicideException ex) {
                JOptionPane.showMessageDialog(GameGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateGUI() {
        for (int y = boardManager.getBoard().getBoardSize() - 1; y >= 0; y--) {
            for (int x = 0; x < boardManager.getBoard().getBoardSize(); x++) {
                buttons[x][y].repaint();
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
        setTitle(currentPlayer + "'s turn");
    }

    public static void main(String[] args) {
        Board board = new Board(9);
        BoardManager boardManager = new BoardManager(board);
        ArrayOfNeighbours arrayOfNeighbours = new ArrayOfNeighbours(9);
        NeighbourManager neighbourManager = new NeighbourManager(board, arrayOfNeighbours);
        CaptureManager captureManager = new CaptureManager(board, boardManager, neighbourManager);
        SwingUtilities.invokeLater(() -> new GameGUI(boardManager, neighbourManager, captureManager));
    }
}
