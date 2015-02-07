package promitech.football.services.dto;

import promitech.football.engine.DifficultyLevel;
import promitech.football.engine.Player;

public class GameSettingsDto {
	private String oneNick;
	private String twoNick;
	private PlayerType playerOneType;
	private PlayerType playerTwoType;
	private DifficultyLevel difficultyLevel;

	public void setIfBlank(String nick1, String nick2) {
		if (oneNick == null || oneNick.trim().length() == 0) {
			oneNick = nick1;
		}
		if (twoNick == null || twoNick.trim().length() == 0) {
			twoNick = nick2;
		}
	}

	public String getPlayerNick(Player player) {
		if (Player.One.equals(player)) {
			return oneNick;
		} else {
			return twoNick;
		}
	}
	
	public boolean isAiMove(Player playerTurn) {
		if (Player.One.equals(playerTurn)
				&& playerOneType.equals(PlayerType.AI)) {
			return true;
		}
		if (Player.Two.equals(playerTurn)
				&& playerTwoType.equals(PlayerType.AI)) {
			return true;
		}
		return false;
	}

	public String getPlayerOneNick() {
		return oneNick;
	}

	public void setPlayerOneNick(String playerOneNick) {
		this.oneNick = playerOneNick;
	}

	public String getPlayerTwoNick() {
		return twoNick;
	}

	public void setPlayerTwoNick(String playerTwoNick) {
		this.twoNick = playerTwoNick;
	}

	public PlayerType getPlayerOneType() {
		return playerOneType;
	}

	public void setPlayerOneType(PlayerType playerOneType) {
		this.playerOneType = playerOneType;
	}

	public PlayerType getPlayerTwoType() {
		return playerTwoType;
	}

	public void setPlayerTwoType(PlayerType playerTwoType) {
		this.playerTwoType = playerTwoType;
	}

	public DifficultyLevel getDifficultyLevel() {
		if (difficultyLevel == null) {
			return DifficultyLevel.MEDIUM;
		}
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

}
