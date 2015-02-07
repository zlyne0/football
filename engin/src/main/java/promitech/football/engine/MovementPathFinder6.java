package promitech.football.engine;

import java.util.LinkedList;


class PathWeightOptimization {
    private Double[][] weightTab;
            
    PathWeightOptimization(MovementGraph movementGraph, PlayfieldPoint point) {
        weightTab = new Double[movementGraph.getPlayfieldHeight()+1][movementGraph.getPlayfieldWidth()+1];
        weightTab[point.y][point.x] = Double.valueOf(0);
    }

    public boolean isItWorthGoForward(PlayfieldPoint destPoint, double localWeight) {
        Double val = weightTab[destPoint.y][destPoint.x];
        if (val == null) {
            weightTab[destPoint.y][destPoint.x] = localWeight;
            return true;
        } 
        if (val.doubleValue() >= localWeight) {
            return false;
        }
        weightTab[destPoint.y][destPoint.x] = localWeight;
        return true;
    }
    
    public void setWeight(PlayfieldPoint destPoint, double localWeight) {
        weightTab[destPoint.y][destPoint.x] = localWeight;
    }
}

public class MovementPathFinder6 {
	public final static RandomResponser response5050 = new RandomResponser(50);
	
    private final MovementGraph movementGraph;
    private final DifficultyLevel difficultyLevel;
    
    //private LeadDirectionController leadDirectionController = new LeadDirectionController();
    private LeadDirectionController leadDirectionController = null;
    
    public MovementPathFinder6(MovementGraph movementGraph, DifficultyLevel difficultyLevel) {
        this.movementGraph = new MovementGraph(movementGraph);
        this.difficultyLevel = difficultyLevel;
    }
    
	private MovementPath recursion(MovementGraph graph, MovementPath movementPath,
			PlayfieldPoint srcPoint, int playerRecursionLevel,
			Player playerTurn, int recursionIndex,
			PathWeightCalculator playerPathWeightCalculator,
			PathWeightOptimization pathWeightOptimization) 
	{
	    recursionIndex++;
        PlayfieldGraphNode srcNode = graph.getNode(srcPoint);
        PlayfieldPoint destPoint;

        MovementPath theBestMovementPath = null;
        
        boolean canBounceBall;
        for (MoveDirection md : MoveDirection.values()) {
            if (leadDirectionController != null && !leadDirectionController.canLeadDirection(recursionIndex, md)) {
                continue;
            }
            if (srcNode.hasConnection(md)) {
                continue;
            }
            destPoint = new PlayfieldPoint(srcPoint);
            destPoint.addAndChange(md);
            if (!graph.isPointOnPlayfield(destPoint)) {
                continue;
            }
            
            canBounceBall = graph.canBounceBallFrom(srcPoint, destPoint);
            
            if (canBounceBall && playerPathWeightCalculator.scoreAGoal(destPoint)) {
                MovementPath endMovePath = MovementPath.instanceCopiedFromPath(movementPath);
                endMovePath.add(destPoint);
                populateMovementPathWithWithStepsToGateway(endMovePath, playerPathWeightCalculator, destPoint);
                return endMovePath;
            }
            
            if (canBounceBall) {
                MovementGraph newGraph = new MovementGraph(graph);
                newGraph.markLine(srcPoint, destPoint);
                
                MovementPath newPath = MovementPath.instanceCopiedFromPath(movementPath);
                newPath.add(destPoint);
                
                double localWeight = playerPathWeightCalculator.weight(newPath, playerRecursionLevel);
                
                if (pathWeightOptimization.isItWorthGoForward(destPoint, localWeight)) {
                    MovementPath nextStepGoodPath = recursion(
                        newGraph, newPath, destPoint, playerRecursionLevel, 
                        playerTurn, recursionIndex, playerPathWeightCalculator, pathWeightOptimization
                    );
                    if (nextStepGoodPath != null && nextStepGoodPath.isScoreAGoal()) {
                        return nextStepGoodPath;
                    }
                    if (nextStepGoodPath != null && nextStepGoodPath.hasBetterWeight(theBestMovementPath)) {
                        theBestMovementPath = nextStepGoodPath;
                    }
                }
            } else {
                MovementPath endMovePath = MovementPath.instanceCopiedFromPath(movementPath);
                endMovePath.add(destPoint);
                
                double localWeight = playerPathWeightCalculator.weight(endMovePath, playerRecursionLevel);
                endMovePath.setPathWeight(localWeight);
                
                pathWeightOptimization.setWeight(destPoint, localWeight);
                
                // 0 - player   *
                // 1 - opponent *
                // 2 - player   *
                // 3 - opponent
                // 4 - player
                // 5 - opponent
                if (playerRecursionLevel <= difficultyLevel.getPredictOpponentMovesCount()) {
                    // opponent
                    MovementPath newPath = MovementPath.instanceWithReferenceToPath(endMovePath, destPoint);
                    
                    MovementGraph newGraph = new MovementGraph(graph);
                    newGraph.markLine(srcPoint, destPoint);
                    
                    PathWeightOptimization newPathWeightOptimization = new PathWeightOptimization(newGraph, destPoint);
                    
                    // opponent turn
                    PathWeightCalculator opponentPathWeightCalculator = new PathWeightCalculator(
                    		newGraph.generateWeightMap(playerTurn.opponent()),
                    		difficultyLevel
                    );
                    MovementPath nextStepGoodPath = recursion(
                        newGraph, newPath, destPoint, playerRecursionLevel+1, 
                        playerTurn.opponent(), recursionIndex, opponentPathWeightCalculator, newPathWeightOptimization);
                    if (nextStepGoodPath != null && !nextStepGoodPath.isBlocked()) {
                    	
                    	localWeight = playerPathWeightCalculator.weightBetweenPlayersMoves(endMovePath, nextStepGoodPath);
                    	endMovePath.setPathWeight(localWeight);
                        if (endMovePath.hasBetterWeight(theBestMovementPath)) {
                        	theBestMovementPath = endMovePath;
                        }
                        
                    }
                } else {                	
                    if (endMovePath.hasBetterWeight(theBestMovementPath)) {
                    	theBestMovementPath = endMovePath;
                    }
                }
            }
        }
        return theBestMovementPath;
    }
    
	private void populateMovementPathWithWithStepsToGateway(
			MovementPath movementPath, 
			PathWeightCalculator pathWeightCalculator, 
			PlayfieldPoint srcPoint
	) {
        movementPath.setPathWeight(PathWeightCalculator.GOAL_WEIGHT);
        movementPath.setScoreAGoal(true);
        LinkedList<PlayfieldPoint> pathToGateway = pathWeightCalculator.generatePathFromPointToGateway(srcPoint);
        for (PlayfieldPoint p : pathToGateway) {
            movementPath.add(p);
        }
	}
	
    public MovementPath generateTheBestMovement() {
        PathWeightCalculator pathWeightCalculator = new PathWeightCalculator(
        		movementGraph.generateWeightMap(movementGraph.getPlayerTurn()),
        		difficultyLevel
        );
        
        MovementPath movementPath = MovementPath.instanceWithSinglePoint(movementGraph.getBallCord());
        PlayfieldPoint srcPoint = new PlayfieldPoint(movementGraph.getBallCord());
        
        MovementPath foundMovementPath = null;
        if (pathWeightCalculator.scoreAGoal(srcPoint)) {
            populateMovementPathWithWithStepsToGateway(movementPath, pathWeightCalculator, srcPoint);
            foundMovementPath = movementPath;
        } else {
            MovementGraph newGraph = new MovementGraph(movementGraph);
            PathWeightOptimization pathWeightOptimization = new PathWeightOptimization(movementGraph, srcPoint);
            
            int playerRecursionLevel = 0;
            int recursionIndex = 0;
            foundMovementPath = recursion(
                newGraph, movementPath, srcPoint, playerRecursionLevel, 
                movementGraph.getPlayerTurn(), recursionIndex, pathWeightCalculator, pathWeightOptimization);
        }
        if (foundMovementPath == null) {
            foundMovementPath = MovementPath.instanceBlockedPath();
        }
        
        MovementPath theBestMovementPath = foundMovementPath;
		while (theBestMovementPath.getNextPlayerMovePath() != null) {
            theBestMovementPath = theBestMovementPath.getNextPlayerMovePath();
        }
        return theBestMovementPath;
    }
}
