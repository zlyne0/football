package promitech.football.files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import promitech.football.engine.MovementGraph;

public class MovementGraphFileOperator {

    public static MovementGraph loadGraphFromFile(String fileName) {
        DataInputStream ois = null;
        try {
            ois = new DataInputStream(new FileInputStream(fileName));
            return new MovementGraph(ois);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    public static void saveGraphToFile(MovementGraph movementGraph, String fileName) {
        DataOutputStream oos = null;
        try {
            oos = new DataOutputStream(new FileOutputStream(fileName, false));
            movementGraph.writeToStream(oos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static MovementGraph loadGraphFromResource(String resourceFilename) {
        DataInputStream ois = null;
        try {
            ois = new DataInputStream(Thread.class.getResourceAsStream(resourceFilename));
            return new MovementGraph(ois);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    public static MovementGraph loadGraphFromResourceOld(String resourceFilename) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(Thread.class.getResourceAsStream(resourceFilename));
            MovementGraph movementGraph = (MovementGraph)ois.readObject();
            return movementGraph;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    
}
