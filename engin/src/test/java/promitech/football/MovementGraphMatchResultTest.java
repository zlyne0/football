package promitech.football;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.MovementPath;
import promitech.football.engine.MovementResult;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.engine.WinReason;
import promitech.football.files.MovementGraphFileOperator;

@RunWith(MockitoJUnitRunner.class)
public class MovementGraphMatchResultTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
    public void playerOneScoreAGoalWhenMoveBall() throws Exception {
        // given
	    MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphOneScoreAGoal.dat"); 
        PlayfieldPoint p = new PlayfieldPoint(6, 0);
        
        // when
	    MovementResult movementResult = movementGraph.moveBallToNeighbourPoint(p);

        // then
	    assertEquals(Player.One, movementResult.winner);
        assertEquals(WinReason.SCORE_A_GOAL, movementResult.winReason);
    }

    @Test
    public void playerOneScoreAGoalWhenMoveViaPath() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphOneScoreAGoal.dat"); 
        MovementPath path = MovementPath.instanceWithSinglePoint(new PlayfieldPoint(6, 1));
        path.add(new PlayfieldPoint(6, 0));
        
        // when
        MovementResult movementResult = movementGraph.moveBallViaPath(path);

        // then
        assertEquals(Player.One, movementResult.winner);
        assertEquals(WinReason.SCORE_A_GOAL, movementResult.winReason);
    }
	
	
	@Test
    public void playerTwoScoreAGoalWhenMoveBall() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphTwoScoreAGoal.dat"); 
        PlayfieldPoint p = new PlayfieldPoint(5, 16);
        
        // when
        MovementResult movementResult = movementGraph.moveBallToNeighbourPoint(p);

        // then
        assertEquals(Player.Two, movementResult.winner);
        assertEquals(WinReason.SCORE_A_GOAL, movementResult.winReason);
    }

    @Test
    public void playerTwoScoreAGoalWhenMoveViaPath() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphTwoScoreAGoal.dat"); 
        MovementPath path = MovementPath.instanceWithSinglePoint(new PlayfieldPoint(5, 15));
        path.add(new PlayfieldPoint(5, 16));
        
        // when
        MovementResult movementResult = movementGraph.moveBallViaPath(path);

        // then
        assertEquals(Player.Two, movementResult.winner);
        assertEquals(WinReason.SCORE_A_GOAL, movementResult.winReason);
    }
	
	
	@Test
    public void playerOneBlockedOnBound() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/oneBlockedOnBound.dat"); 
        MovementPath path = MovementPath.instanceWithSinglePoint(new PlayfieldPoint(8, 4));
        path.add(new PlayfieldPoint(9, 5));
        path.add(new PlayfieldPoint(8, 5));
        path.add(new PlayfieldPoint(9, 6));
        path.add(new PlayfieldPoint(10, 7));

        // when
        MovementResult movementResult = movementGraph.moveBallViaPath(path);

        // then
        assertEquals(Player.Two, movementResult.winner);
        assertEquals(WinReason.OPPONENT_BLOCKED, movementResult.winReason);
    }
	
	@Test
    public void playerTwoBlockedOnBound() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/twoBlockedOnBound.dat"); 
        MovementPath path = MovementPath.instanceWithSinglePoint(new PlayfieldPoint(8, 5));
        path.add(new PlayfieldPoint(9, 6));
        path.add(new PlayfieldPoint(10, 7));

        // when
        MovementResult movementResult = movementGraph.moveBallViaPath(path);

        // then
        assertEquals(Player.One, movementResult.winner);
        assertEquals(WinReason.OPPONENT_BLOCKED, movementResult.winReason);
    }
	
	@Test
    public void canDoubleBounceFromBound() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/doubleBounceFromBound.dat");
        List<MovementResult> results = new ArrayList<>();
        
        // when
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(9, 0)));
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(10, 1)));
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(10, 2)));
        
        // then
        assertTrue(results.get(0).canBounce);
        assertTrue(results.get(1).canBounce);
        assertFalse(results.get(2).canBounce);
    }
	
    @Test
    public void canBounceFromGatewayPost() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/doubleBounceFromBound.dat");
        List<MovementResult> results = new ArrayList<>();
        
        // when
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(7, 0)));
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(6, 0)));
        results.add(movementGraph.moveBallToNeighbourPoint(new PlayfieldPoint(6, 1)));
        
        // then
        assertTrue(results.get(0).canBounce);
        assertTrue(results.get(1).canBounce);
        assertFalse(results.get(2).canBounce);
    }
	
}
