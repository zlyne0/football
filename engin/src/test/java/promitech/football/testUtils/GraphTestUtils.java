package promitech.football.testUtils;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.PlayfieldPoint;

public class GraphTestUtils {
	public static void markLine(MovementGraph graph, int x1, int y1, int x2, int y2) {
		PlayfieldPoint src = new PlayfieldPoint(x1, y1);
		PlayfieldPoint dest = new PlayfieldPoint(x2, y2);
		graph.markLine(src, dest);
	}
}
