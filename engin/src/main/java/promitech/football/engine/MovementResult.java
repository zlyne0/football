package promitech.football.engine;

public class MovementResult {
    public boolean canMove = false;
    public boolean canBounce = false;

    public Player winner;
    public WinReason winReason;
    
    private MovementResult() {
    }
    
    public static MovementResult instanceWithAbilityToMove() {
        MovementResult mr = new MovementResult();
        mr.canMove = true;
        return mr;
    }
    
    public static MovementResult instanceWithoutAbilityToMove() {
        MovementResult mr = new MovementResult();
        mr.canMove = false;
        return mr;
    }
}
