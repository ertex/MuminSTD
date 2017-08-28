/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muminstd;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Erik
 */
public class GameManager {

    static long delta;

    private TowerType tower;

    private long playTime;
    private Tower infoTower;

    private boolean waveActive;

    private BufferedImage mapImg;

    private int gold;

    private long musicStartTime;//This varibles only purose is to loop the background music
    private long musicPlayTime;

    private Map map;

    GameManager() {

        try {

            mapImg = ImageIO.read(new File("src\\Images\\map.png"));

        } catch (IOException e) {
            System.out.println("Map image ");
        }

        //playSound("Song.wav");
        init();
    }

    public void init() {

        map = new Map(mapImg);
        tower = TowerType.NON;
        waveActive = false;
        playTime = 0;
        map.generateWave();
        gold = 400;
        musicStartTime = System.currentTimeMillis() / 1000;

    }

    public static void playSound(final String url) { //Plays a Sound, Duh
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src\\Sounds\\" + url));
            clip.open(inputStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

        playTime = System.currentTimeMillis() / 1000;
        musicPlayTime = (System.currentTimeMillis() / 1000) - musicStartTime; //Calculates how long the music has been playing
        if (musicPlayTime > 120) {
            musicStartTime = System.currentTimeMillis() / 1000;
            musicPlayTime = 0;
            playSound("Song.wav");
        }

        //checks if wave is currently running
        if (waveActive) {

            if (!map.isEnemysAlive()) {
                waveActive = false;

                //wave++
                map.nextWave();

                //generates wave
                map.generateWave();
            } else {
                //updates the map
                map.enemiesUpdate();
                map.towersUpdate();

                //get gold from dead people
                gold += map.collectGold();
            }
        }

    }

    public void draw(Graphics2D g2d) {

        map.draw(g2d);
        if (infoTower != null) {
            g2d.drawString("Level: " + infoTower.getLevel() + "    Upgrade Cost: " + infoTower.upgradeCost * infoTower.level, 100, 475);
            g2d.drawString("Dmg: " + infoTower.getDamage() + "  Upgrade: + " + infoTower.getDmgUpgrade(), 100, 500);
            g2d.drawString("Attack Speed: " + infoTower.getFirerate() + "  Upgrade: - " + infoTower.getFireRateUpgrade(), 100, 525);
            g2d.drawString("Range: " + infoTower.getRange() + "  Upgrade: + " + infoTower.getRangeUpgrade(), 100, 550);
            g2d.drawImage(infoTower.getImg(), 300, 500, null);
        } else {
            g2d.drawString("Dmg: ", 100, 500);
            g2d.drawString("Attack Speed: ", 100, 525);
            g2d.drawString("Range: ", 100, 550);
            g2d.drawRect(300, 500, 50, 50);

        }

    }

    public void startButton() {
        waveActive = !waveActive;
    }

    public void clearButton() {
        if (!waveActive) {
            map.cleanEnemies();
        }
    }

    public void upgradeButton() {
        if (infoTower != null) {
            //return gold cost and subtracts the gold int
            gold -= infoTower.upgrade(gold);
        }
    }

    public void mousePressed(MouseEvent e) {

        //return gold cost and subtracts the gold int
        gold = gold - map.addTower(e.getX(), e.getY(), tower, gold);

        //set the current clicked tower to the infoTower
        infoTower = map.clicked(e.getX(), e.getY());

        tower = TowerType.NON;

    }

    /**
     * @param tower the tower to set
     */
    public void setTowerType(TowerType tower) {
        this.tower = tower;
    }

    /**
     * @return the playTime
     */
    public long getPlayTime() {
        return playTime;
    }

    /**
     * @param playTime the playTime to set
     */
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    /**
     * @return the tower
     */
    public TowerType getTowerType() {
        return tower;
    }

    /**
     * @return the waveActive
     */
    public boolean isWaveActive() {
        return waveActive;
    }

    /**
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    public enum TowerType {
        NON, HATIFNATT, MORRAN, STINKY;
    }

}
