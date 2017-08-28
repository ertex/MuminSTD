package muminstd;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

public class Tower extends Entity {
// Tower and muminstd belongs to David Johansson Te2

    protected int firerate;
// Tower and muminstd belongs to David Johansson Te2

    protected int damage;
    protected int cost;
    protected int range;
    protected int lineDelay;
    protected int firedelay;
    protected String sound;
    private Enemy target;
    protected boolean canFire;
    protected int level, upgradeCost, dmgUpgrade, rangeUpgrade, fireRateUpgrade, maxLevel;

    public Tower(int x, int y, BufferedImage img, String sound) {
        super(x, y, img); //  could not use "-img.getHeight()" in order to fix issues with location issues, so I did some hardcoding

        this.sound = sound;
        firerate = 0; // Just making sure none of the values is not Null
        damage = 10;
        cost = 0;
        range = 0;
        lineDelay = 0;
        firedelay = 0;
        level = 1;
        upgradeCost = 0;
        dmgUpgrade = 0;
        rangeUpgrade = 0;
        fireRateUpgrade = 0;
        maxLevel = 3;
    }

    public void update(ArrayList<Enemy> enemies) {

        if (canFire) {

            for (Enemy u : enemies) {
                if (u.isAlive()) {
                    if (checkInRange(u)) { //checks if targeted enemy is in range checkInRange returns a boolean, the first enemy will be targeted and fired at
                        shoot(target); //You can never guess what this does...
                        canFire = false; // if it was in range, a shot was fired and the tower can't shoot until it's cooled down
                        firedelay = firerate;
                        break;
                    }
                }

            }
        }

        if (firedelay < 1) { //cools the tower down and allows it to shoot again
            canFire = true;
        } else {
            firedelay--;
        }

    }

    public boolean checkInRange(Enemy target) {

        if (sqrt(Math.pow((((x + width / 2) - (target.getX() + target.width / 2))), 2) + Math.pow((((y + height / 2) - (target.getY() + target.height / 2))), 2)) < range) { //does some math magic with distance, but Fredrik said "matte är min kryptonit" so this is not very detailed, sorry.

            this.target = target; //Saves target so it can be fetched from other methods
            return true;

        } else {
            return false;
        }

    }

    public void shoot(Enemy enemy) {
        enemy.getDamaged(damage);
        GameManager.playSound(sound);

        lineDelay = 5; //The amount of time the line will stay up. Sorry for hardcode

    }

    public int upgrade(int gold) {
        if (gold >= upgradeCost * level && level < maxLevel) {
            level++;
            damage += dmgUpgrade;
            range += rangeUpgrade;
            firerate -= fireRateUpgrade;
            return upgradeCost * level;
        }
        return 0;
    }

    public void draw(Graphics g2d) {

        g2d.drawRect(x, y, width, height);
        switch (level) {

            case 1:
                g2d.setColor(Color.green);
                break;
            case 2:
                g2d.setColor(Color.yellow);
                break;
            case 3:
                g2d.setColor(Color.red);
                break;
            default:
                g2d.setColor(Color.black);
                break;

        }

        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.black);
        super.draw(g2d);

        if (target != null) {
            if (checkInRange(target)) {
                if (lineDelay > 1) { //makes the bulletline stay a while so it's visible

                    g2d.drawLine(this.getX() + width / 2, this.getY() + height / 2, target.getX() + target.getWidth() / 2, target.getY() + target.getHeight() / 2); // this.x / y is protected and in sprite

                }
            }
        }
        lineDelay--;

    }

    /**
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the firerate
     */
    public int getFirerate() {
        return firerate;
    }

    /**
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * @return the firedelay
     */
    public int getFiredelay() {
        return firedelay;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the upgradeCost
     */
    public int getUpgradeCost() {
        return upgradeCost;
    }

    /**
     * @return the dmgUpgrade
     */
    public int getDmgUpgrade() {
        return dmgUpgrade;
    }

    /**
     * @return the rangeUpgrade
     */
    public int getRangeUpgrade() {
        return rangeUpgrade;
    }

    /**
     * @return the fireRateUpgrade
     */
    public int getFireRateUpgrade() {
        return fireRateUpgrade;
    }

}
