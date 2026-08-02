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
    public Lailaps(int hp, int maxHp){
        super(hp, maxHp);
    }

    public void loadImage(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/entities/lailaps.png"));
        }catch (IOException e){
            System.out.println("Couldn't load bat sprite: " + e.getMessage());
        }
    }

    /**
     * Returns Lailaps' current row position
     *
     *  @return current row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns Lailaps' current column position
     *
     *  @return current column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Returns Lailaps' current HP
     *
     * @return current HP
     */
    public float getHp() {
        return this.hp;
    }

    /**
     * Returns Lailaps' max HP
     *
     * @return max HP
     */
    public float getMaxHp() {
        return this.maxHp;
    }

    /**
     * Sets Lailaps' position
     *
     * @param row new row position
     * @param col new column position
     */
    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Reduces Lailaps' HP depending on damage
     *
     * @param damage amount of damage taken
     */
    public void takeDamage(float damage){
        this.hp -= damage;
        if(this.hp < 0)
            this.hp = 0; // to make sure hp is not gonna be a - value
    }

    /**
     * Checks if Lailaps is still alive
     *
     * @return true if HP is above 0, false if not
     */
    public boolean isAlive(){
        return this.hp > 0;
    }
}
