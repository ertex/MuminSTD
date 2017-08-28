package muminstd;

import java.awt.image.BufferedImage;

public class Hattifnatt extends Tower {
// Hattifnatt and muminstd belongs to David Johansson Te2

    public Hattifnatt(int x, int y, BufferedImage img) {
        super(x, y, null, "Pistol.wav");

        setImg(img);
        System.out.println("A new Hattifnatt appeared");
        firerate = 30;
        damage = 40;
        cost = 200;
        range = 150;
        upgradeCost = 100;
        dmgUpgrade = 30;
        fireRateUpgrade = 5;
        rangeUpgrade = 20;

    }

    public int cost() {
        return cost;
    }

}
