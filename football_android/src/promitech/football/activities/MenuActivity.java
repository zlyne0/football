package promitech.football.activities;

import promitech.football.IntentVars;
import promitech.football.R;
import promitech.football.R.id;
import promitech.football.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		Button resumeButton = (Button)findViewById(R.id.resumeButton);
		
		if (PlayfieldActivity.class.getName().equals(getIntentVar(IntentVars.FROM_ACTIVITY))) {
			resumeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MenuActivity.this.finish();
				}
			});
		} else {
			resumeButton.setVisibility(View.GONE);
		}
		
		
		((Button)findViewById(R.id.newGameButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, NewGameSettingsActivity.class);
				startActivity(intent);
			}
		});
		
		((Button)findViewById(R.id.quitButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});

		((Button)findViewById(R.id.gameRulesButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, DescriptionActivity.class);
				startActivity(intent);
			}
		});
		
	}

	private String getIntentVar(String varName) {
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return null;
		}
		return extras.getString(varName);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu, menu);
		return false;
	}

}
