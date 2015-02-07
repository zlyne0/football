package promitech.football.activities;

import promitech.football.R;
import promitech.football.engine.DifficultyLevel;
import promitech.football.services.GameConfigurationService;
import promitech.football.services.dto.GameSettingsDto;
import promitech.football.services.dto.PlayerType;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class NewGameSettingsActivity extends Activity {

	private GameConfigurationService gameConfService;
	
	private RadioButton playerOneTypeComputerRadioButton;
	private RadioButton playerOneTypeHumanRadioButton;
	private RadioButton playerTwoTypeComputerRadioButton;
	private RadioButton playerTwoTypeHumanRadioButton;
	
	private boolean oneComputerPlayerAllowedMessageShown = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gameConfService = new GameConfigurationService(getApplicationContext());
		
		setContentView(R.layout.activity_new_game_settings);
		
		playerOneTypeComputerRadioButton = (RadioButton)findViewById(R.id.playerOneTypeRadio0);
		playerOneTypeHumanRadioButton = (RadioButton)findViewById(R.id.playerOneTypeRadio1);
		playerTwoTypeComputerRadioButton = (RadioButton)findViewById(R.id.playerTwoTypeRadio0);
		playerTwoTypeHumanRadioButton = (RadioButton)findViewById(R.id.playerTwoTypeRadio1);
		
		playerOneTypeComputerRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isTwoComputerPlayers()) {
					playerTwoTypeComputerRadioButton.setChecked(false);
					playerTwoTypeHumanRadioButton.setChecked(true);
					showOneComputerPlayerAllowedMessage();
				}
			}
		});
		
		playerTwoTypeComputerRadioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isTwoComputerPlayers()) {
					playerOneTypeComputerRadioButton.setChecked(false);
					playerOneTypeHumanRadioButton.setChecked(true);
					showOneComputerPlayerAllowedMessage();
				}
			}
		});
		
		Button startGameButton = (Button)findViewById(R.id.startPlayButton);
		startGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPlayfieldActivity();
			}
		});
		
		GameSettingsDto gameSettings = gameConfService.loadGameSettings();
		populateWithDefaultValues(gameSettings);		
		
		EditText oneEditText = (EditText)findViewById(R.id.playerOneNickEditText);
		EditText twoEditText = (EditText)findViewById(R.id.playerTwoNickEditText);
		oneEditText.setText(gameSettings.getPlayerOneNick());
		twoEditText.setText(gameSettings.getPlayerTwoNick());
		
		if (gameSettings.getPlayerOneType().equals(PlayerType.AI)) {
			playerOneTypeComputerRadioButton.setChecked(true);
			playerOneTypeHumanRadioButton.setChecked(false);
		} else {
			playerOneTypeComputerRadioButton.setChecked(false);
			playerOneTypeHumanRadioButton.setChecked(true);
		}
		if (gameSettings.getPlayerTwoType().equals(PlayerType.AI)) {
			playerTwoTypeComputerRadioButton.setChecked(true);
			playerTwoTypeHumanRadioButton.setChecked(false);
		} else {
			playerTwoTypeComputerRadioButton.setChecked(false);
			playerTwoTypeHumanRadioButton.setChecked(true);
		}
		
		createDifficultyLevelSpinner(gameSettings);
	}

	private void createDifficultyLevelSpinner(GameSettingsDto gameSettings) {
		Spinner difficultyLevelSpinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.DifficultyLevelNames, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		difficultyLevelSpinner.setAdapter(adapter);
		difficultyLevelSpinner.setSelection(gameSettings.getDifficultyLevel().ordinal());
	}
	
	private void populateWithDefaultValues(GameSettingsDto settings) {
		settings.setIfBlank(getString(R.string.playerOneNickHint), getString(R.string.playerTwoNickHint));
		if (settings.getPlayerOneType() == null) {
			settings.setPlayerOneType(PlayerType.HUMAN);
		}
		if (settings.getPlayerTwoType() == null) {
			settings.setPlayerTwoType(PlayerType.AI);
		}
	}
	
	private boolean isTwoComputerPlayers() {
		return playerOneTypeComputerRadioButton.isChecked() && playerTwoTypeComputerRadioButton.isChecked(); 
	}
	
	private void showOneComputerPlayerAllowedMessage() {
		if (oneComputerPlayerAllowedMessageShown) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(NewGameSettingsActivity.this);
		builder.setMessage(getString(R.string.oneComputerPlayerAllowedMessage));
		builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
		oneComputerPlayerAllowedMessageShown = true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void startPlayfieldActivity() {
		EditText oneEditText = (EditText)findViewById(R.id.playerOneNickEditText);
		EditText twoEditText = (EditText)findViewById(R.id.playerTwoNickEditText);
		
		GameSettingsDto settings = new GameSettingsDto();
		settings.setPlayerOneNick(oneEditText.getText().toString());
		settings.setPlayerTwoNick(twoEditText.getText().toString());
		
		populateWithDefaultValues(settings);
		
		if (playerOneTypeComputerRadioButton.isChecked()) {
			settings.setPlayerOneType(PlayerType.AI);
		} else {
			settings.setPlayerOneType(PlayerType.HUMAN);
		}
		if (playerTwoTypeComputerRadioButton.isChecked()) {
			settings.setPlayerTwoType(PlayerType.AI);
		} else {
			settings.setPlayerTwoType(PlayerType.HUMAN);
		}
		
		Spinner difficultyLevelSpinner = (Spinner) findViewById(R.id.spinner1);
		DifficultyLevel difficultyLevel = DifficultyLevel.values()[difficultyLevelSpinner.getSelectedItemPosition()];
		settings.setDifficultyLevel(difficultyLevel);
		
		gameConfService.saveGameSettings(settings);
		
		Intent intent = new Intent(NewGameSettingsActivity.this, PlayfieldActivity.class);
		startActivity(intent);
		finish();
	}
}
