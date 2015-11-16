package laserlight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Target extends GameObjects {

    private boolean cilBylZasazen = false;
    
     public Target(){
        
        BufferedImage target = null;
        
        try { 
            target = ImageIO.read(getClass().getResource("/images/target.png")); 
        } 
        catch(IOException ex){
            System.out.println("Chyba pri nacitani cile.");
        }
        
        
         initObjekt(target);
         
    }

     public boolean isZasazen(){return this.cilBylZasazen;}
     
    @Override
    public Point transformujVektor(Point dir) {
        
        System.err.println("Zasazen cil: " + getLocation());
       this.cilBylZasazen = true;
        return null;
    }

    @Override
    public String toString() {
        return "TARGET:[" + getLocation().x + "," + getLocation().y + "]";
    }
}