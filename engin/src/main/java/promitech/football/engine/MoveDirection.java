package promitech.football.engine;







public enum MoveDirection {
    NORTH(1, 1, 0),     // 0, -1
    NORTH_EAST(2, 2, 0),// 1, -1
    EAST(3, 2, 1),     // 1, 0
    SOUTH_EAST(4, 2, 2),// 1, 1
    SOUTH(5, 1, 2),    // 0, 1
    SOUTH_WEST(6, 0, 2),// -1, 1
    WEST(7, 0, 1),     // -1, 0
    NORTH_WEST(0, 0, 0);// -1, -1
    
    // cords in neighbor tab
    final byte x, y;
    public final int bitIndex;
    
    private MoveDirection(int bitIndex, int x, int y) {
    	this.bitIndex = bitIndex;
        this.x = (byte)x;
        this.y = (byte)y;
    }
    
    private static MoveDirection directionTab[][] = {
        { NORTH_WEST, NORTH, NORTH_EAST },
        { WEST,        null, EAST},
        { SOUTH_WEST, SOUTH, SOUTH_EAST}
    };
    
    public static MoveDirection createDirectionFromInternalNodeCords(byte x, byte y) {
        MoveDirection moveDirection = null;
        try {
            moveDirection = directionTab[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("can not get move direction, (" + x + ", " + y + ") x and y should be min 0, max 2", e);
        }
        if (moveDirection == null) {
            throw new IllegalStateException("moveDirection == null, propably x and y ("+x+", "+y+") are the same");
        }
        return moveDirection;
    }
    
    public static MoveDirection createDirection(PlayfieldPoint src, PlayfieldPoint dest, PlayfieldPoint tmpWorkingPoint) {
        tmpWorkingPoint.changeCords(dest).minusAndChange(src);
        
        MoveDirection moveDirection = null;
        try {
            moveDirection = directionTab[tmpWorkingPoint.y+1][tmpWorkingPoint.x+1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("src("+ src +") - dest("+ dest +") = " + tmpWorkingPoint + " but x and y should be min -1 and max 1", e);
        }
        
        if (moveDirection == null) {
            throw new IllegalStateException("moveDirection == null, propably src and dest are the same, src: " + src + ", dest = " + dest);
        }
        return moveDirection;
    }
    
    public MoveDirection getOpositeDirection() {
    	switch (this) {
		case NORTH: return SOUTH;
		case NORTH_EAST: return SOUTH_WEST;
		case EAST: return WEST;
		case SOUTH_EAST: return NORTH_WEST;
		case SOUTH: return NORTH;
		case SOUTH_WEST: return NORTH_EAST;
		case WEST: return EAST;
		case NORTH_WEST: return SOUTH_EAST;
		default:
			throw new IllegalStateException("can not recoginze direction: " + this);
		}
    }
}

