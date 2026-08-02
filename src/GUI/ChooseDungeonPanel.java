package GUI;

import Dungeon.Dungeon;
import Game.OverallStats;
import LivingThings.Idol;
import LivingThings.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChooseDungeonPanel extends JPanel {

    public interface ChooseDungeonListener{
        void onDungeonSelected(Dungeon dungeon);
        void onFaceSiren();
        void onInventory();
        void onSaveAndQuit();
        void onShop();
    }

    private Player player;
    private ArrayList<Dungeon> dungeons;
    private OverallStats overallStats;
    private ChooseDungeonListener listener;

    public ChooseDungeonPanel(Player player, ArrayList<Dungeon> dungeons, OverallStats overallStats, ChooseDungeonListener listener){
        this.player = player;
        this.dungeons = dungeons;
        this.overallStats = overallStats;
        this.listener = listener;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(520, 320));

        add(buildTopSection(), BorderLayout.NORTH);
        add(buildDungeonButtons(), BorderLayout.CENTER);
        add(buildSideButtons(), BorderLayout.EAST);
    }

    /**
     * Builds the top text block: Lailaps' line, then HP / Gold / current item.
     */
    private JPanel buildTopSection(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String lailapsLine = allDungeonsCleared()
                ? "Lailaps: Yohane! It's time to face the siren!"
                : "Lailaps: Yohane! Where should we go now?";

        JLabel lailapsLabel = new JLabel(lailapsLine);
        lailapsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        String itemName = (player.getCurrentItem() == null) ? "N/A" : player.getCurrentItem().getName();
        JLabel statsLabel = new JLabel(
                "HP: " + player.getHp() + "/" + player.getMaxHP()
                        + "      Gold: " + player.getGold()
                        + "      Item on Hand: " + itemName);
        statsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(lailapsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(statsLabel);
        return panel;
    }

    /**
     * Builds the dungeon-select buttons, OR — once all 3 dungeons are
     * cleared — a single button to face the Siren instead.
     */
    private JPanel buildDungeonButtons(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if(allDungeonsCleared()){
            JButton sirenButton = new JButton("[1] Face the Siren of Numazu");
            sirenButton.addActionListener(e -> listener.onFaceSiren());
            panel.add(sirenButton);
            return panel;
        }

        for(int i = 0; i < dungeons.size(); i++){
            Dungeon dungeon = dungeons.get(i);

            // spec shows "[X]" for cleared dungeons, "[number]" otherwise
            String marker = dungeon.isCleared() ? "X" : String.valueOf(i + 1);
            JButton button = new JButton("[" + marker + "] Visit " + dungeon.getDungeonName());

            button.setEnabled(!dungeon.isCleared()); // can't re-visit a cleared dungeon
            button.addActionListener(e -> listener.onDungeonSelected(dungeon));

            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        return panel;
    }

    /**
     * Builds the right-hand column: Inventory, Save and Quit, and (only if
     * Hanamaru has ever been rescued) Hanamaru's Shop.
     */
    private JPanel buildSideButtons(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(e -> listener.onInventory());
        panel.add(inventoryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));

        JButton saveQuitButton = new JButton("Save and Quit");
        saveQuitButton.addActionListener(e -> listener.onSaveAndQuit());
        panel.add(saveQuitButton);

        if(isHanamaruRescued()){
            panel.add(Box.createRigidArea(new Dimension(0, 6)));
            JButton shopButton = new JButton("Hanamaru's Shop");
            shopButton.addActionListener(e -> listener.onShop());
            panel.add(shopButton);
        }

        return panel;
    }

    /**
     * True once every dungeon in this playthrough has been cleared.
     * Unlocks the Siren battle.
     */
    private boolean allDungeonsCleared(){
        for(Dungeon dungeon : dungeons){
            if(!dungeon.isCleared())
                return false;
        }
        return true;
    }

    /**
     * Hanamaru's shop unlocks once she's been rescued at least once, either
     * this playthrough or in a previous one.
     */
    private boolean isHanamaruRescued(){
        for(Idol idol : overallStats.getAqours()){
            if(idol.getName().equals("Hanamaru") && idol.isRescuedOnce())
                return true;
        }
        return false;
    }
}
