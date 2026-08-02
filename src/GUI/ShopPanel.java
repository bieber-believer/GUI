package GUI;

import Game.OverallStats;
import Items.Shop;
import LivingThings.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShopPanel extends JPanel {
    public interface ShopListener{
        void onBackToHub();
    }

    private Player player;
    private OverallStats overallStats;
    private ShopListener listener;

    private JLabel goldLabel;
    private JPanel itemListPanel;

    public ShopPanel(Player player, OverallStats overallStats, ShopListener listener){
        this.player = player;
        this.overallStats = overallStats;
        this.listener = listener;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(460, 380));

        add(buildTopSection(), BorderLayout.NORTH);

        itemListPanel = new JPanel();
        itemListPanel.setLayout(new BoxLayout(itemListPanel, BoxLayout.Y_AXIS));
        refreshItemList();
        add(new JScrollPane(itemListPanel), BorderLayout.CENTER);

        add(buildBackButton(), BorderLayout.SOUTH);
    }

    private JPanel buildTopSection(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel hanamaruLabel = new JLabel("Hanamaru: Yohane-chan, zura! What can I do for you today?");
        hanamaruLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        goldLabel = new JLabel("Total Gold: " + player.getGold() + " GP");
        goldLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(hanamaruLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(goldLabel);
        return panel;
    }

    /**
     * Rebuilds the item list from scratch — reflecting the player's
     * current gold and which one-time purchases are already owned.
     * Called once at startup, and again after every purchase attempt so
     * the screen stays in sync without needing to reopen the shop.
     */
    private void refreshItemList(){
        itemListPanel.removeAll();

        ArrayList<Shop.ShopItem> items = Shop.getAvailableItems(overallStats);
        for(int i = 0; i < items.size(); i++){
            itemListPanel.add(buildItemRow(i + 1, items.get(i)));
            itemListPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        goldLabel.setText("Total Gold: " + player.getGold() + " GP");

        itemListPanel.revalidate(); // tells Swing "the components in here changed, recalculate layout"
        itemListPanel.repaint();
    }

    private JPanel buildItemRow(int number, Shop.ShopItem item){
        JPanel row = new JPanel(new BorderLayout(10, 0));

        String labelText = number + ". " + item.getName() + " (" + item.getPrice() + "GP) - " + item.getDescription();
        JLabel itemLabel = new JLabel(labelText);
        itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        row.add(itemLabel, BorderLayout.CENTER);

        boolean alreadyOwned = item.isPurchaseOnce() && Shop.isAlreadyPurchased(item, player);

        JButton buyButton = new JButton(alreadyOwned ? "Owned" : "Buy");
        buyButton.setEnabled(!alreadyOwned);
        buyButton.addActionListener(e -> attemptPurchase(item));
        row.add(buyButton, BorderLayout.EAST);

        return row;
    }

    private void attemptPurchase(Shop.ShopItem item){
        Shop.PurchaseResult result = Shop.purchase(item, player, overallStats);

        switch(result){
            case YOU_BROKE:
                JOptionPane.showMessageDialog(this,
                        "You don't have enough gold for that, zura!",
                        "Not Enough Gold", JOptionPane.WARNING_MESSAGE);
                break;
            case ALREADY_PURCHASED:
                JOptionPane.showMessageDialog(this,
                        "You've already bought that this playthrough!",
                        "Already Purchased", JOptionPane.WARNING_MESSAGE);
                break;
            case SUCCESS:
                JOptionPane.showMessageDialog(this,
                        item.getName() + " purchased!",
                        "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
                break;
        }

        refreshItemList(); // updates gold total + disables "Owned" items either way
    }

    private JPanel buildBackButton(){
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> listener.onBackToHub());

        JPanel wrapper = new JPanel();
        wrapper.add(backButton);
        return wrapper;
    }
}
