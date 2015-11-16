package laserlight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Mirror extends GameObjects{

    private int typeMirrorSignum = 0;
    
    public MirrorType type;
    
    @Override
    public Point transformujVektor(Point dir) {
        
        return new Point(this.typeMirrorSignum * dir.y, this.typeMirrorSignum * dir.x);
        
    }
    
    public enum MirrorType { left, right }
    
    public void setMirrorType(MirrorType mirrType){
        
        BufferedImage mirr = null;
        this.type = mirrType;
        
        this.typeMirrorSignum = (mirrType == MirrorType.left) ? +1 : -1;
        
        try { 
            if(mirrType == MirrorType.left){
                mirr = ImageIO.read(getClass().getResource("/images/mirrorL.png"));  
            }       
            if(mirrType == MirrorType.right){
                mirr = ImageIO.read(getClass().getResource("/images/mirrorR.png"));  
            }
        
        } 
        catch(IOException ex){
            System.out.println("Chyba pri nacitani zrcadel.");
        }
        
        initObjekt(mirr);
        
    }
    
    public MirrorType getMirrorType(){
        return this.type;
    }
}