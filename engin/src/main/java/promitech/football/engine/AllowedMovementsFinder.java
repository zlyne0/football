package promitech.football.engine;

public class AllowedMovementsFinder {
	private MovementGraph movementGraph;

	private MovementGraph allowedMovements = new MovementGraph(); 
	
	public AllowedMovementsFinder(MovementGraph movementGraph) {
        this.movementGraph = new MovementGraph(movementGraph);
        allowedMovements.getBallCord().changeCords(movementGraph.getBallCord());
	}
	
	private void recursion(MovementGraph graph, PlayfieldPoint srcPoint ) {
        PlayfieldGraphNode srcNode = graph.getNode(srcPoint);
        PlayfieldPoint destPoint;

        for (MoveDirection md : MoveDirection.values()) {
            if (srcNode.hasConnection(md)) {
                continue;
            }
            destPoint = new PlayfieldPoint(srcPoint);
            destPoint.addAndChange(md);
            if (!graph.isPointOnPlayfield(destPoint)) {
                continue;
            }
            allowedMovements.markLine(srcPoint, destPoint);
            if (graph.canBounceBallFrom(srcPoint, destPoint)) {
            	MovementGraph newGraph = new MovementGraph(graph);
            	newGraph.markLine(srcPoint, destPoint);
            	recursion(newGraph, destPoint);
            }
        }
	}
	
	public MovementGraph generatePossibleMovements() {
		MovementGraph newGraph = new MovementGraph(movementGraph);
		PlayfieldPoint srcPoint = new PlayfieldPoint(movementGraph.getBallCord());		
		recursion(newGraph, srcPoint);
		return allowedMovements;
	}
}
