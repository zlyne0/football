package promitech.football.engine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import promitech.football.files.MovementGraphFileOperator;

public class MovementPathFinder6Test {
    @Rule
    public Timeout globalTimeout = new Timeout(10 * 1000);

	@Before
	public void setUp() throws Exception {
	}

	DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;
	
	@Test
	public void canFindPath_case1() throws Exception {
		// given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase1.dat");
		
		MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
		
		// when
		MovementPath path = finder.generateTheBestMovement();
		
		// then
		assertNotNull(path);
		assertEquals(2, path.getPath().size());
		assertTrue(path.getPath().get(0).equalCords(6, 4));
		assertTrue(path.getPath().get(1).equalCords(5, 5));
	}

    @Test
	public void canFindPath_case2() throws Exception {
		// given
		
		MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase2.dat");
		MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
		
		// when
		MovementPath path = finder.generateTheBestMovement();
		
		// then
		assertNotNull(path);
		assertEquals(4, path.getPath().size());
		assertTrue(path.getPath().get(0).equalCords(9, 11));
		assertTrue(path.getPath().get(1).equalCords(10, 11));
		assertTrue(path.getPath().get(2).equalCords(10, 10));
		assertTrue(path.getPath().get(3).equalCords(10, 9));
	}

    @Test
	public void canFindPath_case3() throws Exception {
		// given
		
		MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase3.dat");
		MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
		
		// when
		MovementPath path = finder.generateTheBestMovement();
		
		// then
		assertNotNull(path);
		assertEquals(3, path.getPath().size());
		assertTrue(path.getPath().get(0).equalCords(4, 8));
		assertTrue(path.getPath().get(1).equalCords(5, 8));
		assertTrue(path.getPath().get(2).equalCords(6, 7));
	}

    @Test
	public void canFindPath_case4() throws Exception {
		// given
		
		MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase4.dat");
		MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
		
		// when
		MovementPath path = finder.generateTheBestMovement();
		
		// then
		assertNotNull(path);
		assertEquals(2, path.getPath().size());
		assertTrue(path.getPath().get(0).equalCords(6, 7));
		assertTrue(path.getPath().get(1).equalCords(7, 8));
	}

    @Test
    public void canFindPath_case5() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase5.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertEquals(6, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(9,2));
        assertTrue(path.getPath().get(1).equalCords(10,1));
        assertTrue(path.getPath().get(2).equalCords(10,2));
        assertTrue(path.getPath().get(3).equalCords(9,2));
        assertTrue(path.getPath().get(4).equalCords(9,3));
        assertTrue(path.getPath().get(5).equalCords(8,2));
    }

    @Test
    public void canFindPath_case6() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphCase6.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertEquals(2, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(1,4));
        assertTrue(path.getPath().get(1).equalCords(2,4));
    }

    @Test
    public void canFindPath_case7_goalFromSourcePoint() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/goalFromSourcePoint.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertTrue(path.isScoreAGoal());
        assertEquals(9, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(2,8));
        assertTrue(path.getPath().get(1).equalCords(1,7));
        assertTrue(path.getPath().get(2).equalCords(1,6));
        assertTrue(path.getPath().get(3).equalCords(1,5));
        assertTrue(path.getPath().get(4).equalCords(2,4));
        assertTrue(path.getPath().get(5).equalCords(3,3));
        assertTrue(path.getPath().get(6).equalCords(4,2));
        assertTrue(path.getPath().get(7).equalCords(5,1));
        assertTrue(path.getPath().get(8).equalCords(5,0));
    }

    @Test
    public void canFindPath_case8_blocked() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/blocked.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertEquals(0, path.getPath().size());
        assertTrue(path.isBlocked());
    }
    
    @Test
    public void canFindPath_case9_oneScoreAGoal() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphOneScoreAGoal.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertTrue(path.isScoreAGoal());
        assertEquals(2, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(6,1));
        assertTrue(path.getPath().get(1).equalCords(5,0));
    }

    @Test
    public void canFindPath_case10_twoScoreAGoal() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/graphTwoScoreAGoal.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertTrue(path.isScoreAGoal());
        assertEquals(2, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(5,15));
        assertTrue(path.getPath().get(1).equalCords(4,16));
    }

    @Test
    public void canFindPath_case11_stepAlongBoundWithoutGoal() throws Exception {
        // given
        MovementGraph movementGraph = MovementGraphFileOperator.loadGraphFromResource("/graphs/stepAlongBoundWithoutGoal.dat");
        MovementPathFinder6 finder = new MovementPathFinder6(movementGraph, difficultyLevel);
        
        // when
        MovementPath path = finder.generateTheBestMovement();
        
        // then
        assertNotNull(path);
        assertFalse(path.isScoreAGoal());
        assertEquals(6, path.getPath().size());
        assertTrue(path.getPath().get(0).equalCords(1,13));
        assertTrue(path.getPath().get(1).equalCords(0,14));
        assertTrue(path.getPath().get(2).equalCords(1,14));
        assertTrue(path.getPath().get(3).equalCords(0,15));
        assertTrue(path.getPath().get(4).equalCords(1,16));
        assertTrue(path.getPath().get(5).equalCords(2,16));
    }
    
}
