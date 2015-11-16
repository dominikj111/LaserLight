package laserlight;

import java.awt.*;

public class EmptySpace extends GameObjects{

    public EmptySpace() {
        
        initObjekt(null);
             
    }

    @Override
    public Point transformujVektor(Point dir) {
        return dir;
    }
     
}
