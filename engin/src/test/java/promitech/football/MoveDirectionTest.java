package promitech.football;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import promitech.football.engine.MoveDirection;
import promitech.football.engine.PlayfieldPoint;




public class MoveDirectionTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canCreateDirection() throws Exception {
        // given
        final int ix = 10, iy = 10; 
        PlayfieldPoint tmpWorkingPoint = new PlayfieldPoint();
        PlayfieldPoint src = new PlayfieldPoint(ix, iy);
        
        Map<MoveDirection,byte[]> results = new HashMap<MoveDirection, byte[]>();
        results.put(MoveDirection.NORTH_WEST, new byte[] { ix-1, iy-1 } );
        results.put(MoveDirection.NORTH, new byte[] { ix, iy-1 } );
        results.put(MoveDirection.NORTH_EAST, new byte[] { ix+1, iy-1 } );
        results.put(MoveDirection.EAST, new byte[] { ix+1, iy } );
        results.put(MoveDirection.SOUTH_EAST, new byte[] { ix+1, iy+1 } );
        results.put(MoveDirection.SOUTH, new byte[] { ix, iy+1 } );
        results.put(MoveDirection.SOUTH_WEST, new byte[] { ix-1, iy+1 } );
        results.put(MoveDirection.WEST, new byte[] { ix-1, iy } );
        
        for (Entry<MoveDirection, byte[]> entry : results.entrySet()) {
            PlayfieldPoint dest = new PlayfieldPoint(entry.getValue()[0], entry.getValue()[1]);
            // when
            MoveDirection createdDirection = MoveDirection.createDirection(src, dest, tmpWorkingPoint);
            // then
            assertEquals(entry.getKey(), createdDirection);
        }
    }

    @Test
    public void throwExceptionWhenPointsAreNotNeighbours() throws Exception {
        // given
        PlayfieldPoint tmpWorkingPoint = new PlayfieldPoint();
        PlayfieldPoint src = new PlayfieldPoint(10, 10);
        PlayfieldPoint dest = new PlayfieldPoint(10, 12);

        try {
            // when
            MoveDirection.createDirection(src, dest, tmpWorkingPoint);
            fail("should throw exception");
        } catch (IllegalStateException e) {
            // then
            assertTrue(true);
        }
    }

    @Test
    public void throwExceptionWhenTheSamePointCordinates() throws Exception {
        // given
        PlayfieldPoint tmpWorkingPoint = new PlayfieldPoint();
        PlayfieldPoint src = new PlayfieldPoint(10, 10);
        PlayfieldPoint dest = new PlayfieldPoint(10, 10);

        try {
            // when
            MoveDirection.createDirection(src, dest, tmpWorkingPoint);
            fail("should throw exception");
        } catch (IllegalStateException e) {
            // then
            assertTrue(true);
        }
    }
    
}
