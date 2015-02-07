package promitech.football.engine;

import java.util.LinkedList;


public class MovementPath {
	
    @Deprecated
    private static MovementPath blockedPath = new MovementPath();
    static {
    	blockedPath.blocked = true;
    	blockedPath.pathWeight = Double.MIN_VALUE;
    }
    
    // should include source point
    private final LinkedList<PlayfieldPoint> path;
    
    private MovementPath nextPlayerMovePath;
    private boolean blocked = false;
    private boolean scoreAGoal = false;
    private double pathWeight;
    
    private MovementPath() {
        path = new LinkedList<PlayfieldPoint>();
    }
    
    public static MovementPath instanceWithSinglePoint(PlayfieldPoint src) {
        MovementPath movementPath = new MovementPath();
        movementPath.add(src);
        return movementPath;
    }
    
    public static MovementPath instanceCopiedFromPath(MovementPath mp) {
        MovementPath movementPath = new MovementPath();
        movementPath.path.addAll(mp.path);
        movementPath.blocked = mp.blocked;
        movementPath.nextPlayerMovePath = mp.nextPlayerMovePath;
        return movementPath;
    }

    public static MovementPath instanceWithReferenceToPath(MovementPath mp, PlayfieldPoint point) {
        MovementPath movementPath = new MovementPath();
        movementPath.nextPlayerMovePath = mp;
        movementPath.path.add(point);
        return movementPath;        
    }

    public static MovementPath instanceWithReferenceToPath(MovementPath mp) {
        MovementPath movementPath = new MovementPath();
        movementPath.nextPlayerMovePath = mp;
        movementPath.path.add(mp.path.getLast());
        return movementPath;        
    }

    @Deprecated
	public static MovementPath instanceBlockedPath() {
		return blockedPath;
	}
    
    public MovementPath addFirst(PlayfieldPoint dest) {
        path.addFirst(dest);
        return this;
    }
    
    public MovementPath add(PlayfieldPoint dest) {
        path.add(dest);
        return this;
    }
    
    @Deprecated
    public PlayfieldPoint getFirstPoint() {
    	PlayfieldPoint p = path.getFirst();
    	if (p == null) {
    		throw new IllegalStateException("movement path is empty");
    	}
    	return p;
    }
    
    @Deprecated
    public PlayfieldPoint getLastPoint() {
    	PlayfieldPoint p = path.getLast();
    	if (p == null) {
    		throw new IllegalStateException("movement path is empty");
    	}
    	return p;
    }
    
    public String toStringWithoutRecursion() {
        StringBuffer sb = new StringBuffer();
        //sb.append("blocked: ").append(blocked).append(", ");
        //sb.append("distance: ").append(distance).append(", ");
        //sb.append("pathLength: ").append(pathLength).append(", ");
        sb.append("[");
        PlayfieldPoint p = null;
        for (int i=0; i<path.size(); i++) {
            p = path.get(i);
            sb.append(p.toString());
            if (i<path.size()-1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
        if (nextPlayerMovePath != null) {
            sb.append(nextPlayerMovePath).append("\r\n");
        }
        sb.append(toStringWithoutRecursion());
    	return sb.toString();
    }
    
    public LinkedList<PlayfieldPoint> getPath() {
        return path;
    }

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

    public int getPathLength() {
    	if (path == null || path.size() == 0) {
    		throw new IllegalArgumentException("path should never be null");
    	}
        return path.size()-1;
    }
    
    public MovementPath getNextPlayerMovePath() {
        return nextPlayerMovePath;
    }

    public LinkedList<MovementPath> nextMovesToList() {
    	LinkedList<MovementPath> ll = new LinkedList<MovementPath>();
    	MovementPath mp = this;
        while (mp != null) {
        	ll.addFirst(mp);
        	mp = mp.nextPlayerMovePath;
        }
    	return ll;
    }
    
	public double getPathWeight() {
		return pathWeight;
	}

	public void setPathWeight(double pathWeight) {
		this.pathWeight = pathWeight;
	}

	public boolean hasBetterWeight(MovementPath theBestMovementPath) {
		if (theBestMovementPath == null) {
			return true;
		}
		if (pathWeight == theBestMovementPath.pathWeight) {
			return MovementPathFinder6.response5050.response();
		}
		return pathWeight > theBestMovementPath.pathWeight; 
	}

    public boolean isScoreAGoal() {
        return scoreAGoal;
    }

    public void setScoreAGoal(boolean scoreAGoal) {
        this.scoreAGoal = scoreAGoal;
    }

}
