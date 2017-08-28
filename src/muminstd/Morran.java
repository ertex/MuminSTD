package muminstd;

import java.awt.image.BufferedImage;

public class Morran extends Tower {
// Morran and muminstd belongs to David Johansson Te2

    public Morran(int x, int y, BufferedImage img) {
        super(x, y, null, "Sniper.wav");

        setImg(img);

        System.out.println("A new Morran appeard");
        firerate = 150;
        damage = 100;
        cost = 400;
        range = 200;
        upgradeCost = 150;
        dmgUpgrade = 200;
        fireRateUpgrade = 50;
        rangeUpgrade = 200;

    }

}
