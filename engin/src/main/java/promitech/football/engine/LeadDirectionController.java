package promitech.football.engine;

import java.util.HashSet;
import java.util.Set;




public class LeadDirectionController {
	private static final int MAX_LEAD_PATH_LENGTH = 100;
	private Object[] leadPath = new Object[MAX_LEAD_PATH_LENGTH];

	// private List<Set<MoveDirection>> leadPath = new
	// ArrayList<Set<MoveDirection>>(MAX_LEAD_PATH_LENGTH);

	public LeadDirectionController() {
		for (int i = 0; i < leadPath.length; i++) {
			leadPath[i] = null;
		}
		
		init();
	}


	public void addLeadMoveDirection(int moveIndex, MoveDirection moveDirection) {
		if (leadPath[moveIndex] == null) {
			leadPath[moveIndex] = new HashSet<MoveDirection>();
		}
		((HashSet<MoveDirection>) leadPath[moveIndex]).add(moveDirection);
	}

	public boolean canLeadDirection(int moveIndex, MoveDirection moveDirection) {
		Set<MoveDirection> set = (Set<MoveDirection>) leadPath[moveIndex];
		return set == null || set.contains(moveDirection);
	}
	
	private void init() {
		addLeadMoveDirection(1, MoveDirection.SOUTH_WEST);
		addLeadMoveDirection(2, MoveDirection.EAST);
		addLeadMoveDirection(3, MoveDirection.SOUTH_WEST);
		addLeadMoveDirection(4, MoveDirection.SOUTH_EAST);
		addLeadMoveDirection(5, MoveDirection.EAST);
		addLeadMoveDirection(6, MoveDirection.NORTH_EAST);
		addLeadMoveDirection(7, MoveDirection.SOUTH_EAST);
		
//		// should
//		addLeadMoveDirection(1, MoveDirection.SOUTH_WEST);
//		addLeadMoveDirection(2, MoveDirection.WEST);
//		// end
		
        // begin graphCase2.dat
	    // should
//	    addLeadMoveDirection(1, MoveDirection.EAST);
//        addLeadMoveDirection(2, MoveDirection.NORTH);
//        addLeadMoveDirection(3, MoveDirection.NORTH);
        
//        addLeadMoveDirection(1, MoveDirection.NORTH_WEST);
//        addLeadMoveDirection(2, MoveDirection.NORTH_WEST);
//        addLeadMoveDirection(3, MoveDirection.EAST);
        
//        addLeadMoveDirection(1, MoveDirection.SOUTH_EAST);
//        addLeadMoveDirection(2, MoveDirection.NORTH);
//        addLeadMoveDirection(3, MoveDirection.NORTH);
        
	    // end graphCase2.dat
        
        // begin /graphs/graphCase5.dat
//        addLeadMoveDirection(1, MoveDirection.NORTH_EAST);
//        addLeadMoveDirection(1, MoveDirection.WEST);
//        addLeadMoveDirection(4, MoveDirection.SOUTH);
//        addLeadMoveDirection(1, MoveDirection.WEST);
        
        // end /graphs/graphCase5.dat
        
//	    addLeadMoveDirection(1, MoveDirection.NORTH_WEST);
//        addLeadMoveDirection(1, MoveDirection.NORTH);
//        addLeadMoveDirection(2, MoveDirection.SOUTH);
//        addLeadMoveDirection(2, MoveDirection.SOUTH_EAST);
//        addLeadMoveDirection(3, MoveDirection.SOUTH_EAST);
//        addLeadMoveDirection(2, MoveDirection.NORTH);
        //addLeadMoveDirection(2, MoveDirection.NORTH_EAST);
	    
//      leadDirectionController.addLeadMoveDirection(1, MoveDirection.NORTH);
//      leadDirectionController.addLeadMoveDirection(2, MoveDirection.SOUTH_EAST);
//      leadDirectionController.addLeadMoveDirection(3, MoveDirection.SOUTH_EAST);
      
//      addLeadMoveDirection(1, MoveDirection.NORTH_WEST);
//      addLeadMoveDirection(2, MoveDirection.SOUTH);
//      addLeadMoveDirection(3, MoveDirection.NORTH_WEST);
      
      // secound configuration
//      leadDirectionController.addLeadMoveDirection(1, MoveDirection.SOUTH);
//      leadDirectionController.addLeadMoveDirection(2, MoveDirection.NORTH_WEST);
//      leadDirectionController.addLeadMoveDirection(3, MoveDirection.EAST);
//      leadDirectionController.addLeadMoveDirection(4, MoveDirection.NORTH_WEST);
//      
//      leadDirectionController.addLeadMoveDirection(1, MoveDirection.SOUTH_EAST);
//      leadDirectionController.addLeadMoveDirection(2, MoveDirection.NORTH);
		
	}
}
