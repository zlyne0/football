package promitech.football.gif;

import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import promitech.football.RefreshViewScreen;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;

public class MovieGif {
	private final ThreadPoolExecutor movieRefreshViewThread;

	private Movie movie;
	private boolean animate = false;
	
	public MovieGif(Context context, int resourceDrawableId) {
		InputStream moveInputStream = context.getResources().openRawResource(resourceDrawableId);
		movie = Movie.decodeStream(moveInputStream);
		
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
	
	public void startAnimate(final RefreshViewScreen refreshViewScreen) {
		animate = true;
		movieRefreshViewThread.execute(new Runnable() {
			@Override
			public void run() {
				long movieStart = 0;
				
				while (animate) {

					long now = android.os.SystemClock.uptimeMillis();
					if (movieStart == 0) {
						movieStart = now;
					}
					long relTime = ((now - movieStart) % movie.duration());
					movie.setTime((int) relTime);
					
					refreshViewScreen.refresh();
					
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {}
				}
			}
		});
	}
	
	public void stopAnimate() {
		animate = false;
	}

	public boolean isAnimated() {
		return animate;
	}
	
	public void draw(Canvas canvas, int x, int y) {
		movie.draw(canvas, x, y);
	}
	
	public int getWidth() {
		return movie.width();
	}
	
	public int getHeight() {
		return movie.height();
	}
}
