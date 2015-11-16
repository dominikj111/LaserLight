package laserlight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Wall extends GameObjects {

     public Wall(){
        
        BufferedImage wall = null;
        
        try { 
            wall = ImageIO.read(getClass().getResource("/images/wall.png")); 
        } 
        catch(IOException ex){
            System.out.println("Chyba pri nacitani zdi.");
        }
        
         initObjekt(wall);
         
    }

    @Override
    public Point transformujVektor(Point dir) {
        return null;
    }   
}