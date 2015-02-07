package promitech.football.engine;

public interface PlayfieldDrawer {

	void drawPlayLine(PlayfieldPoint fromPoint, PlayfieldPoint toPoint, Player player);

	void drawBall(PlayfieldPoint ball, Player playerTurn);

}
