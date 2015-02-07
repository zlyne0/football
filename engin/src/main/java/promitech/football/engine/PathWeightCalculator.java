package promitech.football.engine;

import java.util.LinkedList;


public class PathWeightCalculator {

    public final static double GOAL_WEIGHT = 100000;
    
    private final WeightMap weightMap;
    private final DifficultyLevel difficultyLevel;

	public PathWeightCalculator(WeightMap weightMap, DifficultyLevel difficultyLevel) {
	    this.weightMap = weightMap;
	    this.difficultyLevel = difficultyLevel;
	}
	
    public boolean scoreAGoal(PlayfieldPoint destPoint) {
        return weightMap.getVal(destPoint) == 0;
    }
    
    public LinkedList<PlayfieldPoint> generatePathFromPointToGateway(PlayfieldPoint point) {
        return weightMap.generatePathFromPointToGateway(point);
    }
    
	public int getDistance(Player player, MovementPath path) {
		PlayfieldPoint first = path.getFirstPoint();
		PlayfieldPoint last = path.getLastPoint();
		return getDistance(first, last);
	}

	public int getDistance(PlayfieldPoint first, PlayfieldPoint last) {
	    return weightMap.getVal(first) - weightMap.getVal(last);
	}

	public double weightBetweenPlayersMoves(MovementPath playerMove, MovementPath nextPlayerMove) {
		return playerMove.getPathWeight() - nextPlayerMove.getPathWeight();
	}
	
	public double weight(MovementPath path, int playerRecursiveDepth) {
        int distance = getDistance(path.getFirstPoint(), path.getLastPoint());
        return stepWeight(distance, path.getPathLength(), playerRecursiveDepth);
	}
	
	private double stepWeight(int stepDistance, int stepPathLength, int playerRecursiveDepth) {
		double stepWeight = stepConstantFunction(playerRecursiveDepth) * stepDistance;
		double weight = 0;
		weight = weight + stepWeight;
		if (difficultyLevel.isSumPathLengthWeight()) {
			weight = weight + stepPathLength;
		}
		return weight;
	}

	private double[] stepConstant = new double[] { 1000, 100, 10, 1 };
	private double stepConstantFunction(int index) {
		if (index < 0 || index >= stepConstant.length) {
			throw new IllegalArgumentException("index " + index + " not exists in step constant function");
		}
		return stepConstant[index];
	}
}
