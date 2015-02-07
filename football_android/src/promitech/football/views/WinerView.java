package promitech.football.views;

import promitech.football.Animation;
import promitech.football.R;
import promitech.football.RefreshViewScreen;
import promitech.football.engine.WinReason;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class WinerView extends View {
	
	private final Animation fireworksGif;
	private final String winerName;
	private final String winReasonLabel;
	private Paint textPainter = new Paint();
	private Paint background = new Paint(0);
	
	private RefreshViewScreen refreshViewScreen = new RefreshViewScreen() {
		private final Handler refreshScreenHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				WinerView.this.invalidate();
			};
		};
		@Override
		public void refresh() {
			refreshScreenHandler.sendEmptyMessage(0);
		}
	};
	
	public WinerView(Context context, String winerName, WinReason winReason) {
		super(context);
		this.winerName = winerName;
		this.winReasonLabel = determineWinReasonLabel(winReason);
		
		fireworksGif = new Animation();
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_000);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_001);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_002);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_003);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_004);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_005);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_006);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_007);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_008);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_009);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_010);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_011);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_012);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_013);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_014);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_015);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_016);
		fireworksGif.addFrame(getContext(), R.drawable.fireworks_017);
		fireworksGif.startAnimate(refreshViewScreen);
		
		textPainter.setColor(Color.WHITE);
		textPainter.setTextSize(24);
		textPainter.setAntiAlias(true);
		textPainter.setTextAlign(Align.CENTER);
	}
	
	private String determineWinReasonLabel(WinReason winReason) {
		if (WinReason.SCORE_A_GOAL.equals(winReason)) {
			return getContext().getString(R.string.playerWinLabel); 
		} else {
			return getContext().getString(R.string.playerWinByBlockingOpponent); 
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		background.setColor(Color.BLACK);
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), background);
		
		int x = getWidth() / 2 - fireworksGif.getWidth() / 2;
		fireworksGif.draw(canvas, x, 0);
		
		drawText(canvas);
	}
	
	private void drawText(Canvas canvas) {
		int x = getWidth() / 2;
		int y = 0;
		canvas.drawText(winerName, x, y*20 + fireworksGif.getHeight()+20, textPainter);
		y += 2;
		canvas.drawText(winReasonLabel, x, y*20 + fireworksGif.getHeight()+20, textPainter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		((Activity)WinerView.this.getContext()).finish();
		return false;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		fireworksGif.stopAnimate();
		super.onDetachedFromWindow();
	}
}

