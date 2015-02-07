package promitech.football.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class PlayfieldPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int x;
    public int y;
    
    public PlayfieldPoint() {
    }
    
    public PlayfieldPoint(PlayfieldPoint p) {
    	changeCords(p);
    }
    
    public PlayfieldPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PlayfieldPoint changeCords(PlayfieldPoint p) {
        this.x = p.x;
        this.y = p.y;
        return this;
    }

    public PlayfieldPoint changeCords(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public PlayfieldPoint minus(PlayfieldPoint a) {
    	PlayfieldPoint p = new PlayfieldPoint(this);
    	p.x -= a.x;
    	p.y -= a.y;
    	return p;
    }

    public PlayfieldPoint minusAndChange(PlayfieldPoint a) {
        this.x -= a.x;
        this.y -= a.y;
        return this;
    }
    
    public PlayfieldPoint addAndChange(PlayfieldPoint a) {
        x += a.x;
        y += a.y;
        return this;
    }

    public PlayfieldPoint addAndChange(MoveDirection md) {
        x = x + md.x - 1;
        y = y + md.y - 1;
        return this;
    }
    
    public PlayfieldPoint add(MoveDirection md) {
    	PlayfieldPoint p = new PlayfieldPoint(this);
    	return p.addAndChange(md);
    }
    
    public boolean equalCords(PlayfieldPoint p) {
    	return equalCords(p.x, p.y);
    }

    public boolean equalCords(int x, int y) {
    	return this.x == x && this.y == y;
    }
    
    public PlayfieldPoint readFromStream(DataInputStream ois) throws IOException {
        x = ois.readInt();
        y = ois.readInt();
        return this;
    }
    
    public PlayfieldPoint writeToStream(DataOutputStream oos) throws IOException {
        oos.writeInt(x);
        oos.writeInt(y);
        return this;
    }
    
    public String toString() {
    	return "(" + x + "," + y + ")";
    }
}
