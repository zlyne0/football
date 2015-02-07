package promitech.football;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import promitech.football.engine.MoveDirection;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldGraphNode;



public class PlayfieldGraphNodeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canCreateIdenticalCopy() throws Exception {
        // given
        PlayfieldGraphNode node = new PlayfieldGraphNode();
        for (MoveDirection md : new MoveDirection[] { 
        		MoveDirection.NORTH_EAST, 
        		MoveDirection.NORTH_WEST, 
        		MoveDirection.SOUTH_EAST, 
        		MoveDirection.SOUTH_WEST  
        		} 
        ) {
            node.makeConnection(md, Player.One);
        }
        node.makeConnection(MoveDirection.NORTH, Player.Two);
        node.makeConnection(MoveDirection.EAST, Player.Two);
        node.makeConnection(MoveDirection.SOUTH, Player.Two);
        node.makeConnection(MoveDirection.WEST, Player.Two);

        // when
        PlayfieldGraphNode copy = new PlayfieldGraphNode(node);

        // then
        assertEquals(node.hasConnectionsToNeighbours(), copy.hasConnectionsToNeighbours());
        for (MoveDirection md : MoveDirection.values()) {
            assertEquals(node.getConnection(md), copy.getConnection(md));
        }
    }

    @Test
    public void canDeterminaWhetherAllWaysBlocked() {
    	// given
        PlayfieldGraphNode node = new PlayfieldGraphNode();
        for (MoveDirection md : new MoveDirection[] { 
        		MoveDirection.NORTH_EAST, 
        		MoveDirection.NORTH_WEST, 
        		MoveDirection.SOUTH_EAST, 
        		MoveDirection.SOUTH_WEST  
        		} 
        ) {
            node.makeConnection(md, Player.One);
        }
        node.makeConnection(MoveDirection.NORTH, Player.Two);
        node.makeConnection(MoveDirection.EAST, Player.Two);
        node.makeConnection(MoveDirection.SOUTH, Player.Two);
        node.makeConnection(MoveDirection.WEST, Player.Two);
//		for (MoveDirection md : MoveDirection.values()) {
//		    node.makeConnection(md, Player.Two);
//		}
    	
    	// when
        boolean allWaysBlocked = node.areAllWaysBlocked();
    	
    	// then
        assertTrue(allWaysBlocked);
    }

    @Test
    public void canDeterminaWhetherAllWaysBlocked_2() {
    	// given
        PlayfieldGraphNode node = new PlayfieldGraphNode();
		for (MoveDirection md : MoveDirection.values()) {
			node.makeConnection(md, Player.Two);
		}
	
		// when
	    boolean allWaysBlocked = node.areAllWaysBlocked();
		
		// then
	    assertTrue(allWaysBlocked);
    }
    
    @Test
	public void notAllWaysBlocked() throws Exception {
		// given
        PlayfieldGraphNode node = new PlayfieldGraphNode();
        node.makeConnection(MoveDirection.NORTH, Player.One);
        node.makeConnection(MoveDirection.EAST, Player.One);
        node.makeConnection(MoveDirection.SOUTH, Player.Two);
        node.makeConnection(MoveDirection.WEST, Player.Two);
    	
    	// when
        boolean allWaysBlocked = node.areAllWaysBlocked();
    	
    	// then
        assertFalse(allWaysBlocked);
	}
}
