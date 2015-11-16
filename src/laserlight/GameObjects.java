package laserlight;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObjects {

    private BufferedImage img = null;
    private static Dimension rozmer = null;
    private static Point stredObrazku = null;
    private Point location;

    private final int hashValue;
    
    public GameObjects() {
        this.hashValue = generateHash();
    }
    
    protected void initObjekt(BufferedImage buffImg){
        
        if(buffImg == null){
            return;
        }
        
        this.img = buffImg;
        
        if(rozmer == null){
            rozmer = new Dimension(buffImg.getWidth(), buffImg.getHeight());
        }
    }
    
    public void setLocation(Point loc){
        this.location = loc;
    }
    
    public Point getLocation(){
        return this.location;
    }
    
    public BufferedImage getImageObjekt(){
        return this.img;
    }
 
    public static Dimension getImageSize(){
        return rozmer;
    }

    public static Point getStredObrazku(){
        if(stredObrazku == null){
            stredObrazku = new Point(rozmer.width / 2, rozmer.height / 2);
        }
        
        return stredObrazku;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder("Objekt hry -> ");
        sb.append(this.getClass().getName());
        
        return sb.toString();
    }

    public abstract Point transformujVektor(Point dir);

    @Override
    public int hashCode() {
        return this.hashValue;
    }

    @Override
    public boolean equals(Object obj) {
        
        if(obj instanceof GameObjects){
            return obj.hashCode() == this.hashCode();
        }
        
        return false;        
    }
    
    private Board fatherBoard;
    
    public void setFather(Board board){
        this.fatherBoard = board;
    }
    
    public Board getFather(){
        return this.fatherBoard;
    }
    
    private static int hashNumber = 0;
    
    private static int generateHash(){
        return hashNumber++;
    }
}