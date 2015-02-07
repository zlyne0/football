package promitech.football.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import promitech.football.files.MovementGraphFileOperator;

public class WeightMapTest {

    private static final int CELL_HEIGHT = 16;
    private static final int CELL_WIDTH = 10;
    
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canSetVal() throws Exception {
        // given
        WeightMap weightMap = new WeightMap(CELL_WIDTH, CELL_HEIGHT);
        LinkedList<PlayfieldPoint> l = new LinkedList<PlayfieldPoint>();
        l.add(new PlayfieldPoint(4, 0));
        l.add(new PlayfieldPoint(5, 0));
        l.add(new PlayfieldPoint(6, 0));
        weightMap.generateWithoutOptimizingMap(l);
        
        PlayfieldPoint point = new PlayfieldPoint(5, 5);
        int val = 69;
        
        // when
        weightMap.setVal(point, val);

        // then
        assertEquals(val, weightMap.getVal(point));
    }
    
    @Test
    public void canGenerateOptimizationWeightMap() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/horseShose.dat");
        
        // when
        WeightMap weightMap = movementGraph.generateWeightMap(Player.Two);

        // then
        List<PlayfieldPoint> zeroWeightPoints = new ArrayList<PlayfieldPoint>();
        
        for (int i=10; i<=16; i++) {
            zeroWeightPoints.add(new PlayfieldPoint(10, i));
            zeroWeightPoints.add(new PlayfieldPoint(7, i));
        }
        for (int i=2; i<=8; i++) {
            zeroWeightPoints.add(new PlayfieldPoint(i, 9));
        }
        zeroWeightPoints.add(new PlayfieldPoint(0, 11));
        zeroWeightPoints.add(new PlayfieldPoint(1, 10));
        zeroWeightPoints.add(new PlayfieldPoint(2, 10));
        zeroWeightPoints.add(new PlayfieldPoint(2, 8));
        
        for (PlayfieldPoint p : zeroWeightPoints) {
            int val = weightMap.getVal(p);
            assertEquals(0, val);
        }
    }
    
    @Test
    public void canGeneratePathToGatewayForGatewayPoint() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/horseShose.dat");
        WeightMap weightMap = movementGraph.generateWeightMap(Player.One);

        // when
        LinkedList<PlayfieldPoint> pathToGateway = weightMap.generatePathFromPointToGateway(new PlayfieldPoint(4, 0));

        // then
        assertEquals(0, pathToGateway.size());
    }

    @Test
    public void canGeneratePathToGatewayForPlayfieldPoint() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/horseShose.dat");
        WeightMap weightMap = movementGraph.generateWeightMap(Player.Two);

        // when
        LinkedList<PlayfieldPoint> pathToGateway = weightMap.generatePathFromPointToGateway(new PlayfieldPoint(7, 14));

        // then
        assertEquals(2, pathToGateway.size());
        assertTrue(pathToGateway.get(0).equalCords(7, 15));
        assertTrue(pathToGateway.get(1).equalCords(6, 16));
    }
    
    @Test
    public void optimizedRoadShouldBeEmpty() throws Exception {
		// given
	    MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/bounceFromGatewayPost.dat"); 
	    WeightMap weightMap = movementGraph.generateWeightMap(Player.Two);

	    PlayfieldPoint sourcePoint = new PlayfieldPoint(6, 15);
    	
    	// when
		LinkedList<PlayfieldPoint> generatePathFromPointToGateway = weightMap.generatePathFromPointToGateway(sourcePoint);
	    
    	// then
		assertTrue(generatePathFromPointToGateway.isEmpty());
    }
    
}
