package laserlight;

import laserlight.GameItemsListener.gameItems;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameMenu extends JPanel{
    
    private gameItems activeItem = null;
    
    public GameMenu() {
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        addClickAction("/images/inMenu.png", "/images/inMenu.png", null);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/mirrorLmenuRed.png", "/images/mirrorLmenu.png", gameItems.mirrorL);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/mirrorRmenuRed.png", "/images/mirrorRmenu.png", gameItems.mirrorR);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/gunRotationRed.png", "/images/gunRotation.png", gameItems.gunRotation);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/gunShootRed.png", "/images/gunShoot.png", gameItems.gunFire);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/freeSpace.png", "/images/freeSpace.png", null);
        addClickAction("/images/freeSpace2.png", "/images/freeSpace2.png", null);
        
        JButton odejit = new JButton("      Obnovit     ");
        odejit.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                
                //tady vyvolame udalost jako pozadavek na vycisteni herni desky
                //proto jsem pridal do rozhrani GameItemsListener metodu public void clearGameState();
                //a tady jeste pridam metodu, ktera tuto udalost aktivuje fireClearGameState (je podobna jako fireItemChanged)
                //repaint//
                
                fireClearGameState();
                
                //ted uz jen zbyva zaregistrovat posluchace teto udalosti
                //to udelame v LaserLight.java v metode spustHru()
                //
            }
     
});
        add(odejit);
        
    }
    
    private void addClickAction(String strActive, String strInactive, gameItems itemType){
        
        BufferedImage imgActive = readImg(strActive);
        BufferedImage imgInactive = readImg(strInactive);
      
        add(new IMGButton(imgActive, imgInactive, itemType));
    }
    
    private BufferedImage readImg(String name){
        
        try{
            return ImageIO.read(getClass().getResource(name));
        } catch(IOException ex){
            System.out.println("Nelze nacist obrazek pro herni menu.\n" + ex.getMessage());
            return null;
        }
    }

    @Override
    public Component add(Component comp) {
        Component c = super.add(comp);
        
        
        if(comp instanceof IMGButton){
            addToListIMGButton((IMGButton)comp);
        }
        
        return c;
    }
    
    private final ArrayList<GameItemsListener> glisteners = new ArrayList<>();
    
    public void addGameItemsListener(GameItemsListener list){
        glisteners.add(list);
    }
    
    private void fireClearGameState(){
        glisteners.forEach((list) -> list.clearGameState());
    }
    
    private void fireItemChanged(){
        glisteners.forEach((list) -> list.itemChanged(this.activeItem));
    }
    
    private class IMGButton extends JLabel{

        
        private final Icon iconActive, iconInactive;
        private final gameItems itemType;
        private boolean active;
        
        
        public IMGButton(BufferedImage imgActive, BufferedImage imgInactive, gameItems itemType) {            
            this.iconActive   = new ImageIcon(imgActive);
            this.iconInactive = new ImageIcon(imgInactive);
            this.itemType = itemType;
            
            this.active = false;
            
            
            addMouseListener(new MouseAdapter(){

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    
                    if(active){
                        deactivate();
                    } else {
                        activate();
                    }
                    
                    fireItemChanged();
                }
                
            });
            
            
            setIcon(this.iconInactive);
        }
        
        public void activate(){
            
            deactivateAllIMGButtons();
            
            
            this.active = true;
            activeItem = this.itemType;
            setIcon(this.iconActive);
        }
        
        public void deactivate(){
            activeItem = null;
            this.active = false;
            setIcon(this.iconInactive);
        }
        
        public boolean isActive(){
            return this.active;
        }
    }
    
    private static final ArrayList<IMGButton> imgButtons = new ArrayList<>();
    
    private static void deactivateAllIMGButtons(){
        imgButtons.forEach((imgButton) -> imgButton.deactivate());
    }
    
    private static void addToListIMGButton(IMGButton mb){
        imgButtons.add(mb);
    }
}