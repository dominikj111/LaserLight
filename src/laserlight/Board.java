package laserlight;

import laserlight.GameItemsListener.gameItems;
import laserlight.Mirror.MirrorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends JPanel {

    public static int mirrorL = 1, mirrorR = 2, laserGun = 3, wall = 4, target = 5, ruin = 6, empty = 0;
    
    private final HashMap<Integer, Class> slovnik;
    
    private GameObjects[][] board;
    private BufferedImage environment;
    private int pocetRadku, pocetSloupcu;
    
    
    private ArrayList<Target> cile;
    
    private BufferedImage pozadi;
    
    public Board(int pocetRadku, int pocetSloupcu) {
        setFocusable(false);

        this.pocetRadku = pocetRadku;
        this.pocetSloupcu = pocetSloupcu;
        
        this.cile = new ArrayList<>();
        
        
        board = new GameObjects[pocetRadku][pocetSloupcu];
        
        slovnik = new HashMap<>();
        slovnik.put(mirrorL, Mirror.class);
        slovnik.put(mirrorR, Mirror.class);
        slovnik.put(laserGun, LaserGun.class);
        slovnik.put(wall, Wall.class);
        slovnik.put(target, Target.class);
        slovnik.put(empty, EmptySpace.class);
        slovnik.put(ruin, Ruin.class);
        
        this.addMouseListener(reakceNaKliknuti);
        
        setBackground(Color.LIGHT_GRAY);
        
//        try {
//            this.pozadi = ImageIO.read(getClass().getResource("background.jpg"));
//        } catch (IOException ex) {
//            System.out.println("Pozadi nelze nacist!\n" + ex.getMessage());
//        }
        
        currentBoard = this;
    
    }

    public void newStateGame(int[][] herniPole) throws Exception{
               
        if(herniPole.length != board.length) {
            throw new Exception("Pocet radku neodpovida nastavenemu typu hry.");
        }
        
        for (int radek = 0; radek < herniPole.length; radek++) {
            
            if(herniPole[radek].length != board[radek].length){
                throw new Exception("Pocet bunek v radku " + radek + " neodpovida nastavenemu typu hry.");
            }
            
            for (int sloupec = 0; sloupec < herniPole[radek].length; sloupec++) {
                
                
                board[radek][sloupec] = (GameObjects)slovnik.get(herniPole[radek][sloupec]).newInstance();
                
                board[radek][sloupec].setLocation(new Point(sloupec, radek));
                board[radek][sloupec].setFather(this);
                
                if(board[radek][sloupec] instanceof Mirror){
                    ((Mirror)board[radek][sloupec]).setMirrorType(
                            (herniPole[radek][sloupec] == mirrorL) ? Mirror.MirrorType.left : Mirror.MirrorType.right
                    );
                }
                
                if(board[radek][sloupec] instanceof Target){
                    this.cile.add((Target)board[radek][sloupec]);
                }
            }
        }
        
        Laser.initLaserArray(board.length, board[0].length);
        
        updateGameEnvironment();
        
    }

    public void updateGameEnvironment() {

        Laser.updateLasers();
               
        for (int i=0; i<this.cile.size(); i++) {
            
            Target tempT = this.cile.get(i);
            
            if(tempT.isZasazen()){                
                
                this.cile.remove(i);
                
                fireShotEvent(tempT, this.cile);
                
                              
                /*Odstraneni cile z mapy a nahrani trosky*/
                
                try{
                    this.board[tempT.getLocation().y][tempT.getLocation().x] = (GameObjects)slovnik.get(ruin).newInstance();
                    this.board[tempT.getLocation().y][tempT.getLocation().x].setLocation(new Point(tempT.getLocation().x, tempT.getLocation().y));
                    this.board[tempT.getLocation().y][tempT.getLocation().x].setFather(this);
                }catch(InstantiationException | IllegalAccessException ex){
                    
                }
                
            } 
        }
        
        if(GameObjects.getImageSize() == null){return;}
    
        Dimension rozmerProstredi = new Dimension(GameObjects.getImageSize().width * pocetSloupcu,
                GameObjects.getImageSize().height * pocetRadku);

        BufferedImage envi2 = new BufferedImage(rozmerProstredi.width, rozmerProstredi.height, BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(rozmerProstredi);
        
        Graphics2D grafika = (Graphics2D)envi2.getGraphics();
        
        if(this.pozadi != null){
            grafika.drawImage(this.pozadi, 0, 0, this);
        }
        
       ArrayList<Laser> lasers = Laser.getAllLasers();
        ArrayList<Point> lightPoints;
        
        for (Laser l : lasers) {
            lightPoints = l.getLaserPath();
            
            grafika.setColor(l.getBarvaLaseru());
            grafika.setStroke(new BasicStroke(l.getSirkaLaseru()));
            
            for (int i = 0; i < lightPoints.size() - 1; i++) {
                grafika.drawLine(lightPoints.get(i).x, lightPoints.get(i).y, 
                                 lightPoints.get(i + 1).x, lightPoints.get(i+1).y);
                
            }
            
            grafika.setColor(Color.WHITE);            
            Point lastPoint = lightPoints.get(lightPoints.size() - 1);
            grafika.fillOval(lastPoint.x - 10, lastPoint.y - 10, 20, 20);
            System.out.println(lastPoint);
        }
        
        vykresli(grafika, new Class[]{LaserGun.class, Target.class, EmptySpace.class});
        
        for (Laser l : lasers) {
            lightPoints = l.getLaserPath();
            grafika.setColor(Color.WHITE);            
            Point lastPoint = lightPoints.get(lightPoints.size() - 1);
            grafika.fillOval(lastPoint.x - 10, lastPoint.y - 10, 20, 20);
            System.out.println(lastPoint);
        }
                
        vykresli(grafika, new Class[]{Mirror.class, Wall.class, Ruin.class});
        
        this.environment = envi2;
        repaint();
    }
    
    private void vykresli(Graphics g, Class[] objs){
        int tempX, tempY;
        GameObjects tempObjHry;
        
        for (int radek = 0; radek < pocetRadku; radek++) {
            for (int sloupec = 0; sloupec < pocetSloupcu; sloupec++) {
                
                tempObjHry = board[radek][sloupec];
                
                for (Class obj : objs) {
                    if (tempObjHry.getClass() == obj) {
                        tempX = sloupec * GameObjects.getImageSize().width;                
                        tempY = radek   * GameObjects.getImageSize().height;
                        
                        g.drawImage(tempObjHry.getImageObjekt(), tempX, tempY, this);
                    }
                }
            }
        }
    }
    
    private gameItems currentItem = null;
    
    public void activeItem(gameItems itemType){
        this.currentItem = itemType;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(environment, 0, 0, this);
    }
    
    private final MouseAdapter reakceNaKliknuti = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent me) {
            super.mouseClicked(me);
            Point souradniceMysi = me.getPoint();
            
            int radek   = souradniceMysi.y / GameObjects.getImageSize().height;
            int sloupec = souradniceMysi.x / GameObjects.getImageSize().width;
            
            System.out.println(board[radek][sloupec] + " : [" + radek + "," + sloupec + "]");
            
            if(board[radek][sloupec] instanceof LaserGun){
                LaserGun laser = (LaserGun)board[radek][sloupec];
                
                if(currentItem == gameItems.gunFire){
                    laser.fireSwitch();
                }
                if(currentItem == gameItems.gunRotation){
                    laser.rotujLaserGun();
                }
            }
            
            if(board[radek][sloupec] instanceof EmptySpace){
                
                if(currentItem != null){
                    if(currentItem == gameItems.mirrorL){
                        addMirror(radek, sloupec, MirrorType.left);
                    }
                    if(currentItem == gameItems.mirrorR){
                        addMirror(radek, sloupec, MirrorType.right);
                    }
                    
                }
             
            }
            
            updateGameEnvironment();
        }
        
    };
    
    public LaserCoordDir getNextLaserState(LaserCoordDir oldLaserState){
        
        if(oldLaserState.dir == null){return null;}    
        
        
        Point noveP, noveV, noveArray;
        
        noveArray = new Point(
                oldLaserState.arrayLocation.x + oldLaserState.dir.x,
                oldLaserState.arrayLocation.y + oldLaserState.dir.y
        );
        
        if(noveArray.x < 0 || noveArray.y < 0 || noveArray.x >= this.pocetSloupcu || noveArray.y >= this.pocetRadku){
            noveV = null;
            noveArray = null;
        } else {
            noveV = this.board[noveArray.y][noveArray.x].transformujVektor(oldLaserState.dir);
        }
        
        noveP = new Point(oldLaserState.coord.x + oldLaserState.dir.x * GameObjects.getImageSize().width,
                          oldLaserState.coord.y + oldLaserState.dir.y * GameObjects.getImageSize().height);       
        
        
        
        
        if(noveArray != null){
            if(board[noveArray.y][noveArray.x] instanceof Wall){
                noveP = generujStredUsecky(oldLaserState.coord, noveP);
            }
        }
         
        return new LaserCoordDir(noveP, noveV, noveArray);
    }
    
    private static Board currentBoard;
    
    public static Board getCurrentBoard(){
        return currentBoard;
    }
    
    private void addMirror(int radek, int sloupec, MirrorType type){
        if(!(this.board[radek][sloupec] instanceof EmptySpace)){
            System.out.println("Policko neni volne.");
            return;
        }
        
        Mirror mir = new Mirror();
        mir.setLocation(new Point(sloupec, radek));
        mir.setFather(this);
        mir.setMirrorType(type);
       
        this.board[radek][sloupec] = mir;
        
    }

    private Point generujStredUsecky(Point A, Point B) {
        return new Point(
                (int)Math.round(A.x + 0.5 * (B.x - A.x)),
                (int)Math.round(A.y + 0.5 * (B.y - A.y))
        );
    }

    
    
    
    private ArrayList<ShotEvent> listeners = new ArrayList<>();
    
    public void addShotEvent(ShotEvent l){
        listeners.add(l);
    }
    
    private void fireShotEvent(Target t, ArrayList<Target> cile) {
        listeners.forEach((listener) -> listener.targetDestroyed(t, cile));
    }

    public void clearMirrors() {
        
        //Tady ten algoritmus vychazi v podstate z metody nahore newStateGame
        //najdeme vsechny zrcadla a misto nich vytvorime prazdne policko
        //to jsme delali i nahore, ale pro vsechny objekty
        //pak postupujeme uz stejne, inicializujeme novy objekt na danem policku
        //a nakonec aktualizujeme lasery a updatujeme environment jako nahore
        
        for (int radek = 0; radek < this.board.length; radek++) {
            for (int sloupec = 0; sloupec < this.board[radek].length; sloupec++) {
                if(this.board[radek][sloupec] instanceof Mirror){
                    try{
                        board[radek][sloupec] = (GameObjects)slovnik.get(empty).newInstance();
                        board[radek][sloupec].setLocation(new Point(sloupec, radek));
                        board[radek][sloupec].setFather(this);
                    } catch(InstantiationException | IllegalAccessException ex){
                        System.out.println("Chyba pri mazani zrcadel!");
                    }
                }
            }
        }
        
        Laser.initLaserArray(board.length, board[0].length);
        
        updateGameEnvironment();
        
    }
}