package LivingThings;

import Dungeon.Floor;
import Items.Item;
import Items.Upgrades;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    private int gold;
    private ArrayList<Item> inventory;
    private int currentItemIndex;
    private Upgrades upgrades;
    private boolean isAlive;
    private String deathCause;

    private BufferedImage image; // player sprite

    public Player(float hp, float maxHP){
        super(hp, maxHP);
        this.gold = 0;
        this.inventory = new ArrayList<Item>();
        this.currentItemIndex = -1; // -1 if inventory is empty
        this.upgrades = new Upgrades();
        this.isAlive = true;
        this.deathCause = null;
    }

    //------------------------
    // player sprite
    //------------------------
    public void loadPlayerImage(){
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entities/player.png"));
        }catch (IOException e){
            System.out.println("Couldn't load player sprite: " + e.getMessage());
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    //------------------------
    // health
    //------------------------
    /**
     * Deals damage to Player AND records what caused it, but only if this
     * hit brings her HP down to 0. This lets us show "Killed by: Bat" on the game over
     * screen later.
     *
     * @param amount amount of damage taken
     * @param cause what dealt the damage (e.g. "Bat", "Heat Tile")
     */
    public void takeDamage(float amount, String cause){
        //check for bat tamer upgrade
        if(cause.equalsIgnoreCase("Bat") && upgrades.hasBatTamer())
            amount = 0.5f;

        //check for choco minnt and if she has use it
        Item currentItem = getCurrentItem();
        boolean wouldBeFatal = (getHp() - amount) <= 0;

        if(wouldBeFatal && currentItem.getName().equalsIgnoreCase("Choco-mint Ice Cream")
            && currentItem != null){
            consumeCurrentItem(); // use the choco ming
            heal(getMaxHP()); // heal max hp
            return;
        }

        super.takeDamage(amount); // reuses Entity's take damage
        if(!isAlive()){
            this.deathCause = cause;
        }
    }

    /**
     * Checks whether Yohane is still alive
     *
     * @return true if HP is above 0, false otherwise
     */
    public boolean isAlive(){
        return this.getHp() > 0;
    }

    public String getDeathCause() {
        return deathCause;
    }

    /**
     * Increases Player's max HP by the given amount
     *
     * @param amount amount to increase max HP by
     */
    public void increaseMaxHP(float amount){
        setMaxHP(getMaxHP() + amount);
    }

    //------------------------
    // moolah
    //------------------------
    /**
     * Add gold to Yohane's total gold
     *
     * @param amount amount of gold to add
     */
    public void addGold(int amount){
        this.gold += amount;
    }

    /**
     * Returns the total gold Yohane has currently
     *
     * @return current gold
     */
    public int getGold() {
        return this.gold;
    }

    //------------------------
    // inventory
    //------------------------
    /**
     * Adds an item to Yohane's inventory. If Yohane already has that item,
     * its quantity gets incremented. If this is the first item picked up, it
     * becomes the item on hand.
     *
     * @param newItem item to add
     */
    public void addItem(Item newItem){
        //to check if yohane already has that item
        for(Item item : inventory){ // hi amber, this means "for each item in the inventory". this is a for each loop
            if(item.getName().equals(newItem.getName())){
                item.incrementQty(); // add +1 to qty
                return;
            }
        }

        //if yohane dont have that item yet
        inventory.add(newItem);

        if(currentItemIndex == -1) // -1 means empty
            currentItemIndex = inventory.size() - 1; // sets the item index to 0 if yohane has nthg on hand
        // inventory.size = 1 after adding the item so index becomes 0
    }

    /**
     * Returns the current item on hand, null if no items
     *
     * @return current item, null if none
     */
    public Item getCurrentItem(){
        if(inventory.isEmpty() || currentItemIndex == -1) return null;

        return inventory.get(currentItemIndex);
    }

    /**
     * Switches the item on hand to the next item in the inventory
     */
    public void switchToNextItem(){
        if(inventory.isEmpty()) return;

        currentItemIndex++;
        if(currentItemIndex >= inventory.size())
            currentItemIndex = 0; // go back to the first item
    }

    /**
     * Switches the item on hand to the previous item in the inventory
     */
    public void switchToPreviousItem(){
        if(inventory.isEmpty()) return;

        currentItemIndex--;
        if(currentItemIndex < 0)
            currentItemIndex = inventory.size() - 1; // go to the last item in list
    }

    /**
     * Decrements the quantity of whatever item is currently on hand,
     * removing it from the inventory entirely once that hits 0. Shared by
     * useCurrentItem() (manual use) and the Choco-mint Ice Cream auto-save,
     * so both "use an item" the same way.
     */
    private void consumeCurrentItem(){
        Item item = getCurrentItem();
        if(item == null) return;

        item.decrementQty();
        if(item.getQuantity() == 0){
            inventory.remove(item);
            currentItemIndex = -1;
        }
    }

    /**
     * Use the item current on hand and decremnt its quantity. If item qty drops
     * to 0 then it is removed from the inventory and current item on hand becomes
     * N/A
     *
     * @param floor floor yohane is on
     */
    public void useCurrentItem(Floor floor){
        Item item = getCurrentItem();

        if(item == null) return;

        if(item.getName().equalsIgnoreCase("Noppo Bread")) {
            heal((float) 0.5); // the possible item we can get rn is only noppo bread for mco1
        } else if(item.getName().equalsIgnoreCase("Tears of a Fallen Angel")){
            heal(0.5f);
            consumeCurrentItem();
        }

        item.decrementQty();
        if(item.getQuantity() == 0){
            inventory.remove(item);
            currentItemIndex = -1; // players has to [ ] to have smth on hand
        }
    }

    /**
     * Returns Yohane's entire inventory
     *
     * @return list of items in inventory
     */
    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    /**
     * Returns Player's current upgrades
     *
     * @return Player's upgrades for this playthrough
     */
    public Upgrades getUpgrades() {
        return upgrades;
    }
}
