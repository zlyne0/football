package promitech.football.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

//Wyjrzyj prze okno
//Spójrz na tych ludzi.
//A teraz powiedz mi, którzy z nich s¹ wolni?
//Wolni od zad³u¿enia,
//obaw,
//stresu,
//strachu,
//niepowodzeñ, poni¿eñ, zdrady.
//Ilu z nich chcia³oby siê urodziæ wiedz¹c to co wiedz¹ teraz?
//Spytaj siê samej ile z nich zrobi³oby wszystko tak samo raz jeszcze?       


public class MovementGraph implements Serializable {
	private static final long serialVersionUID = 1L;
    
    private static final int PLAYFIELD_HEIGHT = 16;
    private static final int PLAYFIELD_WIDTH = 10;
    private static final int MIDDLE_OF_PLAYFIELD = PLAYFIELD_WIDTH / 2;
    

	private final PlayfieldGraphNode[][] playfieldGraph;
    
    private final PlayfieldPoint ball;
    private Player playerTurn = Player.One;

    private static final LinkedList<PlayfieldPoint> playerOneGateway = new LinkedList<PlayfieldPoint>();
    private static final LinkedList<PlayfieldPoint> playerTwoGateway = new LinkedList<PlayfieldPoint>();
    static {
        playerTwoGateway.add(new PlayfieldPoint(4, 0));
        playerTwoGateway.add(new PlayfieldPoint(5, 0));
        playerTwoGateway.add(new PlayfieldPoint(6, 0));
        playerOneGateway.add(new PlayfieldPoint(4, PLAYFIELD_HEIGHT));
        playerOneGateway.add(new PlayfieldPoint(5, PLAYFIELD_HEIGHT));
        playerOneGateway.add(new PlayfieldPoint(6, PLAYFIELD_HEIGHT));
        
    }
    
    public MovementGraph(MovementGraph mg) {
        playerTurn = mg.playerTurn;
        ball = new PlayfieldPoint(mg.ball);
        playfieldGraph = new PlayfieldGraphNode[PLAYFIELD_HEIGHT+1][PLAYFIELD_WIDTH+1];

        for (int y = 0; y < playfieldGraph.length; y++) {
            PlayfieldGraphNode[] row = playfieldGraph[y];
            PlayfieldGraphNode[] mgRow = mg.playfieldGraph[y];
            for (int x = 0; x < row.length; x++) {
                row[x] = new PlayfieldGraphNode(mgRow[x]);
            }
        }
    }
    
    public MovementGraph() {
        playfieldGraph = new PlayfieldGraphNode[PLAYFIELD_HEIGHT+1][PLAYFIELD_WIDTH+1];
        for (int y = 0; y < playfieldGraph.length; y++) {
            PlayfieldGraphNode[] row = playfieldGraph[y];
            for (int x = 0; x < row.length; x++) {
                PlayfieldGraphNode node = new PlayfieldGraphNode();
                setBlockedBoundsOnNodeForCordinates(node, x, y);
				row[x] = node;
            }
        }
        ball = new PlayfieldPoint(PLAYFIELD_WIDTH/2, PLAYFIELD_HEIGHT/2);
    }
    
    public MovementGraph(DataInputStream ois) throws IOException {
        ois.readInt(); // width
        ois.readInt(); // height
        playerTurn = Player.values()[ois.readInt()];
        ball = new PlayfieldPoint().readFromStream(ois);
        
        playfieldGraph = new PlayfieldGraphNode[PLAYFIELD_HEIGHT+1][PLAYFIELD_WIDTH+1];
        
        for (int y = 0; y < playfieldGraph.length; y++) {
            PlayfieldGraphNode[] row = playfieldGraph[y];
            for (int x = 0; x < row.length; x++) {
                try {
                    PlayfieldGraphNode node = new PlayfieldGraphNode(ois);
                    setBlockedBoundsOnNodeForCordinates(node, x, y);
					row[x] = node;
                } catch (IOException e) {
                    System.out.println("x = " + x + ", y = " + y);
                    throw e;
                }
            }
        }
    }
    
    private void setBlockedBoundsOnNodeForCordinates(PlayfieldGraphNode node, int x, int y) {
    	if (x == 0) {
    		node.markAllLeftSide();
    	}
    	if (x == PLAYFIELD_WIDTH) {
    		node.markAllRightSide();
    	}
    	if (y == 0) {
    		node.markAllUpperSide();
    	}
    	if (y == PLAYFIELD_HEIGHT) {
    		node.markAllBottomSide();
    	}
    }

    public void writeToStream(DataOutputStream oos) throws IOException {
        oos.writeInt(PLAYFIELD_WIDTH);
        oos.writeInt(PLAYFIELD_HEIGHT);
        oos.writeInt(playerTurn.ordinal());
        ball.writeToStream(oos);

        for (int y=0; y<playfieldGraph.length; y++) {
            PlayfieldGraphNode[] graphRow = playfieldGraph[y];
            for (int x=0; x<graphRow.length; x++) {
                graphRow[x].writeToStream(oos);
            }
        }
    }
    
    public void paint(PlayfieldDrawer playfieldDrawer) {
        PlayfieldPoint fromPoint = new PlayfieldPoint();
        PlayfieldPoint toPoint = new PlayfieldPoint();
    	
    	for (int y=0; y<playfieldGraph.length; y++) {
    		PlayfieldGraphNode[] graphRow = playfieldGraph[y];
    		for (int x=0; x<graphRow.length; x++) {
    			PlayfieldGraphNode node = graphRow[x];
    			if (!node.hasConnectionsToNeighbours()) {
    				continue;
    			}
    			
    			fromPoint.changeCords(x, y);
    			// draw only field some direction to avoid repeat draw
                Player player = null;
				player = node.getConnection(MoveDirection.NORTH_EAST);
				if (player != null) {
	                toPoint.changeCords(x, y);
				    toPoint.addAndChange(MoveDirection.NORTH_EAST);
					playfieldDrawer.drawPlayLine(fromPoint, toPoint, player);
				}
				player = node.getConnection(MoveDirection.EAST);
				if (player != null) {
                    toPoint.changeCords(x, y);
                    toPoint.addAndChange(MoveDirection.EAST);
					playfieldDrawer.drawPlayLine(fromPoint, toPoint, player);
				}
				player = node.getConnection(MoveDirection.SOUTH_EAST);
				if (player != null) {
                    toPoint.changeCords(x, y);
                    toPoint.addAndChange(MoveDirection.SOUTH_EAST);
					playfieldDrawer.drawPlayLine(fromPoint, toPoint, player);
				}
				player = node.getConnection(MoveDirection.SOUTH);
				if (player != null) {
                    toPoint.changeCords(x, y);
                    toPoint.addAndChange(MoveDirection.SOUTH);
					playfieldDrawer.drawPlayLine(fromPoint, toPoint, player);
				}
    		}
    	}
        playfieldDrawer.drawBall(ball, playerTurn);
    }

    public boolean canMoveBallToNeighbourPoint(PlayfieldPoint p) {
    	return isPointOnPlayfield(p) && !ball.equalCords(p) && isPointNeighbours(ball, p) && isFreeWayToNeighbour(ball, p);
    }

    public MovementResult moveBallToNeighbourPoint(PlayfieldPoint p) {
    	boolean canMove = canMoveBallToNeighbourPoint(p);
    	if (!canMove) {
    	    return MovementResult.instanceWithoutAbilityToMove();
    	}

    	MovementResult movementResult = MovementResult.instanceWithAbilityToMove();
    	movementResult.canBounce = canBounceBallFrom(ball, p);
    	
    	markLine(ball, p);
    	ball.changeCords(p);

    	prepareMatchResult(movementResult);
    	return movementResult;
    }

    public MovementResult moveBallViaPath(MovementPath path) {
        for (int i=1; i<path.getPath().size(); i++) {
            PlayfieldPoint dest = path.getPath().get(i);
            markLine(ball, dest);
            ball.changeCords(dest);
        }
        
        MovementResult movementResult = MovementResult.instanceWithoutAbilityToMove();
        movementResult.canBounce = false;
        prepareMatchResult(movementResult);
        return movementResult; 
    }    
    
    private void prepareMatchResult(MovementResult movementResult) {
        if (isPlayerScoreAGoal(playerTurn)) {
            movementResult.winner = playerTurn;
            movementResult.winReason = WinReason.SCORE_A_GOAL;
        } else {
            if (getNode(ball).areAllWaysBlocked()) {
                movementResult.winner = playerTurn.opponent();
                movementResult.winReason = WinReason.OPPONENT_BLOCKED;
            } else {
                if (!movementResult.canBounce) {
                    playerTurn = playerTurn.opponent();
                }
            }
        }
    }
    
    private boolean isPlayerScoreAGoal(Player player) {
        if (Player.One.equals(player)) {
            return ball.y == 0 && 
                    (ball.x == MIDDLE_OF_PLAYFIELD-1 || ball.x == MIDDLE_OF_PLAYFIELD || ball.x == MIDDLE_OF_PLAYFIELD+1); 
        } else {
            return ball.y == PLAYFIELD_HEIGHT && 
                    (ball.x == MIDDLE_OF_PLAYFIELD-1 || ball.x == MIDDLE_OF_PLAYFIELD || ball.x == MIDDLE_OF_PLAYFIELD+1); 
        }
    }
    
    public void markLine(PlayfieldPoint src, PlayfieldPoint dest) {
    	PlayfieldPoint tmpPoint = new PlayfieldPoint();
    	MoveDirection direction = null;
    	
    	PlayfieldGraphNode srcNode = playfieldGraph[src.y][src.x];
    	direction = MoveDirection.createDirection(src, dest, tmpPoint);
    	srcNode.makeConnection(direction, playerTurn);
    	
    	PlayfieldGraphNode destNode = playfieldGraph[dest.y][dest.x];
    	direction = MoveDirection.createDirection(dest, src, tmpPoint);
    	destNode.makeConnection(direction, playerTurn);
    }
    
    public boolean canBounceBallFrom(PlayfieldPoint src, PlayfieldPoint dest) {
        if (isPointOnCenterOfGatway(dest)) {
            return false;
        }
        if (isPointOnGatewayPost(dest)) {
            return true;
        }
        PlayfieldGraphNode destNode = playfieldGraph[dest.y][dest.x];
        if (isPointOnBound(dest)) {
            if (isPointOnCorner(dest)) {
                return true;
            }
            if (isPointOnBound(src)) {
                if (arePointsOnTheSameBound(src, dest)) {
                    return destNode.hasConnectionsToNeighbours();
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return destNode.hasConnectionsToNeighbours(); 
        }
    }
    
    public boolean isPointOnGatewayPost(PlayfieldPoint p) {
        return 
        		(p.y == 0 || p.y == PLAYFIELD_HEIGHT) && 
        		(p.x == MIDDLE_OF_PLAYFIELD - 1 || p.x == MIDDLE_OF_PLAYFIELD + 1);
    }
    
    private boolean isPointOnCenterOfGatway(PlayfieldPoint p) {
        return p.x == MIDDLE_OF_PLAYFIELD && (p.y == 0 || p.y == PLAYFIELD_HEIGHT);
    }
    
    public boolean willBlocked(PlayfieldPoint src, PlayfieldPoint dest) {
		PlayfieldPoint tmpWorkingPoint = new PlayfieldPoint();
        MoveDirection direction = MoveDirection.createDirection(dest, src, tmpWorkingPoint);
        PlayfieldGraphNode destNode = playfieldGraph[dest.y][dest.x];
        
        if (isPointOnBound(dest)) {
        	PlayfieldGraphNode cc = new PlayfieldGraphNode(destNode);
        	if (dest.x == 0) {
        		cc.markAllLeftSide();
        	}
        	if (dest.x == PLAYFIELD_WIDTH) {
        		cc.markAllRightSide();
        	}
        	if (dest.y == 0) {
        		cc.markAllUpperSide();
        	}
        	if (dest.y == PLAYFIELD_HEIGHT) {
        		cc.markAllBottomSide();
        	}
        	cc.makeConnection(direction, Player.One);
        	return cc.areAllWaysBlocked();
        } else {
        	PlayfieldGraphNode cc = new PlayfieldGraphNode(destNode);
        	// it does not matter which player 
        	cc.makeConnection(direction, Player.One);
        	return cc.areAllWaysBlocked();
        }
    }

    public PlayfieldGraphNode getNode(PlayfieldPoint p) {
        return playfieldGraph[p.y][p.x];
    }
    
    private boolean isPointOnCorner(PlayfieldPoint p) {
        return 
            p.x == 0 && p.y == 0 || 
            p.x == 0 && p.y == PLAYFIELD_HEIGHT ||
            p.x == PLAYFIELD_WIDTH && p.y == 0 ||
            p.x == PLAYFIELD_WIDTH && p.y == PLAYFIELD_HEIGHT;
    }
    
    public boolean isPointOnBound(PlayfieldPoint p) {
        return p.x == 0 || p.x == PLAYFIELD_WIDTH || p.y == 0 || p.y == PLAYFIELD_HEIGHT;
    }
    
    public boolean arePointsOnTheSameBound(PlayfieldPoint a, PlayfieldPoint b) {
        return a.y == b.y || a.x == b.x;
    }
    
    public boolean isPointOnPlayfield(PlayfieldPoint p) {
        return p.x >= 0 && p.x <= PLAYFIELD_WIDTH && p.y >= 0 && p.y <= PLAYFIELD_HEIGHT;
	}

	public boolean isFreeWayToNeighbour(PlayfieldPoint src, PlayfieldPoint dest) {
		// src and dest should be neighbour
		PlayfieldGraphNode aNode = playfieldGraph[src.y][src.x];
		
		PlayfieldPoint tmpWorkingPoint = new PlayfieldPoint();
        MoveDirection direction = MoveDirection.createDirection(src, dest, tmpWorkingPoint);
		
        return !aNode.hasConnection(direction);
	}

	public boolean isPointNeighbours(PlayfieldPoint a, PlayfieldPoint b) {
		if (a.x == b.x) {
			if (Math.abs(a.y - b.y) == 1) {
				return true;
			}
		}
		if (a.y == b.y){
			if (Math.abs(a.x - b.x) == 1 ) {
				return true;
			}
		}
		if (a.y == b.y + 1) {
			if (Math.abs(a.x - b.x) == 1 ) {
				return true;
			}
		}
		if (a.y == b.y - 1) {
			if (Math.abs(a.x - b.x) == 1 ) {
				return true;
			}
		}
        return false;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public PlayfieldPoint getBallCord() {
        return new PlayfieldPoint(ball);
    }
    
    public WeightMap generateWeightMap(Player player) {
        WeightMap weightMap = new WeightMap(PLAYFIELD_WIDTH, PLAYFIELD_HEIGHT);
        weightMap.generate(this, player);
        return weightMap;
    }
    
    public LinkedList<PlayfieldPoint> getPlayerOpponentGateway(Player player) {
        if (Player.One.equals(player)) {
            return playerTwoGateway;
        } else {
            return playerOneGateway;
        }
    }

    public int getPlayfieldWidth() {
        return PLAYFIELD_WIDTH;
    }

    public int getPlayfieldHeight() {
        return PLAYFIELD_HEIGHT;
    }
}
