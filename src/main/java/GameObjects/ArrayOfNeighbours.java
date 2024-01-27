package GameObjects;

public class ArrayOfNeighbours {
    private final int arraySize;
    private final StoneNeighbours[][] arrayOfNeighbours;


    public ArrayOfNeighbours(int arraySize){
        this.arraySize = arraySize;
        arrayOfNeighbours = new StoneNeighbours[arraySize][arraySize];
    }

    public int getArraySize() {
        return arraySize;
    }

    public StoneNeighbours getNeighbours(int x, int y) throws ArrayIndexOutOfBoundsException{
        if(0 <= x && x < arraySize && 0 <= y && y < arraySize)
            return arrayOfNeighbours[x][y];
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect array position");
    }

    public void setNeighbours(int x, int y, StoneNeighbours stoneNeighbours) throws ArrayIndexOutOfBoundsException{
        if(0 <= x && x < arraySize && 0 <= y && y < arraySize)
            arrayOfNeighbours[x][y] = stoneNeighbours;
        else
            throw new ArrayIndexOutOfBoundsException("Incorrect array position");
    }
}
