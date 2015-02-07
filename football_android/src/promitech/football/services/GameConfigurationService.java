package promitech.football.services;

import promitech.football.engine.DifficultyLevel;
import promitech.football.services.dto.GameSettingsDto;
import promitech.football.services.dto.PlayerType;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameConfigurationService {
	
	private final static String DATASET_NAME = "GameConfigurationService";
	private final static String PLAYER_ONE_NICK_KEY = "playerOneNick";
	private final static String PLAYER_TWO_NICK_KEY = "playerTwoNick";
	private final static String PLAYER_ONE_TYPE_KEY = "playerOneType";
	private final static String PLAYER_TWO_TYPE_KEY = "playerTwoType";
	private final static String GAME_DIFFICULTY_LEVEL = "gameDifficultyLevel";
	
	private final Context context;
	
	public GameConfigurationService(Context context) {
		this.context = context;
	}
	
	public void saveGameSettings(GameSettingsDto settings) {
		SharedPreferences sharedPref = context.getSharedPreferences(DATASET_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString(PLAYER_ONE_NICK_KEY, settings.getPlayerOneNick());
		editor.putString(PLAYER_TWO_NICK_KEY, settings.getPlayerTwoNick());
		editor.putString(PLAYER_ONE_TYPE_KEY, settings.getPlayerOneType().name());
		editor.putString(PLAYER_TWO_TYPE_KEY, settings.getPlayerTwoType().name());
		editor.putString(GAME_DIFFICULTY_LEVEL, settings.getDifficultyLevel().toString());
		editor.commit();
	}
	
	public GameSettingsDto loadGameSettings() {
		GameSettingsDto settings = new GameSettingsDto();
		SharedPreferences sharedPref = context.getSharedPreferences(DATASET_NAME, Context.MODE_PRIVATE);
		if (sharedPref == null) {
			return settings;
		}
		settings.setPlayerOneNick(sharedPref.getString(PLAYER_ONE_NICK_KEY, null));
		settings.setPlayerTwoNick(sharedPref.getString(PLAYER_TWO_NICK_KEY, null));
		
		String t1 = sharedPref.getString(PLAYER_ONE_TYPE_KEY, null);
		String t2 = sharedPref.getString(PLAYER_TWO_TYPE_KEY, null);
		if (t1 != null && t2 != null) {
			settings.setPlayerOneType(PlayerType.valueOf(t1));
			settings.setPlayerTwoType(PlayerType.valueOf(t2));
		}
		
		String difficultyStr = sharedPref.getString(GAME_DIFFICULTY_LEVEL, DifficultyLevel.MEDIUM.name());
		settings.setDifficultyLevel(DifficultyLevel.valueOf(difficultyStr));
		return settings;
	}
	
}
