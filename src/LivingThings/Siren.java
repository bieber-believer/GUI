package LivingThings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Siren extends Entity{
    private boolean isReleased;
    private BufferedImage image; // for sprite

    public Siren(String name, int row, int col, int goldDrop, boolean movesDiagonal, float atkDamage){
        super(name, row, col, goldDrop, movesDiagonal, atkDamage);
    }

    public void loadImage(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/entities/siren.png"));
        }catch (IOException e){
            System.out.println("Couldn't load bat sprite: " + e.getMessage());
        }
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void spawnBat(){

    }

    public void move(){

    }
}
