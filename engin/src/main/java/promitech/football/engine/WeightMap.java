package promitech.football.engine;

import java.util.LinkedList;


public class WeightMap {
    private int weight[];
    private final int playfieldWidth; 
    private final int playfieldHeight;
    
    private MoveDirection[] optimizingWeightMap = null;
    private LinkedList<PlayfieldPoint> zeroWeightPoints = null;
    
    public WeightMap(int playfieldWidth, int playfieldHeight) {
        this.playfieldWidth = playfieldWidth;
        this.playfieldHeight = playfieldHeight;
        createWeightTable();
    }

    private void createWeightTable() {
        weight = new int[(playfieldHeight+1)*(playfieldWidth+1)];
        for (int i=0; i<weight.length; i++ ) {
            weight[i] = Integer.MAX_VALUE;
        }
    }
    
    private void searchZeroPoints(MovementGraph graph, PlayfieldPoint srcPoint) {
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
            if (getOptimizingWeightMapVal(destPoint) != null) {
                continue;
            }
            if (graph.canBounceBallFrom(srcPoint, destPoint)) {
                setOptimizingWeightMapVal(destPoint, md.getOpositeDirection());
                zeroWeightPoints.add(destPoint);
                searchZeroPoints(graph, destPoint);
            } else {
                
            }
        }
    }
    
    public void generate(MovementGraph graph, Player player) {
        LinkedList<PlayfieldPoint> playerGateway = graph.getPlayerOpponentGateway(player);
        createOptimizingWeightMap(player, playerGateway);
        
        zeroWeightPoints = new LinkedList<PlayfieldPoint>(playerGateway);
        for (PlayfieldPoint p : playerGateway) {
            MovementGraph newGraph = new MovementGraph(graph);
            searchZeroPoints(newGraph, p);
        }
        generateMapFromZeroPoints(zeroWeightPoints);
    }
    
    public void generateWithoutOptimizingMap(LinkedList<PlayfieldPoint> playerGateway) {
        generateMapFromZeroPoints(playerGateway);
    }
    
    private void createOptimizingWeightMap(Player player, LinkedList<PlayfieldPoint> playerGateway) {
        optimizingWeightMap = new MoveDirection[(playfieldHeight+1)*(playfieldWidth+1)];
        for (PlayfieldPoint p : playerGateway) {
            if (Player.One.equals(player)) {
                setOptimizingWeightMapVal(p, MoveDirection.NORTH);
            } else {
                setOptimizingWeightMapVal(p, MoveDirection.SOUTH);
            }
        }
    }
    
    private void generateMapFromZeroPoints(LinkedList<PlayfieldPoint> zeroWeightPoints) {
        LinkedList<PlayfieldPoint> heap = new LinkedList<PlayfieldPoint>();
        for (PlayfieldPoint p : zeroWeightPoints) {
            heap.add(p);
            setVal(p, 0);
        }
        
        PlayfieldPoint tmpPoint = new PlayfieldPoint();
        while (!heap.isEmpty()) {
            PlayfieldPoint srcPoint = heap.removeFirst();
            int srcWeight = getVal(srcPoint);
            
            for (MoveDirection direction : MoveDirection.values()) {
                tmpPoint.changeCords(srcPoint).addAndChange(direction);
                if (!isPointOnPlayfield(tmpPoint)) {
                    continue;
                }
                
                int destWeight = getVal(tmpPoint);
                if (srcWeight+1 < destWeight) {
                    setVal(tmpPoint, srcWeight + 1);
                    heap.add(new PlayfieldPoint(tmpPoint));
                }
            }
        }
        PlayfieldPoint p = new PlayfieldPoint();
        int val;
        for (int i=0; i<playfieldWidth; i++) {
            val = getVal(p.changeCords(1, i));
            setVal(p.changeCords(0, i), val);
            
            val = getVal(p.changeCords(playfieldWidth-1, i));
            setVal(p.changeCords(playfieldWidth, i), val);
        }
    }

    public boolean isPointOnPlayfield(PlayfieldPoint p) {
        return p.x >= 0 && p.x <= playfieldWidth && p.y >= 0 && p.y <= playfieldHeight;
    }
    
    public void setOptimizingWeightMapVal(PlayfieldPoint point, MoveDirection val) {
        optimizingWeightMap[toTabCord(point)] = val;
    }
    
    public MoveDirection getOptimizingWeightMapVal(PlayfieldPoint point) {
        return optimizingWeightMap[toTabCord(point)];
    }
    
    public void setVal(PlayfieldPoint point, int val) {
        weight[toTabCord(point)] = val;
    }
    
    public int getVal(PlayfieldPoint point) {
        return weight[toTabCord(point)];
    }
    
    private int toTabCord(PlayfieldPoint point) {
        return point.y * (playfieldWidth+1) + point.x;
    }

    public LinkedList<PlayfieldPoint> generatePathFromPointToGateway(PlayfieldPoint point) {
        LinkedList<PlayfieldPoint> l = new LinkedList<PlayfieldPoint>();
        
        point = new PlayfieldPoint(point);
        MoveDirection moveDirection = getOptimizingWeightMapVal(point);
        while (moveDirection != null) {
            point = point.add(moveDirection);
            if (isPointOnMap(point)) {
                l.add(point);
                moveDirection = getOptimizingWeightMapVal(point);
            } else {
                moveDirection = null;
            }
        }
        return l;
    }
    
    private boolean isPointOnMap(PlayfieldPoint point) {
        return point.x >= 0 && point.y >= 0 && point.x <= playfieldWidth && point.y <= playfieldHeight;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<weight.length; i++) {
            sb.append("\t").append(weight[i]);
            if (i % (playfieldWidth+1) == playfieldWidth) {
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }
    
}
