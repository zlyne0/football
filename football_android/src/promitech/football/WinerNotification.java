package promitech.football;

import promitech.football.engine.WinReason;

public interface WinerNotification {

	public void win(String winerNamem, WinReason reason);
}
