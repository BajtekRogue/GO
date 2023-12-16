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
    private Scanner scanner;

    public GameMaster(){
    }

    public void startGame(int boardSize){
        board = new Board(boardSize);
        boardManager = new BoardManager(board);
        arrayOfNeighbours = new ArrayOfNeighbours(boardSize);
        neighbourManager = new NeighbourManager(board, arrayOfNeighbours);
        scanner = new Scanner(System.in);
        System.out.println("Hello, welcome to \"GO\". Blacks will start. To place your stone type the (x,y) coordinates you wish to place it. To surrender input (-1,-1)");
        gameplay();
    }

    public void gameplay(){
        int turnsCounter = 0;
        int x = 0;
        int y = 0;
        Stone stone;
        boolean mainLoop = true;
        while(mainLoop){
            boardManager.printBoard();
            turnsCounter ++;
            if(turnsCounter % 2 == 0){
                System.out.println("Whites turn");
                stone = new Stone(StoneColor.WHITE);
            }else{
                System.out.println("Blacks turn");
                stone = new Stone(StoneColor.BLACK);
            }

            boolean correctInput = false;
            while(!correctInput){
                try{
                    x = Integer.parseInt(scanner.nextLine());
                    y = Integer.parseInt(scanner.nextLine());
                    if(x == -1 && y == -1){
                        mainLoop = false;
                        correctInput = true;
                        break;
                    }


                }catch(NumberFormatException e){
                    System.out.println("Incorrect input");
                }
                try{
                    boardManager.addStone(x, y, stone);
                    neighbourManager.addNeighbours(x, y);
                    neighbourManager.updateNeighbours(x, y);
                    correctInput = true;
                }catch (OccupiedTileException e){
                    System.out.println(e.getMessage());

                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                }catch (SuicideException e){
                    boardManager.removeStone(x, y);
                    System.out.println(e.getMessage());
                }

            }
        }

        scanner.close();
        arrayOfNeighbours.printArray();
    }


}
