package laserlight;

import java.awt.*;

public class LaserCoordDir {
    
    public final Point coord, dir, arrayLocation;
    
    public LaserCoordDir(Point coord, Point dir, Point arrayLocation) {
        this.coord = coord;
        this.dir = dir;
        this.arrayLocation = arrayLocation;
    }
}
