package laserlight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public final class LaserGun extends GameObjects {

    private Laser offspringLight;

    private Point smerHlavne = new Point(1, 0);

    private Color[] barvyLaseru = new Color[]{
        Color.RED,};

    private final Color barvaLaseru;
    private boolean fired = false;

    private Point ustiHlavne = null;

    public LaserGun() {

        BufferedImage cannon = null;

        try {
            cannon = ImageIO.read(getClass().getResource("/images/cannon.png"));
        } catch (IOException ex) {
            System.out.println("Chyba pri nacitani zbrane.");
        }

        initObjekt(cannon);

        this.ustiHlavne = new Point(getImageSize().width / 2, getImageSize().height / 2);
        this.barvaLaseru = barvyLaseru[new Random().nextInt(barvyLaseru.length)];

    }

    public void rotujLaserGun() {

        if (this.fired) {
            System.out.println("Delo nelze otacet, je v provozu.");
            return;
        }

        smerHlavne = new Point(-smerHlavne.y, smerHlavne.x);

        ustiHlavne = new Point(getImageSize().width / 2, getImageSize().height / 2);

        BufferedImage novy = new BufferedImage(getImageObjekt().getHeight(), getImageObjekt().getWidth(), getImageObjekt().getType());
        int radekT, sloupecT;
        for (int radek = 0; radek < getImageObjekt().getHeight(); radek++) {
            for (int sloupec = 0; sloupec < getImageObjekt().getWidth(); sloupec++) {

                radekT = sloupec;
                sloupecT = -radek + novy.getWidth() - 1;

                novy.setRGB(sloupecT, radekT, getImageObjekt().getRGB(sloupec, radek));
            }
        }

        super.initObjekt(novy);
    }

    private Point generujSmerHlavne(int imgW, int imgH) {
        return new Point(
                getSmerHlavne().x * imgW / 2 + imgW / 2,
                getSmerHlavne().y * imgH / 2 + imgH / 2
        );
    }

    public Point getPointOfStartLaser() {
        return this.ustiHlavne;
    }

    public Color getBarvaLaseru() {
        return this.barvaLaseru;
    }

    public Point getSmerHlavne() {
        return new Point(this.smerHlavne.x, this.smerHlavne.y);
    }

    @Override
    protected void initObjekt(BufferedImage buffImg) {

        super.initObjekt(buffImg);
    }

    public void fireSwitch() {
        this.fired = !this.fired;

        if (this.fired) {
            this.offspringLight = Laser.addLaser(getBarvaLaseru(),
                    genLocations(getPointOfStartLaser(),
                            new Point(getLocation().x * getImageSize().width,
                                    getLocation().y * getImageSize().height)),
                    getLocation(),
                    getSmerHlavne(),
                    hashCode(),
                    getFather());
        } else {

            this.offspringLight.dispose();
        }

    }

    public boolean isFired() {
        return this.fired;
    }

    @Override
    public Point transformujVektor(Point dir) {
        return null;
    }

    private Point genLocations(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
}