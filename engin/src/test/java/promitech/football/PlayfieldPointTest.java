package promitech.football;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import promitech.football.engine.MoveDirection;
import promitech.football.engine.PlayfieldPoint;




public class PlayfieldPointTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canAddMoveDirectionCordinates() throws Exception {
        final int ix = 10;
        final int iy = 10;
        
        // given
        Map<MoveDirection,byte[]> results = new HashMap<MoveDirection, byte[]>();
        results.put(MoveDirection.NORTH_WEST, new byte[] { ix-1, iy-1 } );
        results.put(MoveDirection.NORTH, new byte[] { ix, iy-1 } );
        results.put(MoveDirection.NORTH_EAST, new byte[] { ix+1, iy-1 } );
        results.put(MoveDirection.EAST, new byte[] { ix+1, iy } );
        results.put(MoveDirection.SOUTH_EAST, new byte[] { ix+1, iy+1 } );
        results.put(MoveDirection.SOUTH, new byte[] { ix, iy+1 } );
        results.put(MoveDirection.SOUTH_WEST, new byte[] { ix-1, iy+1 } );
        results.put(MoveDirection.WEST, new byte[] { ix-1, iy } );

        for(MoveDirection md : MoveDirection.values()) {
            PlayfieldPoint from = new PlayfieldPoint(10, 10);
            // when
            PlayfieldPoint newPoint = from.addAndChange(md);
            // then
            assertTrue("the same reference", newPoint == from);
            assertEquals(results.get(md)[0], from.x);
            assertEquals(results.get(md)[1], from.y);
        }
        
    }

}
