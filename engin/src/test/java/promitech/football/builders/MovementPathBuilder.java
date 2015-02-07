package promitech.football.builders;

import promitech.football.engine.MovementPath;
import promitech.football.engine.PlayfieldPoint;

public class MovementPathBuilder {

	private int weight = Integer.MAX_VALUE;
    private boolean blocked = false;
	
	public static MovementPathBuilder create() {
		return new MovementPathBuilder();
	}
	
	public MovementPathBuilder blocked() {
		this.blocked = true;
		return this;
	}
	
	public MovementPathBuilder withWeight(int weight) {
		this.weight = weight;
		return this;
	}
	
	public MovementPath build() {
		MovementPath mp = MovementPath.instanceWithSinglePoint(new PlayfieldPoint());
		mp.setBlocked(blocked);
		//mp.setWeight(weight);
		
		return mp;
	}
	
}
