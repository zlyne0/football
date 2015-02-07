package promitech.football.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class PlayfieldGraphNode implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int pointNeighbours = 0;
	private int boundBlockedSteps = 0;

    public PlayfieldGraphNode(DataInputStream ois) throws IOException {
    	pointNeighbours = ois.readInt();
    }
    
    public PlayfieldGraphNode() {
    }
    
    public PlayfieldGraphNode(PlayfieldGraphNode n) {
    	pointNeighbours = n.pointNeighbours;
    	boundBlockedSteps = n.boundBlockedSteps;
    }

    public void writeToStream(DataOutputStream oos) throws IOException {
    	oos.writeInt(pointNeighbours);
    }
    
    public Player getConnection(MoveDirection moveDirection) {
    	if ((pointNeighbours & (1 << moveDirection.bitIndex)) != 0) {
    		return Player.One;
    	}
    	if ((pointNeighbours & (1 << (moveDirection.bitIndex + 8))) != 0) {
    		return Player.Two;
    	}
    	return null;
    }
    
    public void makeConnection(MoveDirection direction, Player playerTurn) {
    	int playerBits = 0;
    	if (Player.Two.equals(playerTurn)) {
    		playerBits = 8;
    	}
    	pointNeighbours |= (1 << (direction.bitIndex + playerBits));
    }
    
    public boolean hasConnection(MoveDirection direction) {
    	return 
    			((pointNeighbours & (1 << direction.bitIndex)) != 0) || 
    			((pointNeighbours & (1 << (direction.bitIndex + 8))) != 0); 
    }
    
    public boolean hasConnectionsToNeighbours() {
    	return pointNeighbours != 0;
    }
    
    public boolean areAllWaysBlocked() {
        // it's not working on bounds
    	return ((pointNeighbours & 0xFF) | ((pointNeighbours & 0xFF00) >> 8) | (boundBlockedSteps & 0xFF)) == 0xFF;
    }
    
    public void markAllUpperSide() {
    	boundBlockedSteps |= 1 << MoveDirection.NORTH_WEST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.NORTH.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.NORTH_EAST.bitIndex;
    }
    
    public void markAllBottomSide() {
    	boundBlockedSteps |= 1 << MoveDirection.SOUTH_WEST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.SOUTH.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.SOUTH_EAST.bitIndex; 
    }
    
    public void markAllLeftSide() {
    	boundBlockedSteps |= 1 << MoveDirection.NORTH_WEST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.WEST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.SOUTH_WEST.bitIndex;
    }
    
    public void markAllRightSide() {
    	boundBlockedSteps |= 1 << MoveDirection.NORTH_EAST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.EAST.bitIndex; 
    	boundBlockedSteps |= 1 << MoveDirection.SOUTH_EAST.bitIndex;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        MoveDirection.
//        for (int y=0; y<pointNeighbours.length; y++) {
//            Player[] row = pointNeighbours[y];
//            for (int x=0; x<row.length; x++) {
//                if (row[x] == null) {
//                    sb.append(".");
//                    continue;
//                }
//                if (Player.One.equals(row[x])) {
//                    sb.append("1");
//                } else {
//                    sb.append("2");
//                }
//            }
//            sb.append("\r\n");
//        }
//        sb.append(pathPointReference);
        return sb.toString();
    }
}
