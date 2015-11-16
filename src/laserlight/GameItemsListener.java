package laserlight;

public interface GameItemsListener {

    enum gameItems {mirrorL, mirrorR, gunRotation, gunFire}
    
    void itemChanged(gameItems item);
    
    void clearGameState();
}
