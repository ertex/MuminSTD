package muminstd;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {
// Enemy and muminstd belongs to David Johansson Te2

    int hp, maxHp, gold, speed, moveDelay, nextY, nextX, tileSize;
    private boolean alive, canMove;
    private String deathZero, deathOne, deathTwo;
    private BufferedImage bloodImg;
    private int nextWaypoint;
    private int bloodDelay;
    private boolean collected;
    private Random rnd;

    public Enemy(int x, int y, int type, int wave, BufferedImage img, String deathZero, String deathOne, String deathTwo, BufferedImage bloodImg) {
        super(x, y, img); //Img gets declared further down, this is ugly I know

        this.bloodImg = bloodImg;
        this.deathZero = deathZero;
        this.deathOne = deathOne;
        this.deathTwo = deathTwo;

        tileSize = Map.tileSize;
        rnd = new Random();

        collected = false;  //checks if the gold of the enemy was collected

        bloodDelay = 10; //how often the blood gains size

        System.out.println("Enemy Created");

        if (type == 0) {
            //Muminpappa Is Type #0
            gold = 40;
            speed = 2; //lower is faster
            hp = (int) ((int) 160 * (Math.pow(wave, 2) / 4));
            maxHp = hp;
        }
        //Preset 1 
        if (type == 1) {
            //"Lilla My" Is Type #1
            gold = (int) 30;
            speed = 1;//lower is faster
            hp = (int) ((int) 70 * (Math.pow(wave, 2) / 8));
            maxHp = hp;
        }

        alive = true;
        nextWaypoint = 0;

    }

    public void draw(Graphics2D g2d) {
        if (alive) {
            super.draw(g2d);
            g2d.setColor(Color.RED);//The following 4 lines is a hp-bar
            g2d.fillRect(x, y + 45, 50, 5);//background color
            g2d.setColor(Color.GREEN);
            g2d.fillRect(x, y + 45, (hp * 50 / maxHp), 5); // overlaycolor. I have it as (hp*50/maxHp) instead of 50*(hp/maxHp) due to converting to int issues
            g2d.setColor(Color.BLACK);
        } else {
            g2d.drawImage(bloodImg, x - (((width - tileSize) / 2)), y - (((height - tileSize) / 2)), width, height, null);// this makes the gorey stuff happen
            if (bloodDelay > 3) {
                bloodDelay = 0; //More gorey stuff
                width++;
                height++;
            }
            bloodDelay++;

        }

    }

    public void update(ArrayList<Integer> waypoints) {

        if (hp <= 0 && alive == true) {
            switch (rnd.nextInt(3)) {
                case 0:

                    GameManager.playSound(deathZero);
                    break;

                case 1:

                    GameManager.playSound(deathOne);
                    break;

                case 2:

                    GameManager.playSound(deathTwo);
                    break;
            }
            alive = false;
        }

        if (alive) {
            if (waypoints.size() == nextWaypoint) {

                Map.lost = true;

            }

            if (canMove) {
                move();//-----------------------------------
                canMove = false; //This is to regulte speed, see movedelay and speed
                if (x < nextX + tileSize / 2 & x > nextX - tileSize / 2 & y < nextY + tileSize / 2 & y > nextY - tileSize / 2) {

                    checkNextWaypoint(waypoints);
                    nextWaypoint += 2; //Moves 2 steps due to the waypoints is stacked in a one dimentional array on odds and evens
                }
                moveDelay = speed;
            }

            if (moveDelay < 1) { //This is to regulate how often enemy can move
                canMove = true;
            } else {
                moveDelay--;
            }
        }
    }

    public void move() {//This homes the Enemy to the Next X/Y value

        if (nextY > y) {
            y += 4;

        }
        if (nextY < y) {
            y -= 4;

        }
        if (nextX > x) {
            x += 4;

        }
        if (nextX < x) {
            x -= 4;

        }

    }

    public void checkNextWaypoint(ArrayList<Integer> waypoints) {//This looks in the the waypoints and finds the next appropriate waypoint

        nextY = waypoints.get(nextWaypoint); //The Y cordinates are on the even numbers and X on odds
        nextX = waypoints.get(nextWaypoint + 1);

    }

    public void getDamaged(int hp) {
        this.hp = this.hp - hp;
        if (hp < 1) {
            alive = false;
        }

    }

    public boolean isAlive() {
        return alive;
    }

    public int getGold() {
        return gold;
    }

    public int gethp() {
        return hp;
    }

    /**
     * @return the collected
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * @param collected the collected to set
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

}
