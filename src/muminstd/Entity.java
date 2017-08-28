package muminstd;

import java.awt.image.BufferedImage;

/**
 *
 * @author Erik
 */
public class Entity extends Sprite {

    public Entity(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    //checks if clicked
    public boolean clicked(int x, int y) {
        if (x > this.x && x < this.x + width
                && y > this.y && y < this.y + height) {
            return true;
        }
        return false;
    }

}
