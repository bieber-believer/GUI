package GUI;

import Dungeon.BossFight;
import Dungeon.Dungeon;
import Dungeon.Floor;
import Game.OverallStats;
import Game.SaveManager;
import Items.Item;
import LivingThings.Idol;
import LivingThings.Lailaps;
import LivingThings.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;

public class GameFrame extends JFrame implements MenuPanel.MenuListener,
        ChooseDungeonPanel.ChooseDungeonListener,
        GamePanel.FloorCompleteListener, GamePanel.PlayerDeathListener,
        CreditsPanel.CreditsListener, StatusPanel.StatusListener,
        InventoryPanel.InventoryListener, ShopPanel.ShopListener,
        BossFightPanel.GameWonListener, BossFightPanel.GameOverListener{
     private CardLayout cardLayout;
     private JPanel cardContainer; // holds every screen; card layout shows one at a time

    private static final String MENU_PANEL = "menu";
    private static final String CHOOSE_PANEL = "choose";
    private static final String GAME_PANEL = "game";
    private static final String CREDITS_PANEL = "credits";
    private static final String STATUS_PANEL = "status";
    private static final String INVENTORY_PANEL = "inventory";
    private static final String SHOP_PANEL = "shop";
    private static final String BOSS_FIGHT_PANEL = "bossFight";

    private Player player;
    private OverallStats overallStats;
    private ArrayList<Dungeon> dungeons;   // the 3 dungeons for this playthrough
    private ArrayList<Idol> idolsToRescue; // the idols tied to those dungeons

    private Dungeon activeDungeon; // whichever dungeon the player is currently inside
    private GamePanel gamePanel;   // the panel currently showing activeDungeon's floor


    private boolean hasSavedGame = false;      // true after "Save and Quit"
    private boolean hasCompletedOrDied = false; // true after any death or siren pwned

    public GameFrame(){
        setTitle("Yohane The Parhelion!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        add(cardContainer);

        overallStats = new OverallStats(); // TODO: later, load this from a save file instead

        showMainMenu();
        setVisible(true);
    }

    //------------------------
    // Main Menu
    //------------------------
    private void showMainMenu(){
        MenuPanel menuPanel = new MenuPanel(hasSavedGame, hasCompletedOrDied, this);
        cardContainer.add(menuPanel, MENU_PANEL);
        pack();
        cardLayout.show(cardContainer, MENU_PANEL);
    }

    @Override
    public void onContinue(){
        // player/dungeons/idolsToRescue are still sitting in memory from
        // before "Save and Quit" was pressed (which only happens from the
        // dungeon hub), so we can just show that hub again as-is
        showChoosePanel();
    }

    @Override
    public void onNewGame(){
        hasSavedGame = false; // discards any previous save
        startNewPlaythrough();
    }

    @Override
    public void onStatus(){
        StatusPanel statusPanel = new StatusPanel(overallStats, this);
        cardContainer.add(statusPanel, STATUS_PANEL);
        pack();
        cardLayout.show(cardContainer, STATUS_PANEL);
    }

    @Override
    public void onCredits(){
        CreditsPanel creditsPanel = new CreditsPanel(this);
        cardContainer.add(creditsPanel, CREDITS_PANEL);
        pack();
        cardLayout.show(cardContainer, CREDITS_PANEL);
    }

    @Override
    public void onBack(){
        showMainMenu();
    }

    @Override
    public void onQuit(){
        System.exit(0);
    }

    //------------------------
    // Starting/loading a playthrough
    //------------------------

    /**
     * Sets up a fresh playthrough: new Player, a map pool,
     * and 3 randomly chosen idols to rescue.
     */
    private void startNewPlaythrough(){
        player = new Player(3f, 3f); // starting HP
        player.loadPlayerImage();

        // carry gold and saved items
        player.addGold(overallStats.getGold());
        for(int i = 0; i < overallStats.getNoppoBreadCount(); i++){
            player.addItem(new Item("Noppo Bread"));
        }
        for(int i = 0; i < overallStats.getTearsOfAngelCount(); i++){
            player.addItem(new Item("Tears of a Fallen Angel"));
        }

        Dungeon.resetMapPool(); // fresh shuffled 7 maps so floors don't repeat

        // randomly pick 3 of the 8 idols to be rescued this playthrough
        ArrayList<Idol> shuffled = new ArrayList<>(overallStats.getAqours());
        Collections.shuffle(shuffled);

        idolsToRescue = new ArrayList<>();
        dungeons = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Idol idol = shuffled.get(i);
            idolsToRescue.add(idol);
            // dungeonOrder (1, 2, 3) controls how many floors that dungeon has
            dungeons.add(new Dungeon(idol.getDungeonName(), i + 1));
        }

        showChoosePanel();
    }

    /**
     * (Re)builds the hub screen so it reflects the player's current
     * HP/gold/item and which dungeons are cleared, then shows it.
     */
    private void showChoosePanel(){
        ChooseDungeonPanel chooseDungeonPanel = new ChooseDungeonPanel(player, dungeons, overallStats, this);
        cardContainer.add(chooseDungeonPanel, CHOOSE_PANEL);

        pack(); // resizes the window to fit whichever card is showing
        cardLayout.show(cardContainer, CHOOSE_PANEL);
    }

    //------------------------
    // helpers
    //------------------------
    private void saveProgressToOverallStats(){
        overallStats.setGold(player.getGold());
        overallStats.setNoppoBreadCount(countItemInInventory("Noppo Bread"));
        overallStats.setTearsOfAngelCount(countItemInInventory("Tears of a Fallen Angel"));
    }

private void saveGame(){
    try{
        PrintWriter out = SaveManager.getWriter();

        //------------------------
        // PLAYER
        //------------------------
        out.println("HP=" + player.getHp());
        out.println("GOLD=" + player.getGold());
        out.println("BREAD=" + countItemInInventory("Noppo Bread"));
        out.println("TEARS=" + countItemInInventory("Tears of a Fallen Angel"));

        //------------------------
        // UPGRADES
        //------------------------
        out.println("SHOVEL=" + player.getUpgrades().hasShovelUpgrade());
        out.println("BATTAMER=" + player.getUpgrades().hasBatTamer());
        out.println("AIRSHOES=" + player.getUpgrades().hasAirShoes());
        out.println("STEWSHINE=" + player.getUpgrades().hasStewshine());
        out.println("MIKANMOCHI=" + player.getUpgrades().hasMikanMochi());
        out.println("KUROSAWAMACHA=" + player.getUpgrades().hasKurosawaMacha());
        out.println("CHOCOMINT=" + player.getUpgrades().hasChocoMintOnce());
        out.println("TEARSONCE=" + player.getUpgrades().hasTearsOnce());

        //------------------------
        // DUNGEONS
        //------------------------
        for(Dungeon dungeon : dungeons){
            out.println("NAME=" + dungeon.getDungeonName());
            out.println("ORDER=" + dungeon.getDungeonOrder());
            out.println("CLEARED=" + dungeon.isCleared());
        }

        //------------------------
        // IDOLS
        //------------------------
        for(Idol idol : idolsToRescue){
            out.println("IDOL=" + idol.getName());
        }

        out.close();

    }catch(IOException e){
        e.printStackTrace();
    }
}

    private int countItemInInventory(String itemName){
        for(Item item : player.getInventory()){
            if(item.getName().equals(itemName))
                return item.getQuantity();
        }
        return 0; // player doesn't have that item
    }

    //------------------------
    // ChooseDungeonListener — reacting tscreen buttons
    //------------------------
    @Override
    public void onDungeonSelected(Dungeon dungeon){
        loadFloor(dungeon);
    }

    @Override
    public void onFaceSiren(){
        BossFight bossFight = new BossFight("/floorMaps/bossMap.txt"); //
        Lailaps lailaps = new Lailaps(4.0f,4.0f); // spec's sample screen: "Lailaps HP: 4/4"
        lailaps.loadImage();

        try {
            bossFight.loadBossMap(player, lailaps);
        } catch (IOException e) {
            System.out.println("Couldn't load boss fight: " + e.getMessage());
            return;
        }

        if(gamePanel != null){
            gamePanel.stopTimers();
            cardContainer.remove(gamePanel);
        }

        BossFightPanel bossFightPanel = new BossFightPanel(bossFight);
        bossFightPanel.setGameWonListener(this);
        bossFightPanel.setGameOverListener(this);
        cardContainer.add(bossFightPanel, BOSS_FIGHT_PANEL);

        pack();
        cardLayout.show(cardContainer, BOSS_FIGHT_PANEL);
        bossFightPanel.requestFocusInWindow();
    }

    @Override
    public void onInventory(){
        InventoryPanel inventoryPanel = new InventoryPanel(player, this);
        cardContainer.add(inventoryPanel, INVENTORY_PANEL);
        pack();
        cardLayout.show(cardContainer, INVENTORY_PANEL);
    }

    @Override
    public void onSaveAndQuit(){
        saveProgressToOverallStats();
        saveGame();
        hasSavedGame = true;
        System.exit(0);
    }

    @Override
    public void onShop(){
        ShopPanel shopPanel = new ShopPanel(player, overallStats, this);
        cardContainer.add(shopPanel, SHOP_PANEL);
        pack();
        cardLayout.show(cardContainer, SHOP_PANEL);
    }

    //------------------------
    // Loading floors
    //------------------------

    /**
     * Loads the given dungeon's CURRENT floor into a fresh GamePanel and
     * switches to it. Used both when first entering a dungeon and when
     * moving on to that dungeon's next floor.
     */
    private void loadFloor(Dungeon dungeon){
        this.activeDungeon = dungeon;
        Floor floor = dungeon.getCurrentFloor();

        try {
            floor.loadMap(player);
        } catch (IOException e) {
            System.out.println("Couldn't load floor: " + e.getMessage());
            return;
        }

        if(gamePanel != null){
            gamePanel.stopTimers();     // stop the old floor's bat/heat timers first!
            cardContainer.remove(gamePanel);
        }

        gamePanel = new GamePanel(floor);
        gamePanel.setFloorCompleteListener(this);
        gamePanel.setPlayerDeathListener(this);
        cardContainer.add(gamePanel, GAME_PANEL);

        pack();
        cardLayout.show(cardContainer, GAME_PANEL);
        gamePanel.requestFocusInWindow(); // so WASD works immediately
    }

    //------------------------
    // FloorCompleteListener — reacting to reaching the exit tile
    //------------------------
    @Override
    public void onFloorComplete(){
        if(activeDungeon.goToNextFloor()){
            // more floors left in this dungeon
            JOptionPane.showMessageDialog(this, "Floor Complete!", "Floor Cleared",
                    JOptionPane.INFORMATION_MESSAGE);
            loadFloor(activeDungeon);
        } else {
            // that was the LAST floor — the whole dungeon is cleared
            onDungeonComplete();
        }
    }

    /**
     * Marks the dungeon as cleared, rescues its matching idol, shows the
     * victory popup, and returns to the hub screen.
     */
    private void onDungeonComplete(){
        activeDungeon.setCleared(true);
        gamePanel.stopTimers(); // dungeon is done, stop its timers too

        saveProgressToOverallStats();

        for(Idol idol : idolsToRescue){
        if(idol.getDungeonName().equals(activeDungeon.getDungeonName())){

            idol.rescue();

            // determine what this idol unlocks
            String unlocked = "";

            switch(idol.getName()){
                case "Kanan":
                    unlocked = "Shovel Upgrade";
                    break;
                case "Riko":
                    unlocked = "Bat Tamer";
                    break;
                case "You":
                    unlocked = "Air Shoes";
                    break;
                case "Mari":
                    unlocked = "Stewshine";
                    break;
                case "Chika":
                    unlocked = "Mikan Mochi";
                    break;
                case "Dia":
                    unlocked = "Kurosawa Macha";
                    break;
                case "Ruby":
                    unlocked = "Choco-Mint Ice Cream";
                    break;
            }

            JOptionPane.showMessageDialog(
                    this, activeDungeon.getDungeonName() + " Completed!\n\n" + idol.getName() + " rescued!\n\n"
                    + "You unlocked:\n" + unlocked + "!", "Dungeon Cleared!", JOptionPane.INFORMATION_MESSAGE
            );

            break;
        }
    }

    showChoosePanel();
}

    //------------------------
    // PlayerDeathListener
    //------------------------
    @Override
    public void onPlayerDeath(String cause){
        gamePanel.stopTimers(); // stop bat/heat timers immediately, game is over

        saveProgressToOverallStats(); // gold/items survive a game over per the spec
        overallStats.addGamesLost();
        hasCompletedOrDied = true; // unlocks "New Game+" on the main menu from now on
        hasSavedGame = false;      // a dead playthrough can't be "continued"

        Object[] options = { "Back to Main Menu" };
        JOptionPane.showOptionDialog(this,
                "You Died!\nKilled by: " + cause,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]);

        showMainMenu();
    }

    //------------------------
    // BossFight outcome
    //------------------------
    @Override
    public void onGameWon(){
        saveProgressToOverallStats();
        overallStats.addNumSiren(); // tracked on the Status screen
        hasCompletedOrDied = true;  // unlocks "New Game+"
        hasSavedGame = false;

        JOptionPane.showMessageDialog(this,
                "Congratulations! You defeated the Siren and saved Aqours!",
                "Victory!", JOptionPane.INFORMATION_MESSAGE);

        showMainMenu();
    }

    @Override
    public void onGameOver(String cause){
        saveProgressToOverallStats();
        overallStats.addGamesLost();
        hasCompletedOrDied = true;
        hasSavedGame = false;

        Object[] options = { "Back to Main Menu" };
        JOptionPane.showOptionDialog(this,
                "You Died!\nKilled by: " + cause,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]);

        showMainMenu();
    }

    //------------------------
    // Inventory
    //----------------------
    @Override
    public void onBackToHub(){
        showChoosePanel();
    }

    private void saveGame() {
    try {
        PrintWriter out = SaveManager.getWriter();

        // PLAYER
        out.println(player.getHp());
        out.println(player.getGold());
        out.println(countItemInInventory("Noppo Bread"));
        out.println(countItemInInventory("Tears of a Fallen Angel"));

        // UPGRADES
        out.println(player.getUpgrades().hasShovelUpgrade());
        out.println(player.getUpgrades().hasBatTamer());
        out.println(player.getUpgrades().hasAirShoes());
        out.println(player.getUpgrades().hasStewshine());
        out.println(player.getUpgrades().hasMikanMochi());
        out.println(player.getUpgrades().hasKurosawaMacha());
        out.println(player.getUpgrades().hasChocoMintOnce());
        out.println(player.getUpgrades().hasTearsOnce());

        // DUNGEONS
        for(Dungeon dungeon : dungeons){
            out.println(dungeon.getDungeonName());
            out.println(dungeon.getDungeonOrder());
            out.println(dungeon.isCleared());
        }

        out.close();

    } catch(IOException e){
        e.printStackTrace();
    }
}
}
