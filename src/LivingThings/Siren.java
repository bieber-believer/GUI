package LivingThings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Siren extends Entity{
    private boolean isReleased;
    private BufferedImage image; // for sprite

    public Siren( int row, int col){
        super("Siren", row, col, 750, true, 100f);
    }

    public void loadImage(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/entities/siren.png"));
        }catch (IOException e){
            System.out.println("Couldn't load siren sprite: " + e.getMessage());
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
