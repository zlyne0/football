package promitech.football.engine;

public enum DifficultyLevel {
	
	TRIVIAL(-1, false),
	EASY(0, true),
	MEDIUM(1, true);
	
	private final int predicOpponentMovesCount;
	private final boolean sumPathLengthWeight;

	private DifficultyLevel(int predicOpponentMovesCount, boolean sumPathLengthWeight) {
		this.predicOpponentMovesCount = predicOpponentMovesCount;
		this.sumPathLengthWeight = sumPathLengthWeight;
	}

	public int getPredictOpponentMovesCount() {
		return predicOpponentMovesCount;
	}

	public boolean isSumPathLengthWeight() {
		return sumPathLengthWeight;
	}
	
}
