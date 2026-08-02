package GUI;

import Items.Item;
import LivingThings.Player;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    public interface InventoryListener{
        void onBackToHub();
    }

    //the only items that can be tracked
    private static final String[] TRACKED_ITEMS = {
            "Tears of a Fallen Angel",
            "Noppo Bread",
            "Choco-mint Ice Cream"
    };

    public InventoryPanel(Player player, InventoryListener listener){
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(400, 320));

        add(buildTopSection(player), BorderLayout.NORTH);
        add(buildItemList(player), BorderLayout.CENTER);
        add(buildBackButton(listener), BorderLayout.SOUTH);
    }

    private JPanel buildTopSection(Player player){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lailapsLabel = new JLabel("Lailaps: These are the items you have, Yohane!");
        lailapsLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel statsLabel = new JLabel(
                "HP: " + player.getHp() + "/" + player.getMaxHP()
                        + "      Total Gold: " + player.getGold());
        statsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(lailapsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel itemsHeader = new JLabel("Items available");
        itemsHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(itemsHeader);

        return panel;
    }

    /**
     * Builds the numbered list of the 3 trackable items, each with its
     * current quantity (0 if Yohane doesn't have any).
     */
    private JPanel buildItemList(Player player){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

       int number = 1; // number only the items the player has

        for(String itemName : TRACKED_ITEMS){
        
            int quantity = countItemInInventory(player, itemName);
        
            // don't display items with 0 quantity
            if(quantity == 0)
                continue;
        
            JLabel itemLabel =
                    new JLabel(number + ". " + itemName + " x" + quantity);
        
            itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
            panel.add(itemLabel);
            panel.add(Box.createRigidArea(new Dimension(0,4)));
        
            number++;
        }

        // if the player has no items
            if(number == 1){
                JLabel empty = new JLabel("No items.");
                empty.setFont(new Font("SansSerif", Font.PLAIN,14));
                panel.add(empty);
            }
            return panel;
            }

    private JPanel buildBackButton(InventoryListener listener){
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> listener.onBackToHub());

        JPanel wrapper = new JPanel();
        wrapper.add(backButton);
        return wrapper;
    }

    /**
     * Looks through the player's inventory for an item by name and returns
     * its quantity, or 0 if the player doesn't have it.
     */
    private int countItemInInventory(Player player, String itemName){
        for(Item item : player.getInventory()){
            if(item.getName().equals(itemName))
                return item.getQuantity();
        }
        return 0;
    }
}
