package laserlight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaserLight extends JPanel {
    
    
    private static final int[][] herniPole = new int[][]{
        
        /*18*/
/*20*/  {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 3, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 0, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 },
   
        }; 

    public static void main (String args[]) throws Exception {

        final JFrame menu = new JFrame("Menu");
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menu.setVisible(true);
        menu.setLayout(null);
        menu.setSize(706, 659);
        menu.setResizable(false);
        menu.setLocationRelativeTo(null);
        
        JButton bttStart = new JButton("Hrát");
        menu.add(bttStart);
        bttStart.setSize(70, 50);
        bttStart.setLocation(300, 215);
        bttStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                try {
                    spustHru();
                    menu.dispose();
                } catch (Exception ex) {
                    Logger.getLogger(LaserLight.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }   
});
        
        JButton bttQuit = new JButton("Odejít");
        menu.add(bttQuit);
        bttQuit.setSize(70, 50);
        bttQuit.setLocation(300, 315);
        bttQuit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                menu.dispose();
                
            }   
});   
     
    }

    public static  void spustHru() throws Exception{
        
        System.out.println("Hra spustena.");
        final JFrame window = new MyFrame();
        final Board board = new Board(herniPole.length, herniPole[0].length);
        board.newStateGame(herniPole);

        board.addShotEvent((target, tartgetsList) -> {
            System.out.println("Tento cil byl zasazen: " + target.toString());
            
            if(tartgetsList.isEmpty()){
                System.out.println("Zadne dalsi cile nejsou definovany.");
            }
        });
        
        
        final GameMenu gmenu = new GameMenu();
        /* ******************************** */
        /*  POZOR                           */
        /* ******************************** */
        //V rozhrani GameItemsListener uz neni jedna metoda, proto nelze takto pouzit lambda operator

//        gmenu.addGameItemsListener((itemType) -> {
//            board.activeItem(itemType);
//            System.out.println(itemType);        
//        });
        
        gmenu.addGameItemsListener(new GameItemsListener() {

            @Override
            public void itemChanged(GameItemsListener.gameItems itemType) {
                board.activeItem(itemType);
                System.out.println(itemType); 
            }

            @Override
            public void clearGameState() {
                //tady rekneme tride board at se vycisti a tam budeme implementovat algoritmus
                board.clearMirrors();
            }
        });
        
        
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        mainPanel.setBackground(Color.GRAY);
        
        mainPanel.add(board);
        mainPanel.add(gmenu);
        
        window.add(mainPanel);
        window.pack();
        window.setVisible(true);
        
    } 
   
}
