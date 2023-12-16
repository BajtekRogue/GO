package GameObjects;

public class StoneNeighbours {

    private NeighbourState north;
    private NeighbourState east;
    private NeighbourState south;
    private NeighbourState west;
    private int numberOfBreaths;
    private int numberOfConnections;

    public StoneNeighbours(NeighbourState north, NeighbourState east, NeighbourState south, NeighbourState west){
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        setNumberOfBreaths();
        setNumberOfConnections();
    }
    public NeighbourState getNorth() {
        return north;
    }

    public NeighbourState getEast() {
        return east;
    }

    public NeighbourState getSouth() {
        return south;
    }

    public NeighbourState getWest() {
        return west;
    }

    public int getNumberOfBreaths() {
        return numberOfBreaths;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public void setNorth(NeighbourState north) {
        this.north = north;
        setNumberOfBreaths();
        setNumberOfConnections();
    }

    public void setEast(NeighbourState east) {
        this.east = east;
        setNumberOfBreaths();
        setNumberOfConnections();
    }

    public void setSouth(NeighbourState south) {
        this.south = south;
        setNumberOfBreaths();
        setNumberOfConnections();
    }

    public void setWest(NeighbourState west) {
        this.west = west;
        setNumberOfBreaths();
        setNumberOfConnections();
    }

    public void setNumberOfBreaths() {
        numberOfBreaths = 0;
        if(north == NeighbourState.EMPTY)
            numberOfBreaths++;
        if(east == NeighbourState.EMPTY)
            numberOfBreaths++;
        if(south== NeighbourState.EMPTY)
            numberOfBreaths++;
        if(west == NeighbourState.EMPTY)
            numberOfBreaths++;
    }

    public void setNumberOfConnections() {
        numberOfConnections = 0;
        if(north == NeighbourState.ALLY)
            numberOfConnections++;
        if(east == NeighbourState.ALLY)
            numberOfConnections++;
        if(south== NeighbourState.ALLY)
            numberOfConnections++;
        if(west == NeighbourState.ALLY)
            numberOfConnections++;

    }
}
