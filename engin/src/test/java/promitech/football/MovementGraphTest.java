package promitech.football;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldDrawer;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.engine.WeightMap;
import promitech.football.files.MovementGraphFileOperator;

@RunWith(MockitoJUnitRunner.class)
public class MovementGraphTest {

    @Mock
    PlayfieldDrawer playfieldDrawer;

    @Mock
    Graphics graphics;
    
    private MovementGraph movementGraph = new MovementGraph();
    private int CELL_HEIGHT = movementGraph.getPlayfieldHeight();
    private int CELL_WIDTH = movementGraph.getPlayfieldWidth();
    
    private PlayfieldPoint src = null;
	private PlayfieldPoint dest = null;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
    public void shouldBounceBallFromCenterOfGateway_1() throws Exception {
        // given
	    src = new PlayfieldPoint(5, 1);
        dest = new PlayfieldPoint(5, 0);

        // when
	    boolean canBounce = movementGraph.canBounceBallFrom(src, dest);

        // then
        assertFalse(canBounce);
    }

    @Test
    public void shouldBounceBallFromCenterOfGateway_2() throws Exception {
        // given
        src = new PlayfieldPoint(5, CELL_HEIGHT-1);
        dest = new PlayfieldPoint(5, CELL_HEIGHT);

        // when
        boolean canBounce = movementGraph.canBounceBallFrom(src, dest);

        // then
        assertFalse(canBounce);
    }
	
    @Test
	public void canBounceFromGatewayPost() throws Exception {
		// given
	    movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/bounceFromGatewayPost.dat"); 
    	
    	src = new PlayfieldPoint(7, 16);
    	dest = new PlayfieldPoint(6, 16);
    	
    	// when
	    boolean canBounce = movementGraph.canBounceBallFrom(src, dest);	    
    	
    	// then
	    assertTrue(canBounce);
	}
    
	@Test
	public void isPointNeighbours() {
		// given
		PlayfieldPoint ball = new PlayfieldPoint(5, 5);
		List<PlayfieldPoint> points = new ArrayList<PlayfieldPoint>();
		points.add(new PlayfieldPoint(5, 4));
		points.add(new PlayfieldPoint(5, 6));
		points.add(new PlayfieldPoint(4, 5));
		points.add(new PlayfieldPoint(6, 5));
		points.add(new PlayfieldPoint(4, 4));
		points.add(new PlayfieldPoint(4, 6));
		points.add(new PlayfieldPoint(6, 4));
		points.add(new PlayfieldPoint(6, 6));
		
		for (PlayfieldPoint p : points) {
			// when
			boolean neighbour = movementGraph.isPointNeighbours(ball, p);
			// then
			assertTrue(neighbour);
		}
	}

	@Test
	public void pointIsNotNeighbour() throws Exception {
		// given
		PlayfieldPoint ball = new PlayfieldPoint(5, 5);
		List<PlayfieldPoint> points = new ArrayList<PlayfieldPoint>();
		points.add(new PlayfieldPoint(5, 7));
		points.add(new PlayfieldPoint(5, 3));
		points.add(new PlayfieldPoint(7, 5));
		points.add(new PlayfieldPoint(3, 5));

		// up
		points.add(new PlayfieldPoint(4, 3));
		points.add(new PlayfieldPoint(6, 3));
		// right
		points.add(new PlayfieldPoint(7, 4));
		points.add(new PlayfieldPoint(7, 6));
		// left
		points.add(new PlayfieldPoint(3, 4));
		points.add(new PlayfieldPoint(3, 6));
		// down
		points.add(new PlayfieldPoint(4, 7));
		points.add(new PlayfieldPoint(6, 7));
		
		for (PlayfieldPoint p : points) {
			// when
			boolean neighbour = movementGraph.isPointNeighbours(ball, p);
			// then
			assertFalse(neighbour);
		}
	}
	
	@Test
    public void canDeterminePointOnBound() throws Exception {
        // given
        List<PlayfieldPoint> points = new ArrayList<PlayfieldPoint>();
        points.add(new PlayfieldPoint(0, 1));
        points.add(new PlayfieldPoint(1, 0));
        points.add(new PlayfieldPoint(CELL_WIDTH, CELL_HEIGHT-1));
        points.add(new PlayfieldPoint(CELL_WIDTH-1, CELL_HEIGHT));
        
        for (PlayfieldPoint p : points) {
            // when
    	    boolean pointOnBound = movementGraph.isPointOnBound(p);
            // then
    	    assertTrue(pointOnBound);
        }
    }

	@Test
    public void canDeterminePointNotOnBound() throws Exception {
        // given
        List<PlayfieldPoint> points = new ArrayList<PlayfieldPoint>();
        points.add(new PlayfieldPoint(1, 1));
        points.add(new PlayfieldPoint(CELL_WIDTH-1, 1));
        points.add(new PlayfieldPoint(1, CELL_HEIGHT-1));
        points.add(new PlayfieldPoint(CELL_WIDTH-1, CELL_HEIGHT-1));

        for (PlayfieldPoint p : points) {
            // when
            boolean pointOnBound = movementGraph.isPointOnBound(p);
            // then
            assertFalse(pointOnBound);
        }
    }

	@Test
    public void canPaintGraph() {
        // given

	    // on begining ball is in the middle of playfield (5,8)
//	    movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(5, 7)); // north
//        movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(6, 6)); // north-east
//        movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(6, 7)); // south
	    
        // when
//	    movementGraph.paint(graphics, playfieldDrawer);
//
//	    ArgumentCaptor<PlayfieldPoint> srcPointCaptor = ArgumentCaptor.forClass(PlayfieldPoint.class);
//        ArgumentCaptor<PlayfieldPoint> destPointCaptor = ArgumentCaptor.forClass(PlayfieldPoint.class);
//        
//        // then
//	    verify(playfieldDrawer, atLeastOnce()).drawPlayLine(any(Graphics.class), srcPointCaptor.capture(), destPointCaptor.capture(), any(Player.class));
//        verify(playfieldDrawer, atLeastOnce()).drawPlayLine(any(Graphics.class), srcPointCaptor.capture(), destPointCaptor.capture(), any(Player.class));
//        
//        for (PlayfieldPoint p : srcPointCaptor.getAllValues()) {
//            System.out.println("p = " + p);
//        }
    }
	
	@Test
    public void canGenerateWeightsForPlayerOne() throws Exception {
        // given
	    
        // when
	    WeightMap weightMap = movementGraph.generateWeightMap(Player.One);

        // then
	    LinkedList<PlayfieldPoint> playerGateway = movementGraph.getPlayerOpponentGateway(Player.One);
	    for (PlayfieldPoint p : playerGateway) {
	        assertEquals(0, weightMap.getVal(p));
	    }
        assertEquals(8, weightMap.getVal(new PlayfieldPoint(5, 8)));
    }
	
    @Test
    public void canGenerateWeightsForPlayerTwo() throws Exception {
        // given
        
        // when
        WeightMap weightMap = movementGraph.generateWeightMap(Player.Two);

        // then
        LinkedList<PlayfieldPoint> playerGateway = movementGraph.getPlayerOpponentGateway(Player.Two);
        for (PlayfieldPoint p : playerGateway) {
            assertEquals(0, weightMap.getVal(p));
        }
        
        assertEquals(8, weightMap.getVal(new PlayfieldPoint(5, 8)));
    }
/*	
    @Test(timeout=2000)
	public void canGetTheBestMovementPath() throws Exception {
		// given
    	PlayfieldPoint src ;
    	PlayfieldPoint dest ;

    	src = new PlayfieldPoint(5, 8);
    	dest = new PlayfieldPoint(5, 7);
    	movementGraph.getNode(dest).addPathPointReference(new PlayfieldPoint(src), false);
    	
    	src = new PlayfieldPoint(5, 7);
    	dest = new PlayfieldPoint(4, 8);
    	movementGraph.getNode(dest).addPathPointReference(new PlayfieldPoint(src), false);

    	src = new PlayfieldPoint(4, 8);
    	dest = new PlayfieldPoint(5, 8);
    	movementGraph.getNode(dest).addPathPointReference(new PlayfieldPoint(src), false);

    	src = new PlayfieldPoint(5, 8);
    	dest = new PlayfieldPoint(6, 7);
    	movementGraph.getNode(dest).addPathPointReference(new PlayfieldPoint(src), false);
    	
    	movementGraph.updateTheBestMove(dest, src, 7, 2);
    	
		// when
    	MovementPath theBestPath = movementGraph.getTheBestMovementPath();
    	
    	System.out.println(theBestPath);
    	
		// then
    	List<PlayfieldPoint> path = theBestPath.getPath();
    	assertTrue(path.get(0).equalCords(5, 8));
    	assertTrue(path.get(1).equalCords(5, 7));
    	assertTrue(path.get(2).equalCords(4, 8));
    	assertTrue(path.get(3).equalCords(5, 8));
    	assertTrue(path.get(4).equalCords(6, 7));
	}
*/    
}
