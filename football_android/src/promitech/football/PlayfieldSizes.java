package promitech.football;

import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldPoint;
import android.graphics.Point;
import android.graphics.RectF;

public class PlayfieldSizes {
	public final static int VERTICAL_CELLS = 16;
	public final static int HORIZONTAL_CELLS = 10;
	public final static int HORIZONTAL_BAND_PART_CELLS = 4;
	public final static int GATEWAY_WIDTH_CELLS = 2;
	
	public final static int DEFAULT_MARGIN_TOP = 20+40;
	public final static int DEFAULT_CELL_SIZE = 20;
	public final static int DEFAULT_MARGIN_LEFT = 20;
	public final static int DEFAULT_GAP_BETWEEN_PLAYFIELD_AND_ARROW = 5;
	
	public final static int MOVEMENTS_BUTTON_POS_X = 20;
	public final static int MOVEMENTS_BUTTON_POS_Y = 10;
	private final static int MOVEMENTS_BUTTON_WEIGHT = 80;
	private final static int MOVEMENTS_BUTTON_HEIGHT = 30;
	
	public final static RectF movementsButtonPosition = new RectF(
			MOVEMENTS_BUTTON_POS_X, 
			MOVEMENTS_BUTTON_POS_Y, 
			MOVEMENTS_BUTTON_WEIGHT + MOVEMENTS_BUTTON_POS_X, 
			MOVEMENTS_BUTTON_HEIGHT + MOVEMENTS_BUTTON_POS_Y
	); 
	
	public final static int REQUIRE_SIZE_X =  
			HORIZONTAL_CELLS * DEFAULT_CELL_SIZE + 
			DEFAULT_MARGIN_LEFT + 
			DEFAULT_GAP_BETWEEN_PLAYFIELD_AND_ARROW + 
			50;
	public final static int REQUIRE_SIZE_Y =
			VERTICAL_CELLS * DEFAULT_CELL_SIZE +
			DEFAULT_MARGIN_TOP +
			20;
	
	private float marginLeft = DEFAULT_MARGIN_LEFT;
	private float marginTop  = DEFAULT_MARGIN_TOP;
	private float cellSize   = DEFAULT_CELL_SIZE;
	private float gapBetweenPlayfieldAndArrow = DEFAULT_GAP_BETWEEN_PLAYFIELD_AND_ARROW;
	private float arrowHeight = 50;
	
	private float[][] arrowUp = new float[][] {
		{20, 20},
		{10, 30},
		{15, 30},
		{15, 60},
		{25, 60},
		{25, 30},
		{30, 30}
	};
	private float[][] arrowDown = new float[][] {
		{15, 30},
		{25, 30},
		{25, 60},
		{30, 60},
		{20, 70},
		{10, 60},
		{15, 60}    	
	};

	private final int screenWidth;
	private final int screenHeight;
	private float horizontalBound;
	private float verticalBound;
	private final Point scrollCords = new Point(0, 0);

	private final float defaultScale;
	private float scale; 

	private PlayfieldSizes(int w, int h, float scale) {
		this.screenWidth = w;
		this.screenHeight = h;
		this.defaultScale = calculateScaleBaseOnScreenSize(w, h);
		this.scale = scale;
		
		scaleElements(scale);
	}
	
	public PlayfieldSizes(int w, int h) {
		this.screenWidth = w;
		this.screenHeight = h;
		this.defaultScale = calculateScaleBaseOnScreenSize(w, h);
		this.scale = this.defaultScale;
		scaleElements(scale);
	}
	
	public PlayfieldSizes changeScale(float scaleFactor) {
		scale *= scaleFactor;
		if (scale < defaultScale) {
			scale = defaultScale;
		}
		// TODO: max scale
		PlayfieldSizes newPlayfieldSizes = new PlayfieldSizes(screenWidth, screenHeight, scale);
		newPlayfieldSizes.scrollCords.x = scrollCords.x;
		newPlayfieldSizes.scrollCords.y = scrollCords.y;
		newPlayfieldSizes.resetScrollCordsToProperFitToScreen();
		return newPlayfieldSizes;
	}
	
	private void scaleElements(float scaleFactor) {
		marginLeft *= scaleFactor;
		marginTop *= scaleFactor;
		cellSize *= scaleFactor;
		gapBetweenPlayfieldAndArrow *= scaleFactor;
		arrowHeight *= scaleFactor;
		for (int i=0; i<arrowUp.length; i++) {
			arrowUp[i][0] *= scaleFactor;
			arrowUp[i][1] *= scaleFactor;
		}
		for (int i=0; i<arrowDown.length; i++) {
			arrowDown[i][0] *= scaleFactor;
			arrowDown[i][1] *= scaleFactor;
		}
		
		horizontalBound = screenWidth - PlayfieldSizes.REQUIRE_SIZE_X * scaleFactor;
		verticalBound = screenHeight - PlayfieldSizes.REQUIRE_SIZE_Y * scaleFactor;
	}
	
	public void correctScrollCoordinates(float distanceX, float distanceY) {
		scrollCords.x -= distanceX;
		scrollCords.y -= distanceY;
		resetScrollCordsToProperFitToScreen();
	}
	
	private void resetScrollCordsToProperFitToScreen() {
		if (scrollCords.x > 0) {
			scrollCords.x = 0;
		}
		if (scrollCords.y > 0) {
			scrollCords.y = 0;
		}
		if (scrollCords.x < horizontalBound) {
			scrollCords.x = (int)horizontalBound;
		}
		if (scrollCords.y < verticalBound) {
			scrollCords.y = (int)verticalBound;
		}
	}
	
	private float calculateScaleBaseOnScreenSize(int screenWidth, int screenHeight) {
		int requireSize = REQUIRE_SIZE_X;
		return (float)(screenWidth) / requireSize; 
	}
	
	public float[][] getArrowPoints(Player player) {
		if (Player.One.equals(player)) {
			return arrowUp;
		}
		return arrowDown;
	}

	public boolean isPointOnScreen(Point p) {
		return p.x >= 0 && p.x <= screenWidth && p.y >= 0 && p.y <= screenHeight;
	}
	
	public void centerScreenOnPlayfieldPoint(PlayfieldPoint dest) {
		Point screenDestPoint = playfieldPointToScreenPoint(dest);
		if (isPointOnScreen(screenDestPoint)) {
			return;
		}
		scrollCords.x = (int)(marginLeft + dest.x * cellSize) - screenWidth / 2;
		scrollCords.x = -scrollCords.x;
		scrollCords.y = (int)(marginTop + dest.y * cellSize) - screenHeight / 2;
		scrollCords.y = -scrollCords.y;
		resetScrollCordsToProperFitToScreen();
	}
	
	public Point playfieldPointToScreenPoint(PlayfieldPoint p) {
		Point r = new Point();
		r.x = (int)(marginLeft + p.x * cellSize) + scrollCords.x;
		r.y = (int)(marginTop + p.y * cellSize) + scrollCords.y;
    	return r;
	}

	public PlayfieldPoint screenCordsToPlayfieldPoint(float x, float y) {
		x -= scrollCords.x;
		y -= scrollCords.y;
		if (!canCalculateScreenPointToPlayfieldPoint(x, y)) {
			return null;
		}
		
        x = x - marginLeft;
        y = y - marginTop;
        PlayfieldPoint p = new PlayfieldPoint();

        p.x = (int)(x / cellSize);
        if (x % cellSize > cellSize/2) {
        	p.x++;
        }
        p.y = (int)(y / cellSize);
        if (y % cellSize > cellSize/2) {
        	p.y++;
        }
        return p;
	}
	
	public boolean canCalculateScreenPointToPlayfieldPoint(float screenX, float screenY) {
		// cellSize/2 in order to easly click on band
		return 
				screenX > marginLeft - cellSize/2 && 
				screenX < marginLeft + cellSize*HORIZONTAL_CELLS + cellSize/2 &&
				screenY > marginTop - cellSize/2 &&
				screenY < marginTop + cellSize*VERTICAL_CELLS + cellSize/2;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public float getCellSize() {
		return cellSize;
	}

	public float getGapBetweenPlayfieldAndArrow() {
		return gapBetweenPlayfieldAndArrow;
	}

	public float getArrowHeight() {
		return arrowHeight;
	}

	public Point getScrollCords() {
		return scrollCords;
	}

	public float getScale() {
		return scale;
	}
}

