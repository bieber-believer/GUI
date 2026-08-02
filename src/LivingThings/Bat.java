package LivingThings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bat extends Entity{
    private BufferedImage image; // bat sprite

    public Bat(int row, int col, int goldDrop, boolean movesDiagonal, float atkDamage){
        super("Bat", row, col, goldDrop, movesDiagonal, atkDamage);
    }

    public void loadImage(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/entities/bat.png"));
        }catch (IOException e){
            System.out.println("Couldn't load bat sprite: " + e.getMessage());
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
