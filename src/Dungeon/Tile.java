package Dungeon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {
    protected int row, col;
    protected char symbol;

    //properties of the tiles
    protected float damage;
    protected boolean isPassable;
    protected boolean isDiggable;
    protected BufferedImage image; //for the sprite image
    protected int goldValue; // how much gold is on the tile (0 means no gold)

    public Tile(char symbol, int row, int col){
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    public void tileProperties() throws IOException {
        switch (this.symbol){
            case '*': // border
                this.isPassable = false;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/border.png"));
                break;
            case '.': // passable tile
                this.isPassable = true;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
                break;
            case 'v': // wall tile
                this.isPassable = false;
                this.isDiggable = true;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
                break;
            case 'x': // spike tile
                this.isPassable = false;
                this.isDiggable = true;
                this.damage = 0.5f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/spike.png"));
                break;
            case 'w': // water tile
                this.isPassable = false;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
                break;
            case 'h': // heat tile
                this.isPassable = true;
                this.isDiggable = false;
                this.damage = 1.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/heat.png"));
                break;
            case 'T': // treasure tile
                this.isPassable = false;
                this.isDiggable = true;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/treasure.png"));
                break;
            case 'E': // exit tile
                this.isPassable = true;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/exit.png"));
                break;
            case 'g': // gold tile
                this.isPassable = true;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/gold.png"));
                break;
            case '0': //the switch for the boss fight
                this.isPassable = true;
                this.isDiggable = false;
                this.damage = 0.0f;
                image = ImageIO.read(getClass().getResourceAsStream("/tiles/switch.png")); // change to switch sprite
                break;
            case 'Y': // yohane tile
            case 'L': // lailaps tile
            case 'b': // bat tile
            case 'S': // siren tile
                break;
            default:
                System.out.println("Yo, this isn't supposed to happen, contact developer");
        }
    }

    //------------------------
    // getters
    //------------------------
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getSymbol() {
        return symbol;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public boolean isDiggable() {
        return isDiggable;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getGoldValue() {
        return goldValue;
    }

    //------------------------
    // setters
    //------------------------

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public void setDiggable(boolean diggable) {
        isDiggable = diggable;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }
}
