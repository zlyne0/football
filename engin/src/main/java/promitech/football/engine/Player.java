package promitech.football.engine;

public enum Player {
    // going up
    One, 
    // going down
    Two;
    
    public Player opponent() {
        if (this.equals(One)) {
            return Two;
        }
        return One;
    }
}
