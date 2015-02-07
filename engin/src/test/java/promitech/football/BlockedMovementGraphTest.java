package promitech.football;

import org.junit.Test;
import static org.junit.Assert.*;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.testUtils.GraphTestUtils;

public class BlockedMovementGraphTest {
    private MovementGraph movementGraph = new MovementGraph();
    private int CELL_HEIGHT = movementGraph.getPlayfieldHeight();
    private int CELL_WIDTH = movementGraph.getPlayfieldWidth();
    
    private PlayfieldPoint src = null;
	private PlayfieldPoint dest = null;
	
	@Test
	public void canDetermineWhetherBlockedInNorthWestCorner() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, 0, 0, 0, 1);
        GraphTestUtils.markLine(movementGraph, 0, 0, 1, 0);
        
        src = new PlayfieldPoint(1, 1);
        dest = new PlayfieldPoint(0, 0);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}
	
	@Test
	public void canDetermineWhetherBlockedInNorthEastCorner() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 0, CELL_WIDTH-1, 0);
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 0, CELL_WIDTH, 1);
        
        src = new PlayfieldPoint(CELL_WIDTH-1, 1);
        dest = new PlayfieldPoint(CELL_WIDTH, 0);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}
	
	@Test
	public void canDetermineWhetherBlockedInSouthEastCorner() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT-1);
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, CELL_HEIGHT, CELL_WIDTH-1, CELL_HEIGHT);
        
        src = new PlayfieldPoint(CELL_WIDTH-1, CELL_HEIGHT-1);
        dest = new PlayfieldPoint(CELL_WIDTH, CELL_HEIGHT);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}

	@Test
	public void canDetermineWhetherBlockedInSouthWestCorner() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, 0, CELL_HEIGHT, 0, CELL_HEIGHT-1);
        GraphTestUtils.markLine(movementGraph, 0, CELL_HEIGHT, 1, CELL_HEIGHT);
        
        src = new PlayfieldPoint(1, CELL_HEIGHT-1);
        dest = new PlayfieldPoint(0, CELL_HEIGHT);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}

	@Test
	public void canDetermineWhetherBlockedInWestBand() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, 0, 8, 0, 7);
        GraphTestUtils.markLine(movementGraph, 0, 8, 1, 7);
        GraphTestUtils.markLine(movementGraph, 0, 8, 0, 9);
        GraphTestUtils.markLine(movementGraph, 0, 8, 1, 9);
        
        src = new PlayfieldPoint(1, 8);
        dest = new PlayfieldPoint(0, 8);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}

	@Test
	public void canDetermineWhetherBlockedInSouthBand() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 8, CELL_WIDTH, 7);
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 8, CELL_WIDTH-1, 7);
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 8, CELL_WIDTH, 9);
        GraphTestUtils.markLine(movementGraph, CELL_WIDTH, 8, CELL_WIDTH-1, 9);
        
        src = new PlayfieldPoint(CELL_WIDTH-1, 8);
        dest = new PlayfieldPoint(CELL_WIDTH, 8);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}
	
	@Test
	public void whetherBlockedInNorthBand() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, 3, 0, 2, 0);
        GraphTestUtils.markLine(movementGraph, 3, 0, 2, 1);
        GraphTestUtils.markLine(movementGraph, 3, 0, 4, 0);
        GraphTestUtils.markLine(movementGraph, 3, 0, 4, 1);
        
        src = new PlayfieldPoint(3, 1);
        dest = new PlayfieldPoint(3, 0);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}

	@Test
	public void whetherBlockedInSouthBand() throws Exception {
		// given
        GraphTestUtils.markLine(movementGraph, 3, CELL_HEIGHT, 2, CELL_HEIGHT);
        GraphTestUtils.markLine(movementGraph, 3, CELL_HEIGHT, 2, CELL_HEIGHT-1);
        GraphTestUtils.markLine(movementGraph, 3, CELL_HEIGHT, 4, CELL_HEIGHT);
        GraphTestUtils.markLine(movementGraph, 3, CELL_HEIGHT, 4, CELL_HEIGHT-1);
        
        src = new PlayfieldPoint(3, CELL_HEIGHT-1);
        dest = new PlayfieldPoint(3, CELL_HEIGHT);
		
		// when
		boolean blocked = movementGraph.willBlocked(src, dest);
		
		// then
		assertTrue(blocked);
	}
	
}
