package promitech.football;

import java.util.List;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.MovementPath;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.engine.WeightMap;





public class PlayfieldDrawableObjects {
    PlayfieldPoint gridCursorPoint = null;
    MovementGraph movementGraph;
    List<MovementPath> hintMovements;
    WeightMap oneWeightMap;
    WeightMap twoWeightMap;
	MovementGraph possibleMovements;    
}
