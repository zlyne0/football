package promitech.football;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {
	private final static long DEFAULT_DURATION = 150;
	
	private final ThreadPoolExecutor movieRefreshViewThread;
	
	private List<Bitmap> frames = new ArrayList<Bitmap>();
	private Bitmap actualFrame;
	private int actualFrameIndex = 0;
	private boolean animated = false;
	private Paint paint = new Paint();
	
	public Animation() {
		int corePoolSize = 1;
		int maximumPoolSize = 1;
		long keepAliveTime = 60 * 60;
		TimeUnit unit = TimeUnit.SECONDS;
		
		movieRefreshViewThread = new ThreadPoolExecutor(
				corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				unit, 
				new LinkedBlockingQueue<Runnable>()
		);
	}
	
	public void addFrame(Context context, int resourceId) {
		Bitmap b = BitmapFactory.decodeResource(context.getResources(), resourceId);
		frames.add(b);
	}
	
	public boolean isAnimated() {
		return animated;
	}

	public int getWidth() {
		if (frames.isEmpty()) {
			return 0;
		}
		return frames.get(0).getWidth();
	}

	public int getHeight() {
		if (frames.isEmpty()) {
			return 0;
		}
		return frames.get(0).getHeight();
	}

	public void startAnimate(final RefreshViewScreen refreshViewScreen) {
		animated = true;
		movieRefreshViewThread.execute(new Runnable() {
			@Override
			public void run() {
				while (animated) {
					actualFrame = frames.get(actualFrameIndex);
					refreshViewScreen.refresh();
					try {
						Thread.sleep(DEFAULT_DURATION);
					} catch (InterruptedException e) {}
					actualFrameIndex++;
					if (actualFrameIndex > frames.size() - 1) {
						actualFrameIndex = 0;
					}
				}
			}
		});
	}
	
	public void stopAnimate() {
		animated = false;
	}
	
	public void draw(Canvas g, int x, int y) {
		if (actualFrame == null) {
			return;
		}
		g.drawBitmap(actualFrame, x, y, paint);
	}

}
