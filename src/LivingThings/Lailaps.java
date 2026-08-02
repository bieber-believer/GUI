package LivingThings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Lailaps extends Entity{
    private BufferedImage image; // for sprite

    /**
     * Creates Lailaps
     *
     *  @param hp current HP
     *  @param maxHp maximum hp
     */
    public Lailaps(float hp, float maxHp){
        super(hp, maxHp);
    }

    public void loadImage(){
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entities/lailaps.png"));
        } catch (IOException e){
            System.out.println("Couldn't load Lailaps sprite: " + e.getMessage());
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Checks whether Lailaps is still alive
     *
     * @return true if alive, false otherwise
     */
    public boolean isAlive(){
        return getHp() > 0;
    }
}
