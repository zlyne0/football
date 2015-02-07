package promitech.football.activities;

import promitech.football.IntentVars;
import promitech.football.WinerNotification;
import promitech.football.engine.WinReason;
import promitech.football.views.PlayfieldView;
import promitech.football.views.WinerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PlayfieldActivity extends Activity {
	
	private boolean endOfGame = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		WinerNotification winerNotification = new WinerNotification() {
			@Override
			public void win(String winerName, WinReason winReason) {
				endOfGame = true;
				setContentView(new WinerView(PlayfieldActivity.this, winerName, winReason));
			}
		};
		setContentView(new PlayfieldView(this, winerNotification));
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PlayfieldActivity.this, MenuActivity.class);
		if (!endOfGame) {
			intent.putExtra(IntentVars.FROM_ACTIVITY, PlayfieldActivity.class.getName());
		}
		startActivity(intent);
	}
}
