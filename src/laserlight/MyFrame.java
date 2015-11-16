package laserlight;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Laser Light");
    }

    private Point getCenterPoint() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();

        return new Point(
                (screenSize.width - windowSize.width) / 2,
                (screenSize.height - windowSize.height) / 2
        );
    }

    @Override
    public void pack() {
        super.pack();
        setLocation(getCenterPoint());
    }

}