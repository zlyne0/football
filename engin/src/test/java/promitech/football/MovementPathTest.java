package promitech.football;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import promitech.football.engine.MovementPath;
import promitech.football.engine.PlayfieldPoint;




public class MovementPathTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void canPrint() {
        // given
        MovementPath mp;
        mp = MovementPath.instanceWithSinglePoint( new PlayfieldPoint(6, 4) );
        mp.add(new PlayfieldPoint(5, 5));
        
        mp = MovementPath.instanceWithReferenceToPath(mp, new PlayfieldPoint(5, 5));
        mp.add(new PlayfieldPoint(5, 4));

        mp = MovementPath.instanceWithReferenceToPath(mp, new PlayfieldPoint(5, 4));
        mp.add(new PlayfieldPoint(6, 3));
        mp.add(new PlayfieldPoint(6, 4));
        mp.add(new PlayfieldPoint(5, 4));
        mp.add(new PlayfieldPoint(4, 5));
        
        // when
        System.out.println(mp.toString());
        // then
        
        LinkedList<MovementPath> mpList = new LinkedList<MovementPath>();
        {
            MovementPath mp1 = mp;
            while (mp1.getNextPlayerMovePath() != null) {
                mp1 = mp1.getNextPlayerMovePath();
                mpList.addFirst(mp1);
            }
            mpList.addLast(mp);
        }
        System.out.println("mpList = \r\n");
        for (MovementPath m : mpList) {
            System.out.println(m.toStringWithoutRecursion());
        }
    }
}
