package laserlight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Ruin  extends GameObjects {

     public Ruin(){
        
        BufferedImage ruin = null;
        
        try { 
            ruin = ImageIO.read(getClass().getResource("/images/ruin.png")); 
        } 
        catch(IOException ex){
            System.out.println("Chyba pri nacitani trosky.");
        }
        
         initObjekt(ruin);
         
    }

    @Override
    public Point transformujVektor(Point dir) {
        return null;
    }
}