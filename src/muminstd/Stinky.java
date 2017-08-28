package muminstd;

import java.awt.image.BufferedImage;

public class Stinky extends Tower {
// Stinky and muminstd belongs to David Johansson Te2

    public Stinky(int x, int y, BufferedImage img) {
        super(x, y, null, "Thomson.wav");

        setImg(img);
        System.out.println("A new Stinky appeard");
        firerate = 18;
        damage = 20;
        cost = 300;
        range = 150;
        upgradeCost = 120;
        dmgUpgrade = 13;
        fireRateUpgrade = 3;
        rangeUpgrade = 30;

    }
}
