package GUI;

import Dungeon.Floor;
import Dungeon.Tile;
import Items.Item;
import LivingThings.Bat;
import LivingThings.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    //lets outside classes know if player reaches exit
    public interface FloorCompleteListener{
        void onFloorComplete();
    }

    // lets outside classes know if player ded
    public interface PlayerDeathListener{
        void onPlayerDeath(String cause);
    }

    private Floor floor;
    private Player player;

    private static final int TILE_SIZE = 25;

    private static final int TOP_BAR_HEIGHT = 30;
    private static final int BOTTOM_BAR_HEIGHT = 40;

    private Timer batTimer;
    private Timer heatTimer;

    private FloorCompleteListener floorCompleteListener;
    private PlayerDeathListener playerDeathListener;

    public GamePanel(Floor floor){
        this.floor = floor;
        this.player = floor.getPlayer();

        int mapRows = floor.getMap().length;
        int mapCols = floor.getMap()[0].length;

        int width = mapCols * TILE_SIZE;
        int height = TOP_BAR_HEIGHT + (mapRows * TILE_SIZE) + BOTTOM_BAR_HEIGHT;
        setPreferredSize(new Dimension(width,height));

        setFocusable(true);
        addKeyListener(this);

        startBatTimer();
        startHeatTimer();
    }

    //------------------------
    // setters
    //------------------------
    public void setFloorCompleteListener(FloorCompleteListener listener) {
        this.floorCompleteListener = listener;
    }

    public void setPlayerDeathListener(PlayerDeathListener playerDeathListener) {
        this.playerDeathListener = playerDeathListener;
    }

    private void startBatTimer(){
        batTimer = new Timer(500, e->{floor.moveBats(); repaint();});
        batTimer.start();
        checkForPlayerDeath();
    }

    private void startHeatTimer(){
        heatTimer = new Timer(1000, e->{floor.checkHeatDamage();repaint();});
        heatTimer.start();
        checkForPlayerDeath();
    }

    public void stopTimers(){
        if(batTimer != null) batTimer.stop();
        if(heatTimer != null) heatTimer.stop();
    }

    /**
     * Checks whether the player is currently standing on the exit tile
     * ('E'). If so, tells whoever is listening (GameFrame) that this floor
     * is complete, so they can decide what happens next.
     */
    private void checkForFloorComplete(){
        char currentSymbol = floor.getMap()[player.getRow()][player.getCol()].getSymbol();
        if(currentSymbol == 'E' && floorCompleteListener != null){
            floorCompleteListener.onFloorComplete();
        }
    }

    /**
     * Checks whether the player has died. If so, tells whoever is
     * listening so it can show a game over screen.
     */
    private void checkForPlayerDeath(){
        if(!player.isAlive() && playerDeathListener != null){
            playerDeathListener.onPlayerDeath(player.getDeathCause());
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // clear panel before drawing new frame

        drawTopBar(g);
        drawMap(g);
        drawBottomBar(g);
    }

    private void drawTopBar(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0, getWidth(), TOP_BAR_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(floor.getDungeonName(), 10, 20);

        String floorText = "Floor #" + floor.getFloorNumber();
        //right align
        int textWidth = g.getFontMetrics().stringWidth(floorText);
        g.drawString(floorText, getWidth()-textWidth-10, 20);
    }

    private void drawBottomBar(Graphics g){
        int barY = TOP_BAR_HEIGHT + (floor.getMap().length * TILE_SIZE);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, barY, getWidth(), BOTTOM_BAR_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        String hpText = "HP: " + player.getHp() + "/" + player.getMaxHP();
        String goldText = "Gold: " + player.getGold();

        Item currentItem = player.getCurrentItem();
        String itemText = "Item: " + (currentItem == null ? "None" : currentItem.getName());

        int textY = barY + 20; // vertically center the text inside the bar
        g.drawString(hpText, 10, textY);
        g.drawString(goldText, 150, textY);
        g.drawString(itemText, 280, textY);
    }

    private void drawMap(Graphics g){
        Tile[][] map = floor.getMap();

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE + TOP_BAR_HEIGHT;

                Image tileImage = map[i][j].getImage();
                if (tileImage != null) {
                    // draw the sprite, scaled to exactly 32 x 32
                    g.drawImage(tileImage, x, y, TILE_SIZE, TILE_SIZE, this);
                } else {
                    // fallback in case a sprite failed to load, so nothing crashes
                    g.setColor(getTileFallbackColor(map[i][j]));
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // draw bats
        for (Bat bat : floor.getBats()) {
            int x = bat.getCol() * TILE_SIZE;
            int y = bat.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;

            if (bat.getImage() != null) {
                g.drawImage(bat.getImage(), x, y, TILE_SIZE, TILE_SIZE, this);
            } else {
                g.setColor(Color.RED);
                g.fillOval(x + 4, y + 4, TILE_SIZE - 8, TILE_SIZE - 8);
            }
        }

        // draw player
        int px = player.getCol() * TILE_SIZE;
        int py = player.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;

        if (player.getImage() != null) {
            g.drawImage(player.getImage(), px, py, TILE_SIZE, TILE_SIZE, this);
        } else {
            g.setColor(Color.CYAN);
            g.fillOval(px + 2, py + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        }
    }

    private Color getTileFallbackColor(Tile tile) {
        switch (tile.getSymbol()) {
            case '*':
                return Color.BLACK;
            case '.':
                return new Color(200, 230, 200);
            case 'v':
                return new Color(120, 80, 40);
            case 'x':
                return Color.GRAY;
            case 'w':
                return Color.BLUE;
            case 'h':
                return Color.ORANGE;
            case 'T':
                return Color.YELLOW;
            case 'E':
                return Color.GREEN;
            case 'g':
                return new Color(255, 215, 0);
            default:
                return Color.MAGENTA;
        }
    }

    //------------------------
    // KP input
    //------------------------
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                floor.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_S:
                floor.movePlayer(1, 0);
                break;
            case KeyEvent.VK_A:
                floor.movePlayer(0, -1);
                break;
            case KeyEvent.VK_D:
                floor.movePlayer(0, 1);
                break;
            case KeyEvent.VK_SPACE:
                player.useCurrentItem(floor);
                break;
            case KeyEvent.VK_OPEN_BRACKET:
                player.switchToPreviousItem();
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                player.switchToNextItem();
                break;
        }
        repaint();
        checkForPlayerDeath();

        if(player.isAlive())
            checkForFloorComplete();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
