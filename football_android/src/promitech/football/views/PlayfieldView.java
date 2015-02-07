package promitech.football.views;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import promitech.football.Animation;
import promitech.football.DrawableObjects;
import promitech.football.PlayfieldDrawerOnCanvas;
import promitech.football.PlayfieldSizes;
import promitech.football.R;
import promitech.football.RefreshViewScreen;
import promitech.football.WinerNotification;
import promitech.football.engine.AllowedMovementsFinder;
import promitech.football.engine.DifficultyLevel;
import promitech.football.engine.MovementGraph;
import promitech.football.engine.MovementPath;
import promitech.football.engine.MovementPathFinder6;
import promitech.football.engine.MovementResult;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.engine.WinReason;
import promitech.football.services.GameConfigurationService;
import promitech.football.services.dto.GameSettingsDto;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;

public class PlayfieldView extends View {

	private PlayfieldDrawerOnCanvas drawer = new PlayfieldDrawerOnCanvas();
	private DrawableObjects drawableObjects = new DrawableObjects();

	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	private PlayfieldSizes playfieldSizes;
	
	private ThreadPoolExecutor computerMoveThread;
	
	private boolean executeMoveOnBegining = true;
	private boolean blockInputBecauseOfComputerMove = false;
	
	private GameSettingsDto gameRules = null;	
	private WinerNotification winerNotification;
	
	private RefreshViewScreen refreshViewScreen = new RefreshViewScreen() {
		private final Handler refreshScreenHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				PlayfieldView.this.invalidate();
			};
		};
		@Override
		public void refresh() {
			refreshScreenHandler.sendEmptyMessage(0);
		}
	};
	
	private final Handler handleAiWining = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MovementResult moveResult = (MovementResult)msg.obj;
			winerNotification.win(gameRules.getPlayerNick(moveResult.winner), moveResult.winReason);
		}
	};
	
	public PlayfieldView(Context context, WinerNotification winerNotification) {
		super(context);
		drawer.initLabels(context);
		
		this.winerNotification = winerNotification;
		drawableObjects.computerMoveCalculationProgress = new Animation();
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_000);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_001);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_002);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_003);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_004);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_005);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_006);
		drawableObjects.computerMoveCalculationProgress.addFrame(getContext(), R.drawable.ball_007);
		
		scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleGestureListener);
		gestureDetector = new GestureDetector(getContext(), gestureListener);
		
		int corePoolSize = 1;
		int maximumPoolSize = 1;
		long keepAliveTime = 60 * 60;
		TimeUnit unit = TimeUnit.SECONDS;
		computerMoveThread = new ThreadPoolExecutor(corePoolSize, maximumPoolSize , keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
		
		loadGameRules();

		drawableObjects.movementGraph = new MovementGraph();
		if (gameRules.isAiMove(drawableObjects.movementGraph.getPlayerTurn())) {
			blockInputBecauseOfComputerMove = true;
		}
	}

	private void loadGameRules() {
		GameConfigurationService gameConfigurationService = new GameConfigurationService(getContext());
		gameRules = gameConfigurationService.loadGameSettings();
		drawableObjects.drawMovesButton = gameRules.getDifficultyLevel().equals(DifficultyLevel.TRIVIAL);
	}
	
	private OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			playfieldSizes = playfieldSizes.changeScale(detector.getScaleFactor());
			return true;
		}
	};
	
	private OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			if (blockInputBecauseOfComputerMove) {
				return true;
			}
			if (drawableObjects.drawMovesButton && 
					PlayfieldSizes.movementsButtonPosition.contains(event.getX(), event.getY())) {
				generatePossibleMovements();
				return true;
			}
			
			drawableObjects.focus = playfieldSizes.screenCordsToPlayfieldPoint(event.getX(), event.getY());
			return true;
		};
		
		@Override
		public boolean onDoubleTap(MotionEvent event) {
			if (blockInputBecauseOfComputerMove) {
				return true;
			}
			drawableObjects.possibleMovements = null;
			
			drawableObjects.focus = null;
			PlayfieldPoint fieldPoint = playfieldSizes.screenCordsToPlayfieldPoint(event.getX(), event.getY());
			if (fieldPoint != null) {
				MovementResult moveResult = drawableObjects.movementGraph.moveBallToNeighbourPoint(fieldPoint);
				if (moveResult.winner != null) {
					winerNotification.win(gameRules.getPlayerNick(moveResult.winner), moveResult.winReason);
				} else {
					if (!moveResult.canBounce) {
						if (gameRules.isAiMove(drawableObjects.movementGraph.getPlayerTurn())) {
							generateComputerMove();
						}
					}
				}
			}
			return true;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			playfieldSizes.correctScrollCoordinates(distanceX, distanceY);
			return true;
		};
	};
	
	private void generatePossibleMovements() {
		if (drawableObjects.possibleMovements != null) {
			drawableObjects.possibleMovements = null;
			return;
		}
		AllowedMovementsFinder finder = new AllowedMovementsFinder(drawableObjects.movementGraph);
		drawableObjects.possibleMovements = finder.generatePossibleMovements();
	}
	
	private void generateComputerMove() {
		blockInputBecauseOfComputerMove = true;
		drawableObjects.computerMoveCalculationProgress.startAnimate(refreshViewScreen);
		
		computerMoveThread.execute(new Runnable() {
			@Override
			public void run() {
				MovementPathFinder6 finder = new MovementPathFinder6(
						drawableObjects.movementGraph, 
						gameRules.getDifficultyLevel()
				);
				MovementPath path = finder.generateTheBestMovement();
				drawableObjects.computerMoveCalculationProgress.stopAnimate();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
				refreshViewScreen.refresh();
				
				if (path.isBlocked()) {
					MovementResult moveResult = MovementResult.instanceWithoutAbilityToMove();
					moveResult.winReason = WinReason.OPPONENT_BLOCKED;
					moveResult.winner = drawableObjects.movementGraph.getPlayerTurn().opponent();
					Message msg = new Message();
					msg.obj = moveResult;
					handleAiWining.sendMessage(msg);
					return;
				}
				
				for (int i=1; i<path.getPath().size(); i++) {
					PlayfieldPoint dest = path.getPath().get(i);
					playfieldSizes.centerScreenOnPlayfieldPoint(dest);
					
					MovementResult moveResult = drawableObjects.movementGraph.moveBallToNeighbourPoint(dest);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {}
					refreshViewScreen.refresh();
					
					if (moveResult.winner != null) {
						Message msg = new Message();
						msg.obj = moveResult;
						handleAiWining.sendMessage(msg);
						return;
					}
				}
				blockInputBecauseOfComputerMove = false;
			}
		});
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (playfieldSizes == null) {
			playfieldSizes = new PlayfieldSizes(getWidth(), getHeight());
		}
		
		drawer.draw(canvas, drawableObjects, playfieldSizes);
		
//		printDebugInfo(canvas);

		if (executeMoveOnBegining) {
			executeMoveOnBegining = false;
			if (gameRules.isAiMove(drawableObjects.movementGraph.getPlayerTurn())) {
				generateComputerMove();
			}
		}
	}

	private void printDebugInfo(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		int line = 0;
		canvas.drawText("scale = " + playfieldSizes.getScale(), 10, line*20+20, paint);
		line++;
		canvas.drawText("screen = " + getWidth() + " x " + getHeight() , 10, line*20+20, paint);
		line++;
		canvas.drawText(
				"scroll = " + playfieldSizes.getScrollCords().x + " x " + playfieldSizes.getScrollCords().y, 
				10, line*20+20, paint);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean retVal = scaleGestureDetector.onTouchEvent(event);
		retVal = gestureDetector.onTouchEvent(event) || retVal;
		retVal = super.onTouchEvent(event) || retVal;
		
		if (retVal) {
			invalidate();
		}
		return retVal;
	}
}
