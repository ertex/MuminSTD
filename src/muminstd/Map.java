package muminstd;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Map extends Sprite {
// Map and muminstd belongs to David Johansson Te2

    //Map is basicly a big container for things that will be doing stuff, like towers and enemies
    static ArrayList<Enemy> enemies; //Add Motivation here    maby make this static to make it easier for Tower
    private ArrayList<Tower> towers;
    private ArrayList<Integer> waypoints;
    private int collitionMap[][], wave;
    static final int tileSize = 50, mapSize = 9; //tilesize can be modified, but DONT TOUCH mapSize! it tells the size of collitionMap[][]
    static final int mapPixelSize = tileSize * mapSize;

    private BufferedImage hatifnattImg;
    private BufferedImage morranImg;

    private BufferedImage bloodImg;

    private BufferedImage myImg;
    private BufferedImage muminPappaImg;
    private BufferedImage stinkyImg;

    static boolean lost;

    public Map(BufferedImage img) {
        super(0, 0, img);

        try {
            hatifnattImg = ImageIO.read(new File("src\\Images\\Hattifnatt.png"));
            morranImg = ImageIO.read(new File("src\\Images\\Morran.png"));

            bloodImg = ImageIO.read(new File("src\\Images\\Blood.png"));

            myImg = ImageIO.read(new File("src\\Images\\My.png"));
            muminPappaImg = ImageIO.read(new File("src\\Images\\Muminpappa.png"));
            stinkyImg = ImageIO.read(new File("src\\Images\\Stinky.png"));

        } catch (IOException e) {
            System.out.println("Hatifnatt.png not found");
        }

        wave = 1;
        enemies = new ArrayList();
        towers = new ArrayList();
        waypoints = new ArrayList();
        lost = false;
        generateMap();//Generates the map sepperatly

    }

    public void enemiesUpdate() {//updates enemies

        for (Enemy u : enemies) {

            if (u.isAlive()) {
                u.update(waypoints);
            }
        }

    }

    public void towersUpdate() {//updates towers

        for (Tower o : towers) {
            o.update(enemies);

        }
    }

    public int collectGold() {// collects all the gold from the dead bodies
        int n = 0;

        for (Enemy e : enemies) {
            //checks if the enemy has been collected and checks  the state of livingness
            if (!e.isCollected() && !e.isAlive()) {

                n += e.getGold();
                e.setCollected(true);
            }
        }

        return n;
    }

    public void draw(Graphics2D g2d) {//draws the whole map
        super.draw(g2d);

        for (Enemy u : enemies) {
            u.draw(g2d);
        }
        for (Tower o : towers) {
            o.draw(g2d);
        }

    }

    public void generateMap() {
        collitionMap = new int[][]{
            {1, 1, 4, 0, 0, 5, 1, 1, 1},
            {1, 1, 0, 1, 1, 0, 1, 1, 1},
            {2, 0, 3, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 0, 1, 10, 11},
            {1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 7, 0, 0, 0, 6, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 8, 0, 0, 0, 0, 0, 9, 1}};
        //Numbers that == 1 wil e buildable tiles, where you can place towers
        //Numbers that =/= 1 will be unbuildable tiles
        //Numbers >1 will be waypoint for Enemy to follow (They follow a incremental pattern)
        for (int k = 0; k < 11; k++) { //The k < 11, 11 is the last waypoint of the collitionMap and k=2 is the first
            for (int i = 0; i < mapSize; i++) {
                for (int u = 0; u < mapSize; u++) {

                    if (collitionMap[i][u] == k + 2) {//COMMENT THIS WHEN TIME
                        waypoints.add(i * tileSize);
                        waypoints.add(u * tileSize);
                    }
                }
            }
        }

    }

    public void generateWave() {

        for (int i = 0; i < 10 * wave; i++) {// This will be 10 enemies
            int type;
            BufferedImage enemyImg;

            if ((wave % 2) == 0) { //checks if the int is even or odd
                type = 0;           //Then sets the enemy type depending on the Mod of the wave
                enemyImg = muminPappaImg;//And sets the approtrate image
            } else {
                type = 1;
                enemyImg = myImg;
            }
            type = 0;
            enemies.add(new Enemy(x - i * 60, y, type, wave, enemyImg, "death0.wav", "death1.wav", "death2.wav", bloodImg));//generates new enemies, the +i*15 is to seperate them so they won't be as tighly packed

        }
    }

    public void cleanEnemies() { //Removes all the dead Enemy from enemies, hence making the ArryList smaller
        for (int i = 0; i < enemies.size(); i++) { //The reason That I use this overly complex system instead of for(Enemy u : enemies) is that it generates ConcurrentModificationException
            if (enemies.size() >= i) {
                Enemy u = enemies.get(i);
                if (!u.isAlive()) { //checks if the enemy is dead
                    enemies.remove(u);
                }
            }
        }
    }

    public boolean checkTile(int x, int y) {
        System.out.println("You Clicked Tile " + (int) x / tileSize + " , " + (int) y / tileSize);
        if (x < mapPixelSize & y < mapPixelSize & mapSize >= (int) (y / tileSize) & mapSize >= (int) (x / tileSize)) {
            if (collitionMap[(int) (y / tileSize)][(int) (x / tileSize)] == 1) { //Makes sure the click is within the borders of the placement bounds

                //Compres the XY Pos with the collitionmap[][] íf the placement i legal. 
                // (int)(x/y)/tilesize determins what placement in the array the click was.
                return true;

            } else {
                System.out.println("Placement out of MapBounds");
                return false;
            }

        } else {
            return false;
        }
    }

    public int addTower(int x, int y, GameManager.TowerType tower, int gold) { // Overly complicated method for adding a tower
        //checks if your holding a tower to place
        if (tower != GameManager.TowerType.NON) {
            //checks if you can place a tower on the current tile
            if (checkTile(x, y)) {
                collitionMap[(int) y / tileSize][(int) x / tileSize] = 0;
                //adds hattifnatt
                if (tower == GameManager.TowerType.HATIFNATT) {

                    towers.add(new Hattifnatt((int) (x / tileSize) * tileSize, (int) (y / tileSize) * tileSize, hatifnattImg));
                    //adds Morran
                } else if (tower == GameManager.TowerType.MORRAN) {
                    towers.add(new Morran((int) (x / tileSize) * tileSize, (int) (y / tileSize) * tileSize, morranImg));
                    //adds Stinky
                } else if (tower == GameManager.TowerType.STINKY) {
                    towers.add(new Stinky((int) (x / tileSize) * tileSize, (int) (y / tileSize) * tileSize, stinkyImg));

                }
                //checks if the tower you placed is to expensive. if it is then the tower will be removed
                if (towers.get(towers.size() - 1).getCost() > gold) {
                    towers.remove(towers.size() - 1);
                    collitionMap[(int) x / tileSize][(int) y / tileSize] = 1;
                    return 0;
                }
                return towers.get(towers.size() - 1).getCost();

            }
        }
        return 0;

    }
//get clicked tower

    public Tower clicked(int x, int y) {

        for (Tower t : towers) {
            if (t.clicked(x, y)) {
                return t;
            }

        }

        return null;
    }

    public boolean isEnemysAlive() {

        for (Enemy e : enemies) {
            if (e.isAlive()) {
                return true;

            }

        }

        return false;
    }

    public void nextWave() {
        wave++;
    }
}
