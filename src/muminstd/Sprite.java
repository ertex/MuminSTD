package muminstd;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

    protected int x;
    protected int y;
    protected int height;
    protected int width;

    protected BufferedImage img;

    Sprite(int x, int y, BufferedImage img) {
        try {
            this.img = ImageIO.read(new File("src\\Images\\Default.jpg"));//This sets the default image of a newly created image
        } catch (IOException e) {
            System.out.println("Sprite Image");
        }
        this.x = x;
        this.y = y;
        setImg(img);
    }

    public void draw(Graphics g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the img
     */
    public BufferedImage getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(BufferedImage img) {
        if (img != null) {
            this.img = img;
            width = img.getWidth();
            height = img.getHeight();
        }
    }

}
