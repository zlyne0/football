package promitech.football;

import promitech.football.engine.MovementGraph;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldDrawer;
import promitech.football.engine.PlayfieldPoint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

class PlayfieldColors {
	int background = Color.BLACK;
	int border = Color.WHITE;
	int grid = Color.GRAY;
	
	int playerOneColor = Color.RED;
	int playerTwoColor = Color.BLUE;
}

class PlayfieldPaints {
	final Paint background;
	final Paint border;
	final Paint grid;
	final Paint arrowUp;
	final Paint arrowDown;
	final Paint focusOne;
	final Paint focusTwo;
	final Paint ballOne;
	final Paint ballTwo;
	final Paint stepLineOne;
	final Paint stepLineTwo;
	
	final Paint movesButton;
	{
		movesButton = new Paint();
		movesButton.setAntiAlias(true);
		movesButton.setColor(Color.YELLOW);
		movesButton.setStyle(Style.FILL);
	}
	
	final Paint hintButtonTextPainter;
	{
		hintButtonTextPainter = new Paint();
		hintButtonTextPainter.setColor(Color.BLACK);
		hintButtonTextPainter.setTextSize(14);
		hintButtonTextPainter.setAntiAlias(true);
		hintButtonTextPainter.setTextAlign(Align.CENTER);
	}
	
	final Paint hintMovementsLines;
	{
		hintMovementsLines = new Paint();
		hintMovementsLines.setColor(Color.YELLOW);
		hintMovementsLines.setAntiAlias(true);
		hintMovementsLines.setStyle(Paint.Style.STROKE);
		hintMovementsLines.setStrokeWidth(3);
		hintMovementsLines.setPathEffect(new DashPathEffect(new float[] { 3, 3, 3 }, 2));
	}
	
	PlayfieldPaints(PlayfieldColors colors) {
		background = new Paint(0);
		background.setColor(colors.background);
		
		border = new Paint(0);
		border.setColor(colors.border);
		
		grid = new Paint(0);
		grid.setColor(colors.grid);
		
    	arrowUp = new Paint();
    	arrowUp.setColor(colors.playerOneColor);
    	arrowUp.setStyle(Style.FILL);
    	arrowDown = new Paint();
    	arrowDown.setColor(colors.playerTwoColor);
    	arrowDown.setStyle(Style.FILL);
    	
    	focusOne = new Paint();
    	focusOne.setColor(colors.playerOneColor);
    	focusOne.setStyle(Paint.Style.STROKE);
    	focusOne.setStrokeWidth(3);
    	focusTwo = new Paint();
    	focusTwo.setColor(colors.playerTwoColor);
    	focusTwo.setStyle(Paint.Style.STROKE);
    	focusTwo.setStrokeWidth(3);
    	
    	ballOne = new Paint();
    	ballOne.setColor(colors.playerOneColor);
    	ballOne.setStyle(Paint.Style.FILL);
    	ballOne.setStrokeWidth(3);

    	ballTwo = new Paint();
    	ballTwo.setColor(colors.playerTwoColor);
    	ballTwo.setStyle(Paint.Style.FILL);
    	ballTwo.setStrokeWidth(3);
    	
    	stepLineOne = new Paint();
    	stepLineOne.setColor(colors.playerOneColor);
    	stepLineOne.setStyle(Style.FILL);
    	stepLineOne.setStrokeWidth(3);
    	stepLineTwo = new Paint();
    	stepLineTwo.setColor(colors.playerTwoColor);
    	stepLineTwo.setStyle(Style.FILL);
    	stepLineTwo.setStrokeWidth(3);
	}

	public Paint getArrowPaint(Player player) {
		if (Player.One.equals(player)) {
			return arrowUp;
		}
		return arrowDown;
	}
	
	public Paint getFocusPaint(Player player) {
		if (Player.One.equals(player)) {
			return focusOne;
		}
		return focusTwo;
	}
	
	public Paint getPlayerBall(Player player) {
		if (Player.One.equals(player)) {
			return ballOne;
		}
		return ballTwo;
	}
	
	public Paint getPlayerStepLinePaint(Player player) {
		if (Player.One.equals(player)) {
			return stepLineOne;
		}
		return stepLineTwo;
	}
}

public class PlayfieldDrawerOnCanvas implements PlayfieldDrawer {

	private PlayfieldPaints paints = new PlayfieldPaints(new PlayfieldColors());
	private PlayfieldSizes sizes;
	
	private Canvas g;	
	
	private String movesButtonLabel = "";
	
	public void initLabels(Context context) {
		movesButtonLabel = context.getString(R.string.movesButtonLabel);
	}
	
	public void draw(Canvas canvas, DrawableObjects objects, PlayfieldSizes sizes) {
		g = canvas;
		this.sizes = sizes;
		Point scroll = sizes.getScrollCords();
		
		g.drawRect(new Rect(0, 0, sizes.getScreenWidth(), sizes.getScreenHeight()), paints.background);
		
		drawPlayfield(sizes.getMarginLeft() + scroll.x, sizes.getMarginTop() + scroll.y);
		drawArrows(objects.movementGraph.getPlayerTurn(), scroll);

		objects.movementGraph.paint(this);		

		drawPossibleMovements(objects.possibleMovements);
		
		drawGridFocus(objects.focus, objects.movementGraph.getPlayerTurn());
		drawAiCalculationProgress(objects.computerMoveCalculationProgress);
		
		drawMovesButton(objects.drawMovesButton);
	}

	private final PlayfieldDrawer possibleMovementsDrawer = new PlayfieldDrawer() {
		@Override
		public void drawPlayLine(PlayfieldPoint from, PlayfieldPoint to, Player player) {
			Point a = sizes.playfieldPointToScreenPoint(from);
			Point b = sizes.playfieldPointToScreenPoint(to);
			g.drawLine(
					a.x, a.y, 
					b.x, b.y,
					paints.hintMovementsLines);
		}
		
		@Override
		public void drawBall(PlayfieldPoint arg0, Player arg1) {
		}
	};
	private void drawPossibleMovements(MovementGraph possibleMovements) {
		if (possibleMovements != null) {
			possibleMovements.paint(possibleMovementsDrawer);
		}
	}

	private void drawMovesButton(boolean drawMovesButton) {
		if (!drawMovesButton) {
			return;
		}
		g.drawRoundRect(PlayfieldSizes.movementsButtonPosition, 10, 10, paints.movesButton);
		g.drawText(movesButtonLabel, 
				PlayfieldSizes.MOVEMENTS_BUTTON_POS_X + 40, 
				PlayfieldSizes.MOVEMENTS_BUTTON_POS_Y + 20, 
				paints.hintButtonTextPainter
		);
	}
	
	private void drawAiCalculationProgress(Animation aiCalculationProgressGif) {
		if (aiCalculationProgressGif.isAnimated()) {
			int x = sizes.getScreenWidth() / 2;
			int y = sizes.getScreenHeight() / 2;
			x -= aiCalculationProgressGif.getWidth() / 2;
			y -= aiCalculationProgressGif.getHeight() / 2;
			aiCalculationProgressGif.draw(g, x, y);
		}
	}
	
	private void drawPlayfield(float pfx, float pfy) {
        drawPlaygroundGrid(pfx, pfy);
        
        // left and right band
        g.drawLine(
            pfx, pfy, 
            pfx, pfy + PlayfieldSizes.VERTICAL_CELLS * sizes.getCellSize(),
            paints.border);
        g.drawLine(
            pfx + PlayfieldSizes.HORIZONTAL_CELLS * sizes.getCellSize(), pfy + 0, 
            pfx + PlayfieldSizes.HORIZONTAL_CELLS * sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(),
            paints.border);
        
        // upper band
        g.drawLine(
            pfx, pfy, 
            pfx + PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS * sizes.getCellSize(), pfy,
            paints.border);
        g.drawLine(
            pfx + PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy, 
            pfx + PlayfieldSizes.HORIZONTAL_CELLS*sizes.getCellSize(), pfy,
            paints.border);
        // upper gateway
        g.drawLine(
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize(), pfy, 
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize(), pfy-sizes.getCellSize()/2,
            paints.border);
        g.drawLine(
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy, 
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy-sizes.getCellSize()/2,
            paints.border);
        
        // bottom band
        g.drawLine(
            pfx, pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(), 
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(),
            paints.border);
        g.drawLine(
            pfx + PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(), 
            pfx + PlayfieldSizes.HORIZONTAL_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(),
            paints.border);
        // bottom gateway
        g.drawLine(
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(), 
            pfx+PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize() + sizes.getCellSize()/2,
            paints.border);
        g.drawLine(
            pfx + PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize(), 
            pfx + PlayfieldSizes.HORIZONTAL_BAND_PART_CELLS*sizes.getCellSize() + PlayfieldSizes.GATEWAY_WIDTH_CELLS*sizes.getCellSize(), pfy + PlayfieldSizes.VERTICAL_CELLS*sizes.getCellSize() + sizes.getCellSize()/2,
            paints.border);
    }
	
    private void drawPlaygroundGrid(float pfx, float pfy) {
        for (int i=0; i<=PlayfieldSizes.HORIZONTAL_CELLS; i++) {
            g.drawLine(
                pfx + (i*sizes.getCellSize()), pfy, 
                pfx + (i*sizes.getCellSize()), pfy + PlayfieldSizes.VERTICAL_CELLS * sizes.getCellSize(),
                paints.grid);
        }
        
        for (int i=0; i<=PlayfieldSizes.VERTICAL_CELLS; i++) {
            g.drawLine(
                pfx, pfy + (i*sizes.getCellSize()), 
                pfx + PlayfieldSizes.HORIZONTAL_CELLS*sizes.getCellSize(), pfy + (i*sizes.getCellSize()),
                paints.grid);
        }
    }
    
    private void drawArrows(Player player, Point scroll) {
    	Paint paint = paints.getArrowPaint(player);
    	float[][] arrowPoints = sizes.getArrowPoints(player);
    	
    	float translateX = 
    			PlayfieldSizes.HORIZONTAL_CELLS * sizes.getCellSize() + 
    			sizes.getMarginLeft() + 
    			sizes.getGapBetweenPlayfieldAndArrow() +
    			scroll.x;
    	float translateY = sizes.getMarginTop() + scroll.y;
    	for (int i=0; i<6; i++ ) {
    		drawArrow(paint, arrowPoints, translateX, translateY);
    		translateY += sizes.getArrowHeight();
    	}
    }
    
    private void drawArrow(Paint paint, float [][] arrowPath, float translateX, float translateY) {
    	Path arrow = new Path();
    	arrow.moveTo(arrowPath[0][0] + translateX, arrowPath[0][1] + translateY);
    	for (int i=0; i<arrowPath.length; i++) {
        	arrow.lineTo(arrowPath[i][0] + translateX, arrowPath[i][1] + translateY);
    	}
    	arrow.lineTo(arrowPath[0][0] + translateX, arrowPath[0][1] + translateY);
    	g.drawPath(arrow, paint);
    }
    
    private void drawGridFocus(PlayfieldPoint p, Player player) {
        if (p != null) {
        	Point sp = sizes.playfieldPointToScreenPoint(p);
            float focusSize = sizes.getCellSize() / 2;

            Paint focusPaint = paints.getFocusPaint(player);
            
            RectF oval = new RectF(
            		sp.x - focusSize, 
            		sp.y - focusSize, 
            		sp.x + focusSize, 
            		sp.y + focusSize);
			g.drawOval(oval, focusPaint);
            //g.drawCircle(sp.x, sp.y, focusSize, focusPaint);
            
//            String st = "(" +p.x + ", " + p.y + ")";
//            g.setColor(Color.gray);
//            g.drawString(st, 30, PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH+100);
            
//            st = "distance( " + getDistance(player, p) + " )";
//            g.drawString(st, 30, PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH+120);
        }
    }

    public PlayfieldSizes getSizes() {
		return sizes;
	}

	@Override
	public void drawBall(PlayfieldPoint ball, Player playerTurn) {
		Point sp = sizes.playfieldPointToScreenPoint(ball);
        
        int ballSize = (int)sizes.getCellSize() / 4;
        
        Paint ballPaint = paints.getPlayerBall(playerTurn);
        RectF oval = new RectF(
        		sp.x - ballSize, 
        		sp.y - ballSize, 
        		sp.x + ballSize, 
        		sp.y + ballSize);
		g.drawOval(oval, ballPaint);
	}

	@Override
	public void drawPlayLine(PlayfieldPoint fromPoint, PlayfieldPoint toPoint, Player player) {
		Paint paint = paints.getPlayerStepLinePaint(player);
		Point a = sizes.playfieldPointToScreenPoint(fromPoint);
		Point b = sizes.playfieldPointToScreenPoint(toPoint);
        g.drawLine(
        		a.x, a.y, 
        		b.x, b.y,
        		paint);
	}
}
