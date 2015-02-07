package promitech.football;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import promitech.football.engine.AllowedMovementsFinder;
import promitech.football.engine.DifficultyLevel;
import promitech.football.engine.MovementPathFinder6;
//import promitech.football.engine.MovementPathFinder7;
import promitech.football.engine.MovementGraph;
import promitech.football.engine.MovementPath;
import promitech.football.engine.MovementResult;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldPoint;
import promitech.football.engine.WinReason;
import promitech.football.files.MovementGraphFileOperator;

public class MainFootball extends JFrame {
    private static final long serialVersionUID = 1L;

    private final PlayfieldDrawerSwing playfieldDrawer;
    private final PlayfieldDrawableObjects playfieldDrawableObjects = new PlayfieldDrawableObjects();
    
    private BufferedImage offScreen;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;
    
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            onMouseMove(e.getX(), e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            moveBallToPoint(e.getX(), e.getY());
        }
    };
    
    private KeyAdapter keyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {

            if (KeyEvent.VK_H == e.getKeyCode()) {
            	playfieldDrawableObjects.possibleMovements = null;        	
                showHintMovements();
            } else if (KeyEvent.VK_M == e.getKeyCode()) {
            	playfieldDrawableObjects.possibleMovements = null;        	
                autoMoveBall();
            } else if (KeyEvent.VK_J == e.getKeyCode()) {
            	showPossibleMovements();
            } else if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
            	playfieldDrawableObjects.possibleMovements = null;        	
                if (e.getModifiers() == 2) { // z ctrl
                    saveMovementGraph(e.getKeyCode());
                    System.out.println("playfield saved");
                } else {
                    loadMovementGraph(e.getKeyCode());
                    repaint();
                }
            } else {
//                System.out.println("keycode: " + e.getKeyCode() + " mod " + e.getModifiers());
            }
        }
    };
    
    public void showPossibleMovements() {
    	if (playfieldDrawableObjects.possibleMovements != null) {
    		playfieldDrawableObjects.possibleMovements = null;        	
        	repaint();
    		return;
    	}
    	AllowedMovementsFinder finder = new AllowedMovementsFinder(playfieldDrawableObjects.movementGraph);
    	playfieldDrawableObjects.possibleMovements = finder.generatePossibleMovements();
    	repaint();
    }
    
    private String createMovementGraphFilename(int keyCode) {
        return "playfield_" + keyCode + ".dat";
    }
    
    private void loadMovementGraph(int keyCode) {
    	String filename = createMovementGraphFilename(keyCode);
        playfieldDrawableObjects.movementGraph = MovementGraphFileOperator.loadGraphFromFile(filename);
        playfieldDrawableObjects.oneWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.One);
        playfieldDrawableObjects.twoWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.Two);
        System.out.println("playfield loaded from " + filename);
    }
    
    private void saveMovementGraph(int keyCode) {
        MovementGraphFileOperator.saveGraphToFile(
            playfieldDrawableObjects.movementGraph, 
            createMovementGraphFilename(keyCode)
        );
    }
    
    private void autoMoveBall() {
        playfieldDrawableObjects.hintMovements = null;
        MovementPathFinder6 movementPathFinder = new MovementPathFinder6(playfieldDrawableObjects.movementGraph, difficultyLevel);
        MovementPath theBestMovement = movementPathFinder.generateTheBestMovement();
        MovementResult movementResult = null;
        if (theBestMovement.isBlocked()) {
        	movementResult = MovementResult.instanceWithoutAbilityToMove();
        	movementResult.winReason = WinReason.OPPONENT_BLOCKED;
        	movementResult.winner = playfieldDrawableObjects.movementGraph.getPlayerTurn().opponent();
        } else {
	        movementResult = playfieldDrawableObjects.movementGraph.moveBallViaPath(theBestMovement);
	        playfieldDrawableObjects.oneWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.One);
	        playfieldDrawableObjects.twoWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.Two);
        }
        if (movementResult.winner != null) {
        	System.out.println("the winner is " + movementResult.winner);
        	System.out.println("       reason " + movementResult.winReason);
        }
        repaint();
    }
    
    public MainFootball() {
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        //setLocationRelativeTo( );
        
        setSize(400, 700);

        addKeyListener(keyAdapter);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        
        playfieldDrawer = new PlayfieldDrawerSwing(getWidth(), getHeight());
        playfieldDrawableObjects.movementGraph = new MovementGraph();
        playfieldDrawableObjects.oneWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.One);
        playfieldDrawableObjects.twoWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.Two);
        
        offScreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void showHintMovements() {
    	playfieldDrawableObjects.possibleMovements = null;
        if (playfieldDrawableObjects.hintMovements != null) {
            playfieldDrawableObjects.hintMovements = null;
            repaint();
        } else {
        	MovementPathFinder6 movementPathFinder = new MovementPathFinder6(playfieldDrawableObjects.movementGraph, difficultyLevel);
        	MovementPath theBestMovement = movementPathFinder.generateTheBestMovement();
        	
        	playfieldDrawableObjects.hintMovements = new ArrayList<MovementPath>(1);
        	playfieldDrawableObjects.hintMovements.add(theBestMovement);
            repaint();
/*        	
        	throw new RuntimeException("show hint movements not implemented");

            
            
            playfieldDrawableObjects.hintMovements = movementPathFinder.generateAllowableMovements(
                playfieldDrawableObjects.movementGraph, 
                playfieldDrawableObjects.movementGraph.getPlayerTurn()
            );
            if (playfieldDrawableObjects.hintMovements != null) {
                for (MovementPath hintMovements : playfieldDrawableObjects.hintMovements) {
                    System.out.println("# " + hintMovements.toString());
                }
                repaint();
            }
*/            
        }
    }
/*
    protected void moveBallToHintMovement() {
        playfieldDrawableObjects.hintMovements = null;
        MovementPathFinder movementPathFinder = new MovementPathFinder();
        long m = System.currentTimeMillis();
        System.out.println("start thinking");
        movementPathFinder.generateAllowableMovements(
                playfieldDrawableObjects.movementGraph, 
                playfieldDrawableObjects.movementGraph.getPlayerTurn()
            );
        m = System.currentTimeMillis() - m;
        System.out.println("end thinging, time: " + m);
        MovementPath chooseTheBestPath = movementPathFinder.chooseTheBestPath(
                playfieldDrawableObjects.movementGraph, 
                playfieldDrawableObjects.movementGraph.getPlayerTurn()
        );
        playfieldDrawableObjects.movementGraph.moveBallAlongPath(chooseTheBestPath);
        repaint();
	}
*/    
    private void moveBallToPoint(int x, int y) {
    	playfieldDrawableObjects.possibleMovements = null;
    	
        if (!playfieldDrawer.isPointOnPlayfield(x, y)) {
            return;
        }
        playfieldDrawableObjects.hintMovements = null;
        
        PlayfieldPoint p = playfieldDrawer.convertScreenPointToPlayfieldPoint(x, y);
        MovementResult movementResult = playfieldDrawableObjects.movementGraph.moveBallToNeighbourPoint(p);
        playfieldDrawableObjects.oneWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.One);
        playfieldDrawableObjects.twoWeightMap = playfieldDrawableObjects.movementGraph.generateWeightMap(Player.Two);
        
        if (movementResult.winner != null) {
            System.out.println("the winner is " + movementResult.winner);
            System.out.println("       reason " + movementResult.winReason);
        }
        if (movementResult.canMove) {
        	repaint();
        }
    }

    public void onMouseMove(int x, int y) {
        if (playfieldDrawer.isPointOnPlayfield(x, y)) {
            PlayfieldPoint p = playfieldDrawer.convertScreenPointToPlayfieldPoint(x, y);
            if (playfieldDrawableObjects.gridCursorPoint == null 
                    || playfieldDrawableObjects.gridCursorPoint.x != p.x
                    || playfieldDrawableObjects.gridCursorPoint.y != p.y) {
                playfieldDrawableObjects.gridCursorPoint = p;
                repaint();
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        playfieldDrawer.setGraphics(offScreen.getGraphics());
        playfieldDrawer.drawGameWindows(playfieldDrawableObjects);
        g.drawImage(offScreen, 0, 0, this);
    }

    public static void main(String[] args) {
        new MainFootball().setVisible(true);
    }

}
