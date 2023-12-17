package GameObjectsLogic;

import GameObjects.ArrayOfNeighbours;
import GameObjects.Board;
import GameObjects.Stone;
import GameObjects.StoneColor;
import MyExceptions.OccupiedTileException;
import MyExceptions.SuicideException;

import java.util.Scanner;

public class GameMaster {

    private Board board;
    private BoardManager boardManager;
    private ArrayOfNeighbours arrayOfNeighbours;
    private NeighbourManager neighbourManager;
    private CaptureManager captureManager;
    private Scanner scanner;

    public GameMaster(){
    }

    public void startGame(int boardSize){
        board = new Board(boardSize);
        boardManager = new BoardManager(board);
        arrayOfNeighbours = new ArrayOfNeighbours(boardSize);
        neighbourManager = new NeighbourManager(board, arrayOfNeighbours);
        captureManager = new CaptureManager(board, boardManager, neighbourManager);
        scanner = new Scanner(System.in);
        System.out.println("Hello, welcome to \"GO\". Blacks will start. To place your stone type the (x,y) coordinates you wish to place it. To surrender input (-1,-1)");
        gameplay();
    }

    public void gameplay(){
        int whitePoints = 0;
        int blackPoints = 0;
        boolean whiteTurn = false;
        boolean blackTurn = true;
        int capturedStones = 0;
        int passCounter = 0;
        int x = 0;
        int y = 0;
        Stone stone;
        boolean mainLoop = true;

        while(mainLoop){
            boardManager.printBoard();


            if(whiteTurn){
                System.out.println("Whites turn");
                stone = new Stone(StoneColor.WHITE);
            }
            else if(blackTurn){
                System.out.println("Blacks turn");
                stone = new Stone(StoneColor.BLACK);
            }else
                return;

            if(passCounter == 2) {
                scanner.close();
                endGame(whitePoints, blackPoints);
                return;
            }

            boolean correctInput = false;

            while(!correctInput){
                try{
                    x = Integer.parseInt(scanner.nextLine());
                    y = Integer.parseInt(scanner.nextLine());

                    if(x == -1 && y == -1){
                        passCounter ++;
                        break;
                    }
                    else
                        passCounter = 0;

                }catch(NumberFormatException e){
                    System.out.println("Incorrect input");
                }

                try{
                    boardManager.addStone(x, y, stone);
                    neighbourManager.addNeighbours(x, y);
                    neighbourManager.updateNeighbours(x, y);
                    capturedStones = captureManager.checkForCapture(x, y);

                    if(capturedStones > 0 && whiteTurn)
                        whitePoints += capturedStones;
                    else if(capturedStones > 0 && blackTurn)
                        blackPoints += capturedStones;

                    if(capturedStones == 0)
                        captureManager.checkForSuicide(x, y);

                    correctInput = true;

                }catch (OccupiedTileException e){
                    System.out.println(e.getMessage());
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                }catch (SuicideException e){
                    System.out.println(e.getMessage());
                }

            }

            if(whiteTurn){
                whiteTurn = false;
                blackTurn = true;
            }
            else if(blackTurn){
                whiteTurn = true;
                blackTurn = false;
            }
        }

    }

    public void endGame(int whitePoints, int blackPoints){
        System.out.println("The game has ended");
        if(whitePoints > blackPoints){
            System.out.println("White has won");
        }else if(whitePoints < blackPoints){
            System.out.println("Black has won");
        }else{
            System.out.println("Draw");
        }
        System.out.println("White has " + whitePoints + " points");
        System.out.println("Black has " + blackPoints + " points");

    }

}
