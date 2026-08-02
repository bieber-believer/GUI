package GUI;

import Dungeon.BossFight;
import Dungeon.Tile;
import Items.Item;
import LivingThings.Bat;
import LivingThings.Lailaps;
import LivingThings.Player;
import LivingThings.Siren;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BossFightPanel extends JPanel implements KeyListener {
    public interface GameWonListener{
        void onGameWon();
    }

    public interface GameOverListener{
        void onGameOver(String cause);
    }

    private BossFight bossFight;
    private Player player;
    private Lailaps lailaps;

    private static final int TILE_SIZE = 20;
    private static final int TOP_BAR_HEIGHT = 30;
    private static final int BOTTOM_BAR_HEIGHT = 46;

    private GameWonListener gameWonListener;
    private GameOverListener gameOverListener;

    private boolean battleEnded = false;

    public BossFightPanel(BossFight bossFight){
        this.bossFight = bossFight;
        this.player = bossFight.getPlayer();
        this.lailaps = bossFight.getLailaps();

        int mapRows = bossFight.getMap().length;
        int mapCols = bossFight.getMap()[0].length;

        int width = mapCols * TILE_SIZE;
        int height = TOP_BAR_HEIGHT + (mapRows * TILE_SIZE) + BOTTOM_BAR_HEIGHT;
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(this);
    }

    public void setGameWonListener(GameWonListener listener) {
        this.gameWonListener = listener;
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    //------------------------
    // input
    //------------------------
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(battleEnded) return;

        switch(e.getKeyCode()){
            case KeyEvent.VK_W: bossFight.moveCharacters(-1, 0); break;
            case KeyEvent.VK_S: bossFight.moveCharacters(1, 0); break;
            case KeyEvent.VK_A: bossFight.moveCharacters(0, -1); break;
            case KeyEvent.VK_D: bossFight.moveCharacters(0, 1); break;
            case KeyEvent.VK_SPACE:
                player.useCurrentItem(bossFight); // BossFight IS a Floor, so this just works
                break;
            case KeyEvent.VK_OPEN_BRACKET: player.switchToPreviousItem(); break;
            case KeyEvent.VK_CLOSE_BRACKET: player.switchToNextItem(); break;
        }

        repaint();
        checkBattleOutcome();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void checkBattleOutcome(){
        if(bossFight.isBattleLost()){
            battleEnded = true;
            if(gameOverListener != null) gameOverListener.onGameOver(bossFight.getLossCause());
            return;
        }

        if(bossFight.isBattleWon()){
            char currentSymbol = bossFight.getMap()[player.getRow()][player.getCol()].getSymbol();
            if(currentSymbol == 'E' && gameWonListener != null){
                battleEnded = true;
                gameWonListener.onGameWon();
            }
        }
    }

    //------------------------
    // drawing
    //------------------------
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTopBar(g);
        drawMap(g);
        drawBottomBar(g);
    }

    private void drawTopBar(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), TOP_BAR_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString(bossFight.getDungeonName(), 10, 20);
    }

    private void drawBottomBar(Graphics g){
        int barY = TOP_BAR_HEIGHT + (bossFight.getMap().length * TILE_SIZE);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, barY, getWidth(), BOTTOM_BAR_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.WHITE);

        String hpText = "HP: " + player.getHp() + "/" + player.getMaxHP();
        String lailapsText = "Lailaps HP: " + lailaps.getHp() + "/" + lailaps.getMaxHP();
        String goldText = "Gold: " + player.getGold();

        Item currentItem = player.getCurrentItem();
        String itemText = "Item: " + (currentItem == null ? "None" : currentItem.getName());

        g.drawString(hpText, 10, barY + 18);
        g.drawString(lailapsText, 150, barY + 18);
        g.drawString(goldText, 350, barY + 18);
        g.drawString(itemText, 10, barY + 36);
    }

    private void drawMap(Graphics g){
        Tile[][] map = bossFight.getMap();

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE + TOP_BAR_HEIGHT;

                Image tileImage = map[i][j].getImage();
                if(tileImage != null){
                    g.drawImage(tileImage, x, y, TILE_SIZE, TILE_SIZE, this);
                } else {
                    g.setColor(Color.MAGENTA);
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        for(Bat bat : bossFight.getBats()){
            int x = bat.getCol() * TILE_SIZE;
            int y = bat.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;
            if(bat.getImage() != null){
                g.drawImage(bat.getImage(), x, y, TILE_SIZE, TILE_SIZE, this);
            } else {
                g.setColor(Color.RED);
                g.fillOval(x + 4, y + 4, TILE_SIZE - 8, TILE_SIZE - 8);
            }
        }

        Siren siren = bossFight.getSiren();
        if(siren != null){
            int x = siren.getCol() * TILE_SIZE;
            int y = siren.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;
            if(siren.getImage() != null){
                g.drawImage(siren.getImage(), x, y, TILE_SIZE, TILE_SIZE, this);
            } else {
                g.setColor(Color.MAGENTA);
                g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
            }
        }

        int lx = lailaps.getCol() * TILE_SIZE;
        int ly = lailaps.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;
        if(lailaps.getImage() != null){
            g.drawImage(lailaps.getImage(), lx, ly, TILE_SIZE, TILE_SIZE, this);
        } else {
            g.setColor(Color.ORANGE);
            g.fillOval(lx + 2, ly + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        }

        int px = player.getCol() * TILE_SIZE;
        int py = player.getRow() * TILE_SIZE + TOP_BAR_HEIGHT;
        if(player.getImage() != null){
            g.drawImage(player.getImage(), px, py, TILE_SIZE, TILE_SIZE, this);
        } else {
            g.setColor(Color.CYAN);
            g.fillOval(px + 2, py + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        }
    }
}
