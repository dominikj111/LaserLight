package laserlight;

import java.awt.*;
import java.util.ArrayList;

public class Laser {

    private static final float defaultLasersWidth = 0.5f;

    private final Color barvaLaseru;
    private final Point pozice, smer;
    private final int hashCodeValue;
    private final Point arrayLocation;

    private ArrayList<Point> laserPath = new ArrayList<>();
    private ArrayList<Point> laserArrayPath = new ArrayList<>();

    public Laser(Color barvaLaseru, Point pozice, Point arrayLocation, Point smer, int hash) {
        this.barvaLaseru = barvaLaseru;
        this.pozice = pozice;
        this.smer = smer;
        this.hashCodeValue = hash;
        this.arrayLocation = arrayLocation;
    }

    private void deletePathFrom(Point endArrayPoint){
        
        boolean cut = false;
        
        for (int i = 0; i < laserArrayPath.size(); i++) {
            
            if(cut){
                laserArrayPath.remove(i);
                laserPath.remove(i);
                i--;
                continue;
            }
            
            if(laserArrayPath.get(i).equals(endArrayPoint)){cut = true;}
        }
    }
    
    private void clearPaths(){
        this.laserPath.clear();
        this.laserArrayPath.clear();
    }
    
    private void addPathPoint(Point realPoint, Point arrayPoint){
        this.laserPath.add(realPoint);
        this.laserArrayPath.add(arrayPoint);
    }

    public ArrayList<Point> getLaserPath() {
        return this.laserPath;
    }

    public Color getBarvaLaseru() {
        return this.barvaLaseru;
    }

    public float getSirkaLaseru() {
        return defaultLasersWidth;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Laser)) {
            return false;
        }

        return obj.hashCode() == this.hashCode();
    }

    public void dispose() {
        removeLaser(this);
    }

    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }

    @Override
    public String toString() {
        return "Laser: [" + this.pozice + "], " + "[" + this.hashCode() + "]";
    }

    // +++++++++++++++++++++++++++++++++
    // +        STATIC MEMBERS         +
    // +++++++++++++++++++++++++++++++++
    
    private static final ArrayList<Laser> lasersToDraw = new ArrayList<>();    
    private static Board paintBoard;

    private static int ROWSCount, COLUMNSCount;

    public static Laser addLaser(Color barvaLaseru, Point pozice, Point arrayLocation, Point smer, int hash, Board board) {

        paintBoard = board;

        Laser paprsek = new Laser(barvaLaseru, pozice, arrayLocation, smer, hash);
        if (!lasersToDraw.contains(paprsek)) {
            lasersToDraw.add(paprsek);
            updateLasers();
        }

        return paprsek;
    }

    private static void removeLaser(Laser paprsek) {
        if (lasersToDraw.contains(paprsek)) {
            lasersToDraw.remove(paprsek);
            updateLasers();
        }
    }

    public static ArrayList<Laser> getAllLasers() {
        return lasersToDraw;
    }

    public static void initLaserArray(int pocetRadku, int pocetSloupcu) {

        ROWSCount = pocetRadku;
        COLUMNSCount = pocetSloupcu;

    }

    
    
    public static void updateLasers() {

        resetLaserContainers(COLUMNSCount, ROWSCount);
        LaserCoordDir tempLaserCordDir;
        
        for (Laser l : lasersToDraw) {
            l.clearPaths();

            tempLaserCordDir = new LaserCoordDir(l.pozice, l.smer, l.arrayLocation);
            
            do {
                l.addPathPoint(tempLaserCordDir.coord, tempLaserCordDir.arrayLocation);
                
                
                if(tempLaserCordDir.arrayLocation == null || !addCell(tempLaserCordDir.arrayLocation.y, tempLaserCordDir.arrayLocation.x, l, tempLaserCordDir.dir)){
                    break;
                }
                
                tempLaserCordDir = paintBoard.getNextLaserState(tempLaserCordDir);
                
            } while (tempLaserCordDir != null);
            
            
        }
                
//        StringBuilder sb = new StringBuilder();
//        
//        for (int radek = 0; radek < laserContainers.length; radek++) {
//            for (int sloupec = 0; sloupec < laserContainers[radek].length; sloupec++) {
//                
//                sb.append("[");
//                
//                if(laserContainers[radek][sloupec] == null || laserContainers[radek][sloupec].lasersCell.isEmpty()){
//                    sb.append(" ");
//                } else {
//                    for(int i=0; i<laserContainers[radek][sloupec].lasersCell.size(); i++){
//                        sb.append(laserContainers[radek][sloupec].lasersCell.get(i).hashCode());
//                    }
//                }
//                sb.append("]");
//                
//            }
//            sb.append("\n");
//            
//            
//        }
//        
//        System.out.println(sb);
    }

    
    static boolean addCell(int radek, int sloupec, Laser laser, Point laserDirection){
        
        
        
        if(laserContainers[radek][sloupec] == null){
            laserContainers[radek][sloupec] = new laserContainer();
        } 
        
        if(laserContainers[radek][sloupec].lasersCell.contains(laser)){
            return false;
        }
        
        laserContainers[radek][sloupec].addLaser(laser, laserDirection);
        
        if(laserContainers[radek][sloupec].lasersCell.size() > 1){
            for (Laser l : laserContainers[radek][sloupec].lasersCell) {
                l.deletePathFrom(new Point(sloupec, radek));
            }
            
            return false;
        }
        
        return true;
    }
    
    static void resetLaserContainers(int sirka, int vyska){
        laserContainers = new laserContainer[vyska][sirka];        
    }
    
    private static laserContainer[][] laserContainers;
    
    
    private static class laserContainer{
        
        ArrayList<Laser> lasersCell      = new ArrayList<>();
        ArrayList<Point> laserDirections = new ArrayList<>();

        private void addLaser(Laser laser, Point laserDirection) {
           lasersCell.add(laser);
           laserDirections.add(laserDirection);
        }
    }
}