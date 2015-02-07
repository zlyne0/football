package promitech.football;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import promitech.football.engine.MovementPath;
import promitech.football.engine.Player;
import promitech.football.engine.PlayfieldDrawer;
import promitech.football.engine.PlayfieldPoint;

public class PlayfieldDrawerSwing implements PlayfieldDrawer {

	private final static Color BACKGROUND_COLOR = Color.BLACK;
    private final static Color PLAYFIELD_LINE_COLOR = Color.gray;
	
    private final static int CELL_WIDTH = 30;

    private final static Polygon arrowUpPolygon = new Polygon();
    private final static Polygon arrowDownPolygon = new Polygon();
    static {
	    arrowUpPolygon.addPoint(20, 20);
	    arrowUpPolygon.addPoint(10, 30);
	    arrowUpPolygon.addPoint(15, 30);
	    arrowUpPolygon.addPoint(15, 60);
	    arrowUpPolygon.addPoint(25, 60);
	    arrowUpPolygon.addPoint(25, 30);
	    arrowUpPolygon.addPoint(30, 30);
	    arrowDownPolygon.addPoint(15, 30);
	    arrowDownPolygon.addPoint(25, 30);
	    arrowDownPolygon.addPoint(25, 60);
	    arrowDownPolygon.addPoint(30, 60);
	    arrowDownPolygon.addPoint(20, 70);
	    arrowDownPolygon.addPoint(10, 60);
	    arrowDownPolygon.addPoint(15, 60);    	
    }
    
    public final static int PLAYFIELD_HEIGHT_IN_CELLS = 16;
    public final static int PLAYFIELD_WIDTH_IN_CELLS = 10;
    private final static int PLAYFIELD_WIDTH_PART = 4;  
    private final static int PLAYFIELD_GATEWAY_WIDTH = 2;  
    
    private final static int PLAYFIELD_CORD_X = 20;
    private final static int PLAYFIELD_CORD_Y = 40;

    private final int windowWidth;
    private final int windowHeight;
    
    private final Map<Player, Color> playerColor;
    private Graphics g;
    
    public PlayfieldDrawerSwing(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        playerColor = new HashMap<Player, Color>(2);
        playerColor.put(Player.One, Color.red);
        playerColor.put(Player.Two, Color.blue);
    }
    
    public void drawGameWindows(PlayfieldDrawableObjects playfieldDrawableObjects) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, windowWidth, windowHeight);
        
        // draw playfield
        drawPlayfield(PLAYFIELD_CORD_X, PLAYFIELD_CORD_Y);
        playfieldDrawableObjects.movementGraph.paint(this);
        
        drawHintMoves(playfieldDrawableObjects.hintMovements);
        drawGridFocus(playfieldDrawableObjects.gridCursorPoint, playfieldDrawableObjects.movementGraph.getPlayerTurn(), playfieldDrawableObjects);
        
        if (playfieldDrawableObjects.possibleMovements != null) {
        	PlayfieldDrawer playfieldDrawer = new PlayfieldDrawer() {
				@Override
				public void drawPlayLine(PlayfieldPoint from, PlayfieldPoint to, Player player) {
			    	g.setColor(Color.yellow);
			    	Graphics2D g2 = (Graphics2D) g;
			    	g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 3f, 3f, 3f }, 2f ));
			    	Point a = convertPlayfieldToScreenPoint(from);
			    	Point b = convertPlayfieldToScreenPoint(to);
			    	g.drawLine(a.x, a.y, b.x, b.y);
				}
				
				@Override
				public void drawBall(PlayfieldPoint ball, Player playerTurn) {
				}
			};
			playfieldDrawableObjects.possibleMovements.paint(playfieldDrawer);
        }
    }
    
    private void drawHintMoves(List<MovementPath> hintMovements) {
        if (hintMovements == null) {
            return;
        }
        g.setColor(Color.yellow);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 3f, 3f, 3f }, 2f ));
        
        PlayfieldPoint src;
        PlayfieldPoint dest;
        for (MovementPath mp : hintMovements) {
            if (mp.getPath().size() == 0) {
                continue;
            }
            src = mp.getPath().get(0);
            for (int i=1; i<mp.getPath().size(); i++) {
                dest = mp.getPath().get(i);
                Point a = convertPlayfieldToScreenPoint(src);
                Point b = convertPlayfieldToScreenPoint(dest);
                g.drawLine(a.x, a.y, b.x, b.y);
                src = dest;
            }
        }
    }

    private void drawPlayfield(int pfx, int pfy) {
        drawPlaygroundGrid(pfx, pfy);
        
        g.setColor(PLAYFIELD_LINE_COLOR);
        
        // left and right band
        g.drawLine(
            pfx, pfy, 
            pfx, pfy+PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH);
        g.drawLine(
            pfx+PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy+0, 
            pfx+PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy+PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH);
        
        // upper band
        g.drawLine(
            pfx, pfy, 
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy);
        g.drawLine(
            pfx + PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy, 
            pfx + PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy);
        // upper gateway
        g.drawLine(
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy, 
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy-CELL_WIDTH/2);
        g.drawLine(
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy, 
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy-CELL_WIDTH/2);
        
        // bottom band
        g.drawLine(
            pfx, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH, 
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH);
        g.drawLine(
            pfx + PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH, 
            pfx + PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH);
        // bottom gateway
        g.drawLine(
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH, 
            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH + CELL_WIDTH/2);
        g.drawLine(
            pfx + PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH, 
            pfx + PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy + PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH + CELL_WIDTH/2);

        // TODO: rysowanie bandy w kolorze gracza
//        g.setColor(playerColor.get(Player.One));
//        g.fillRect(pfx, pfy, pfx+5, 0);
//        
//        g.setColor(playerColor.get(Player.Two));
//        g.fillRect(
//            pfx, pfy-CELL_WIDTH/2,
//            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH, pfy);
//        g.fillRect(
//            pfx+PLAYFIELD_WIDTH_PART*CELL_WIDTH + PLAYFIELD_GATEWAY_WIDTH*CELL_WIDTH, pfy-CELL_WIDTH/2, 
//            pfx+PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy);
    }

    private void drawPlaygroundGrid(int pfx, int pfy) {
        g.setColor(PLAYFIELD_LINE_COLOR);
        for (int i=0; i<=PLAYFIELD_WIDTH_IN_CELLS; i++) {
            g.drawLine(
                pfx + (i*CELL_WIDTH), pfy, 
                pfx + (i*CELL_WIDTH), pfy + PLAYFIELD_HEIGHT_IN_CELLS * CELL_WIDTH);
        }
        
        for (int i=0; i<=PLAYFIELD_HEIGHT_IN_CELLS; i++) {
            g.drawLine(
                pfx, pfy + (i*CELL_WIDTH), 
                pfx + PLAYFIELD_WIDTH_IN_CELLS*CELL_WIDTH, pfy + (i*CELL_WIDTH));
        }
    }
    
    public void drawArrows(Player player) {
		g.setColor(playerColor.get(player));
		Polygon p ;
		if (Player.One.equals(player)) {
			p = new Polygon(arrowUpPolygon.xpoints, arrowUpPolygon.ypoints, arrowUpPolygon.npoints);
		} else {
			p = new Polygon(arrowDownPolygon.xpoints, arrowDownPolygon.ypoints, arrowDownPolygon.npoints);
		}
		p.translate(PLAYFIELD_WIDTH_IN_CELLS * CELL_WIDTH + PLAYFIELD_CORD_X + 20, PLAYFIELD_CORD_Y);
		for (int i = 0; i < 9; i++) {
			g.fillPolygon(p);
			p.translate(0, 50);
		}
    }
    
    private void drawGridFocus(PlayfieldPoint p, Player player, PlayfieldDrawableObjects playfieldDrawableObjects) {
    	drawArrows(player);
    	
        if (p != null) {
            Point sp = convertPlayfieldToScreenPoint(p.x, p.y);
            int focusSize = CELL_WIDTH / 2;
            g.setColor(playerColor.get(player));
            g.drawOval(sp.x - focusSize/2, sp.y - focusSize/2, focusSize, focusSize);

            String st = "(" +p.x + ", " + p.y + ")";
            g.setColor(Color.gray);
            g.drawString(st, 30, PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH+100);
            
            st = "distance( " + getDistance(player, p, playfieldDrawableObjects) + " )";
            g.drawString(st, 30, PLAYFIELD_HEIGHT_IN_CELLS*CELL_WIDTH+120);
        }
    }
    
    private int getDistance(Player player, PlayfieldPoint point, PlayfieldDrawableObjects playfieldDrawableObjects) {
        if (Player.One.equals(player)) {
            return playfieldDrawableObjects.oneWeightMap.getVal(point);
        } else {
            return playfieldDrawableObjects.twoWeightMap.getVal(point);
        }
    }
    

    public boolean isPointOnPlayfield(int x, int y) {
        return x >= PLAYFIELD_CORD_X && x <= PLAYFIELD_CORD_X + PLAYFIELD_WIDTH_IN_CELLS * CELL_WIDTH &&
            y >= PLAYFIELD_CORD_Y && y <= PLAYFIELD_CORD_Y + PLAYFIELD_HEIGHT_IN_CELLS * CELL_WIDTH;
    }
    
    public PlayfieldPoint convertScreenPointToPlayfieldPoint(int x, int y) {
        x = x - PLAYFIELD_CORD_X;
        y = y - PLAYFIELD_CORD_Y;
        PlayfieldPoint p = new PlayfieldPoint();
        if (x % CELL_WIDTH < CELL_WIDTH/2) {
            p.x = (x - x % CELL_WIDTH) / CELL_WIDTH;
        } else {
            p.x = (x - x % CELL_WIDTH + CELL_WIDTH) / CELL_WIDTH;
        }
        if (y % CELL_WIDTH < CELL_WIDTH/2) {
            p.y = (y - y % CELL_WIDTH) / CELL_WIDTH;
        } else {
            p.y = (y - y % CELL_WIDTH + CELL_WIDTH) / CELL_WIDTH;
        }
        return p;
    }

    public Point convertPlayfieldToScreenPoint(PlayfieldPoint p) {
    	return convertPlayfieldToScreenPoint(p.x, p.y);
    }
    
    public Point convertPlayfieldToScreenPoint(int x, int y) {
        Point p = new Point();
        p.x = x * CELL_WIDTH;
        p.y = y * CELL_WIDTH;
        p.x += PLAYFIELD_CORD_X;
        p.y += PLAYFIELD_CORD_Y;
        return p;
    }

    public void drawBall(PlayfieldPoint ball, Player playerTurn) {
        Point sp = convertPlayfieldToScreenPoint(ball.x, ball.y);
        
        int ballSize = CELL_WIDTH / 2;
        g.setColor(playerColor.get(playerTurn));
        g.fillOval(
            sp.x - ballSize/2, 
            sp.y - ballSize/2,
            ballSize, ballSize);
    }
    
    public void drawPlayLine(PlayfieldPoint from, PlayfieldPoint to, Player player) {
    	g.setColor(playerColor.get(player));
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setStroke(new BasicStroke(3));
    	Point a = convertPlayfieldToScreenPoint(from);
    	Point b = convertPlayfieldToScreenPoint(to);
    	g.drawLine(a.x, a.y, b.x, b.y);
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }
    
}
